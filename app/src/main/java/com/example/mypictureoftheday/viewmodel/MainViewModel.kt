package com.example.mypictureoftheday.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypictureoftheday.BuildConfig
import com.example.mypictureoftheday.model.PictureDTO
import com.example.mypictureoftheday.model.PictureData
import com.example.mypictureoftheday.model.PictureRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val liveDataForViewObserve: MutableLiveData<PictureData> = MutableLiveData(),
    private val retrofitImpl: PictureRetrofitImpl = PictureRetrofitImpl()
) : ViewModel() {
    fun getData() : LiveData<PictureData> {
        sendServerRequest()
        return liveDataForViewObserve
    }
    private fun sendServerRequest() {
        liveDataForViewObserve.value = PictureData.Loading(null)

        val apiKey : String = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            PictureData.Error(Throwable("нет API ключа"))
        } else {
            retrofitImpl.getRetrofitImpl().getPicture(apiKey).enqueue(object : Callback<PictureDTO> {
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