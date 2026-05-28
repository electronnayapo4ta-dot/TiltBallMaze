package com.yourcompany.tiltballmaze.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yourcompany.tiltballmaze.databinding.ActivitySettingsBinding
import com.yourcompany.tiltballmaze.game.audio.SoundManager
import com.yourcompany.tiltballmaze.game.data.GamePreferences

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        val enabled = prefs.getBoolean("sound_enabled", true)
        SoundManager.soundEnabled = enabled
        binding.switchSound.isChecked = enabled

        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            SoundManager.soundEnabled = isChecked
            prefs.edit().putBoolean("sound_enabled", isChecked).apply()
        }

        binding.switchProgression.isChecked = GamePreferences.isTrackProgression(this)
        binding.switchTimer.isChecked = GamePreferences.isTrackTimer(this)

        binding.switchProgression.setOnCheckedChangeListener { _, isChecked ->
            GamePreferences.setTrackProgression(this, isChecked)
        }

        binding.switchTimer.setOnCheckedChangeListener { _, isChecked ->
            GamePreferences.setTrackTimer(this, isChecked)
        }
    }
}

