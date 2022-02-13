package com.example.mypictureoftheday.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.model.PictureData
import com.example.mypictureoftheday.view.archive.ArchiveActivity
import com.example.mypictureoftheday.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_yesterday.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(SHAR_PREF_NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy {
        sharedPref.edit()
    }


    companion object {
        const val PREF_THEME = "app_theme"
        const val IS_CHANGED = "is_changed"
        const val SHAR_PREF_NAME = "my_pref"
        const val PLUM_THEME = "Слива"
        const val MINT_THEME = "Мята"
        const val ORANGE_THEME = "Апельсин"
        const val MEDIA_TYPE_VIDEO = "video"
        const val MEDIA_TYPE_IMAGE = "image"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        when (sharedPref.getString(PREF_THEME, "")) {
            PLUM_THEME -> setTheme(R.style.PlumTheme)
            MINT_THEME -> setTheme(R.style.MintTheme)
            ORANGE_THEME -> setTheme(R.style.OrangeTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(this, Observer<PictureData> { renderData(it) })

        setBottomSheetBehavior(findViewById(R.id.bottom_sheet_container))

        setBottomAppBar()

        input_layout.setEndIconOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
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
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.item_archive -> {
                startActivity(Intent(this, ArchiveActivity::class.java))
            }
            android.R.id.home -> {
                BottomNavigationFragment().show(supportFragmentManager, "tag")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }


    private fun renderData(data: PictureData) {
        when (data) {
            is PictureData.Success -> {
                val serverResponseData = data.serverResponseData
                val bottomSheetTextView: TextView = findViewById(R.id.bottom_sheet_text)
                bottomSheetTextView.text = serverResponseData.explanation
                val url = serverResponseData.url
                if (serverResponseData.mediaType == MEDIA_TYPE_IMAGE) {
                    image_view.load(url)
                } else if (serverResponseData.mediaType == MEDIA_TYPE_VIDEO) {
                    image_view.visibility = View.INVISIBLE
                    main_video_button.visibility = View.VISIBLE
                    main_video_button.setOnClickListener {
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
}