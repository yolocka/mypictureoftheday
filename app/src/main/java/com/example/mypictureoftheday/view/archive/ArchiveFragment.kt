package com.example.mypictureoftheday.view.archive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.model.PictureData
import androidx.lifecycle.Observer
import coil.load
import com.example.mypictureoftheday.view.pod.MainActivity
import com.example.mypictureoftheday.viewmodel.ArchiveFragmentViewModel
import kotlinx.android.synthetic.main.collapsing_toolbar.*

class ArchiveFragment(daysBefore: Long) : Fragment() {

    private lateinit var viewModel: ArchiveFragmentViewModel
    private val daysBefore = daysBefore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ArchiveFragmentViewModel::class.java)
        viewModel.getData(daysBefore).observe(this, Observer<PictureData> { renderData(it) })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_archive, container, false)
    }

    private fun renderData(data: PictureData) {
        when (data) {
            is PictureData.Success -> {
                val serverResponseData = data.serverResponseData
                collapsing_title.text = serverResponseData.title
                collapsing_text.text = serverResponseData.explanation
                val url = serverResponseData.url
                if (serverResponseData.mediaType == MainActivity.MEDIA_TYPE_IMAGE) {
                    image_view.load(url)
                } else if (serverResponseData.mediaType == MainActivity.MEDIA_TYPE_VIDEO) {
                    image_view.visibility = View.INVISIBLE
                    with(video_web_view){
                        visibility = View.VISIBLE
                        webViewClient = WebViewClient()
                        settings.mediaPlaybackRequiresUserGesture = false
                        settings.javaScriptEnabled = true
                    }
                    url?.let {
                        video_web_view.loadUrl(url)
                    }
                }
            }
            is PictureData.Loading -> {
                // Ничего нет
            }
            is PictureData.Error -> {

            }
        }
    }

    companion object {
        fun newInstance(daysBefore: Long): ArchiveFragment {
            return ArchiveFragment(daysBefore)
        }
    }
}