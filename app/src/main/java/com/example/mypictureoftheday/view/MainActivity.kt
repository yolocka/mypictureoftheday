package com.example.mypictureoftheday.view

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import coil.load
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.model.PictureData
import com.example.mypictureoftheday.view.archive.ArchiveActivity
import com.example.mypictureoftheday.viewmodel.MainViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.collapsing_toolbar.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(SHAR_PREF_NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy {
        sharedPref.edit()
    }
    private var toMoveFABAnimation = false
    private var isExpanded = false

    companion object {
        const val PREF_THEME = "app_theme"
        const val IS_CHANGED = "is_changed"
        const val SHAR_PREF_NAME = "my_pref"
        const val PLUM_THEME = "Слива"
        const val MANDARIN_THEME = "Мандарин"
        const val BLUEBERRY_THEME = "Черника"
        const val MEDIA_TYPE_VIDEO = "video"
        const val MEDIA_TYPE_IMAGE = "image"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        when (sharedPref.getString(PREF_THEME, "")) {
            PLUM_THEME -> setTheme(R.style.PlumTheme)
            MANDARIN_THEME -> setTheme(R.style.MandarinTheme)
            BLUEBERRY_THEME -> setTheme(R.style.BlueberryTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(this, Observer<PictureData> { renderData(it) })

        setBottomAppBar()

        image_view.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                main_activity,
                ChangeImageTransform()
            )
            val params: ViewGroup.LayoutParams = image_view.layoutParams
            params.height =
                if (isExpanded) main_activity.height else ViewGroup.LayoutParams.MATCH_PARENT
            image_view.layoutParams = params
        }

        my_fab.setOnClickListener {
            toMoveFABAnimation = !toMoveFABAnimation
            val params = my_fab.layoutParams as CoordinatorLayout.LayoutParams
            params.anchorGravity =
                if (toMoveFABAnimation) Gravity.END or Gravity.BOTTOM or Gravity.LEFT
                else Gravity.END or Gravity.BOTTOM or Gravity.RIGHT
            my_fab.layoutParams = params
        }
    }

    override fun onResume() {
        super.onResume()
        if (sharedPref.getBoolean(IS_CHANGED, false)) {
            editor.let {
                editor.putBoolean(IS_CHANGED, false)
                editor.apply()
            }
            recreate()
        }
    }


    private fun setBottomAppBar() {
        setSupportActionBar(findViewById(R.id.bottom_bar))
        bottom_bar.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_baseline_menu_24)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
            R.id.item_archive -> {
                startActivity(Intent(this, ArchiveActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
            android.R.id.home -> {
                BottomNavigationFragment().show(supportFragmentManager, "tag")
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun renderData(data: PictureData) {
        when (data) {
            is PictureData.Success -> {
                val serverResponseData = data.serverResponseData
                collapsing_title.text = serverResponseData.title
                collapsing_text.text = serverResponseData.explanation
                val url = serverResponseData.url
                if (serverResponseData.mediaType == MEDIA_TYPE_IMAGE) {
                    image_view.load(url)
                } else if (serverResponseData.mediaType == MEDIA_TYPE_VIDEO) {
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
}