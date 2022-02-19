package com.example.mypictureoftheday.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mypictureoftheday.model.DataLoader
import com.example.mypictureoftheday.model.PictureData
import java.time.LocalDate

class ArchiveFragmentViewModel : ViewModel() {
    private val now: LocalDate = LocalDate.now()
    fun getData(daysBefore: Long) : LiveData<PictureData> {
        val yesterday = now.minusDays(daysBefore)
        return DataLoader().getData(yesterday)
    }
}