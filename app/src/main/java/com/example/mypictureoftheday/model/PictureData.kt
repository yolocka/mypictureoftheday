package com.example.mypictureoftheday.model

sealed class PictureData {
    data class Success(val serverResponseData: PictureDTO) : PictureData()
    data class Error(val error: Throwable) : PictureData()
    data class Loading(val progress: Int?) : PictureData()
}