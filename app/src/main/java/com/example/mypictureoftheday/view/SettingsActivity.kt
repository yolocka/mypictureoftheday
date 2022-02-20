package com.example.mypictureoftheday.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.view.MainActivity.Companion.IS_CHANGED
import com.example.mypictureoftheday.view.MainActivity.Companion.MINT_THEME
import com.example.mypictureoftheday.view.MainActivity.Companion.ORANGE_THEME
import com.example.mypictureoftheday.view.MainActivity.Companion.PLUM_THEME
import com.example.mypictureoftheday.view.MainActivity.Companion.PREF_THEME
import com.example.mypictureoftheday.view.MainActivity.Companion.SHAR_PREF_NAME
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(SHAR_PREF_NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy {
        sharedPref.edit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when (sharedPref.getString(PREF_THEME, "")) {
            PLUM_THEME -> setTheme(R.style.PlumTheme)
            MINT_THEME -> setTheme(R.style.MintTheme)
            ORANGE_THEME -> setTheme(R.style.OrangeTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setting_chip_group.setOnCheckedChangeListener { setting_chip_group, possition ->
            setting_chip_group.findViewById<Chip>(possition)?.let {
                var text = it.text
                editor.let {
                    editor.putString(PREF_THEME, text as String?)
                    editor.putBoolean(IS_CHANGED, true)
                    editor.apply()
                }
                recreate()
            }
        }
    }
}