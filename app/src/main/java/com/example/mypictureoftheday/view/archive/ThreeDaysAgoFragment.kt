package com.example.mypictureoftheday.view.archive

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.model.PictureData
import com.example.mypictureoftheday.view.MainActivity
import com.example.mypictureoftheday.viewmodel.ThreeDaysAgoFragmentViewModel
import com.example.mypictureoftheday.viewmodel.TwoDaysAgoFragmentViewModel
import kotlinx.android.synthetic.main.fragment_three_days_ago.*
import kotlinx.android.synthetic.main.fragment_two_days_ago.*
import kotlinx.android.synthetic.main.fragment_yesterday.*

class ThreeDaysAgoFragment : Fragment() {

    private lateinit var viewModel: ThreeDaysAgoFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ThreeDaysAgoFragmentViewModel::class.java)
        viewModel.getData().observe(this, Observer<PictureData> { renderData(it) })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_three_days_ago, container, false)
    }

    private fun renderData(data: PictureData) {
        when (data) {
            is PictureData.Success -> {
                val serverResponseData = data.serverResponseData
                val bottomSheetTextView: TextView? = activity?.findViewById(R.id.bottom_sheet_text)
                bottomSheetTextView?.text = serverResponseData.explanation
                val url = serverResponseData.url
                if (serverResponseData.mediaType == MainActivity.MEDIA_TYPE_IMAGE) {
                    three_days_ago_image_view.load(url)
                } else if (serverResponseData.mediaType == MainActivity.MEDIA_TYPE_VIDEO) {
                    three_days_ago_image_view.visibility = View.INVISIBLE
                    three_days_ago_video_button.visibility = View.VISIBLE
                    three_days_ago_video_button.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
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
        fun newInstance() = ThreeDaysAgoFragment()
    }
}