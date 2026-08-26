package com.densappstudio.tiltballmaze.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.densappstudio.tiltballmaze.R
import com.densappstudio.tiltballmaze.databinding.ActivityMainMenuBinding
import com.densappstudio.tiltballmaze.ui.dev.DevSettingsActivity
import com.densappstudio.tiltballmaze.ui.levels.LevelSelectActivity
import com.densappstudio.tiltballmaze.ui.settings.SettingsActivity
import com.densappstudio.tiltballmaze.ui.util.enablePressAnimations
import com.densappstudio.tiltballmaze.ui.util.setOnClickListenerWithBounce

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private var secretTapCount = 0
    private var lastTapTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_intro))

        binding.logo.isClickable = true
        binding.logo.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - lastTapTime < 600) {
                secretTapCount++
            } else {
                secretTapCount = 1
            }
            lastTapTime = now
            if (secretTapCount >= 7) {
                startActivity(Intent(this, DevSettingsActivity::class.java))
                secretTapCount = 0
            }
        }

        binding.btnPlay.enablePressAnimations(this)
        binding.btnSettings.enablePressAnimations(this)
        binding.btnExit.enablePressAnimations(this)

        binding.btnPlay.setOnClickListenerWithBounce(this) {
            startActivity(Intent(this, LevelSelectActivity::class.java))
        }

        binding.btnSettings.setOnClickListenerWithBounce(this) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnExit.setOnClickListenerWithBounce(this) {
            finishAndRemoveTask()
            kotlin.system.exitProcess(0)
        }
    }
}
