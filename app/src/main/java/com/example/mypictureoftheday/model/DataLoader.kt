package com.example.mypictureoftheday.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mypictureoftheday.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class DataLoader (private val liveDataForViewObserve: MutableLiveData<PictureData> = MutableLiveData(),
                  private val retrofitImpl: PictureRetrofitImpl = PictureRetrofitImpl()) {

    fun getData(date: LocalDate) : LiveData<PictureData> {
        sendServerRequest(date)
        return liveDataForViewObserve
    }

    private fun sendServerRequest(date: LocalDate) {
        liveDataForViewObserve.value = PictureData.Loading(null)

        val apiKey : String = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            PictureData.Error(Throwable("нет API ключа"))
        } else {
            retrofitImpl.getRetrofitImpl().getPicture(apiKey, date).enqueue(object :
                Callback<PictureDTO> {
                override fun onResponse(call: Call<PictureDTO>, response: Response<PictureDTO>) {
                    if(response.isSuccessful && response.body() != null) {
                        liveDataForViewObserve.value = PictureData.Success(response.body()!!)
                    } else {
                        val message = response.message()

                        if (message.isNullOrEmpty()) {
                            liveDataForViewObserve.value = PictureData.Error(Throwable("Неопределенная ошибка"))
                        } else {
                            liveDataForViewObserve.value = PictureData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PictureDTO>, t: Throwable) {
                    liveDataForViewObserve.value = PictureData.Error(Throwable(t))
                }
            })
        }
    }
}