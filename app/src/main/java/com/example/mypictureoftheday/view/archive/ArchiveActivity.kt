package com.example.mypictureoftheday.view.archive

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.view.pod.MainActivity
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.MANDARIN_THEME
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.BLUEBERRY_THEME
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.PLUM_THEME
import kotlinx.android.synthetic.main.activity_achive.*

class ArchiveActivity : AppCompatActivity() {

    companion object {
        const val YESTERDAY: Long = 1
        const val TWO_DAYS_AGO: Long = 2
        const val THREE_DAYS_AGO: Long = 3
    }

    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(MainActivity.SHAR_PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when (sharedPref.getString(MainActivity.PREF_THEME, "")) {
            PLUM_THEME -> setTheme(R.style.PlumTheme)
            MANDARIN_THEME -> setTheme(R.style.MandarinTheme)
            BLUEBERRY_THEME -> setTheme(R.style.BlueberryTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achive)

        supportFragmentManager.beginTransaction()
            .add(R.id.archive_container, ArchiveFragment.newInstance(YESTERDAY))
            .commit()

        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_yesterday -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.archive_container, ArchiveFragment.newInstance(YESTERDAY))
                        .commitAllowingStateLoss()
                    true
                }
                R.id.bottom_view_two_days_ago -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.archive_container, ArchiveFragment.newInstance(TWO_DAYS_AGO))
                        .commitAllowingStateLoss()
                    true
                }
                R.id.bottom_view_three_days_ago -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.archive_container, ArchiveFragment.newInstance(THREE_DAYS_AGO))
                        .commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }

    }
}