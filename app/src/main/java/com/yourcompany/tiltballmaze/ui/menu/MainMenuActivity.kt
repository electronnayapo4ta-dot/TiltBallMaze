package com.yourcompany.tiltballmaze.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.yourcompany.tiltballmaze.R
import com.yourcompany.tiltballmaze.databinding.ActivityMainMenuBinding
import com.yourcompany.tiltballmaze.ui.levels.LevelSelectActivity
import com.yourcompany.tiltballmaze.ui.settings.SettingsActivity
import com.yourcompany.tiltballmaze.ui.util.enablePressAnimations
import com.yourcompany.tiltballmaze.ui.util.setOnClickListenerWithBounce

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_intro))

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
