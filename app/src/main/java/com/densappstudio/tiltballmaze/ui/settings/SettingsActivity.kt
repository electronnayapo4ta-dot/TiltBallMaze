package com.densappstudio.tiltballmaze.ui.settings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.densappstudio.tiltballmaze.R
import com.densappstudio.tiltballmaze.databinding.ActivitySettingsBinding
import com.densappstudio.tiltballmaze.game.audio.SoundManager
import com.densappstudio.tiltballmaze.game.data.GamePreferences
import com.densappstudio.tiltballmaze.ui.support.SupportActivity
import com.densappstudio.tiltballmaze.ui.util.LocaleHelper
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupVersionInfo()

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

        val currentLang = LocaleHelper.getLanguage(this)
        if (currentLang == "ru") {
            binding.langRu.isChecked = true
        } else {
            binding.langEn.isChecked = true
        }

        binding.radioLanguage.setOnCheckedChangeListener { _, checkedId ->
            val lang = if (checkedId == R.id.langRu) "ru" else "en"
            if (lang != currentLang) {
                prefs.edit().putString("language", lang).apply()
                LocaleHelper.setLocale(lang)
            }
        }

        binding.btnSupport.setOnClickListener {
            startActivity(Intent(this, SupportActivity::class.java))
        }

        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:AVG.LOGIN@yandex.ru")
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_feedback))
            }
            try {
                startActivity(intent)
            } catch (_: Exception) {}
        }
    }

    private fun setupVersionInfo() {
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0)
            }
            binding.tvVersion.text = getString(R.string.settings_version, packageInfo.versionName)
        } catch (e: Exception) {
            binding.tvVersion.text = "Version unknown"
        }
    }
}
