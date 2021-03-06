package com.example.mypictureoftheday.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mypictureoftheday.model.DataLoader
import com.example.mypictureoftheday.model.PictureData
import java.time.LocalDate

class MainViewModel : ViewModel() {
    fun getData() : LiveData<PictureData> {
        return DataLoader().getData(LocalDate.now())
    }
}