package com.example.mypictureoftheday.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintSet
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.IS_CHANGED
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.MANDARIN_THEME
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.BLUEBERRY_THEME
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.PLUM_THEME
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.PREF_THEME
import com.example.mypictureoftheday.view.pod.MainActivity.Companion.SHAR_PREF_NAME
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_settings_start.*
import java.time.LocalDate

class SettingsActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(SHAR_PREF_NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy {
        sharedPref.edit()
    }
    private var isDateShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        when (sharedPref.getString(PREF_THEME, "Черника")) {
            PLUM_THEME -> setTheme(R.style.PlumTheme)
            MANDARIN_THEME -> setTheme(R.style.MandarinTheme)
            BLUEBERRY_THEME -> setTheme(R.style.BlueberryTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_start)

        date_text_field.text = LocalDate.now().toString()

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

        show_date_button.setOnClickListener{
            if (isDateShowing) hideDate()
            else showDate()
        }
    }

    private fun hideDate(){
        isDateShowing = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_settings_start)

        val changeBounds = ChangeBounds()
        changeBounds.duration = 1000
        TransitionManager.beginDelayedTransition(setting_container, changeBounds)
        constraintSet.applyTo(setting_container)
    }

    private fun showDate(){
        isDateShowing = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_settings_end_showing_date)

        val changeBounds = ChangeBounds()
        changeBounds.duration = 1000
        TransitionManager.beginDelayedTransition(setting_container, changeBounds)
        constraintSet.applyTo(setting_container)
    }
}