package com.example.mypictureoftheday.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mypictureoftheday.model.DataLoader
import com.example.mypictureoftheday.model.PictureData
import java.time.LocalDate

class TwoDaysAgoFragmentViewModel : ViewModel() {
    private val now: LocalDate = LocalDate.now()
    fun getData() : LiveData<PictureData> {
        val yesterday = now.minusDays(2)
        return DataLoader().getData(yesterday)
    }
}