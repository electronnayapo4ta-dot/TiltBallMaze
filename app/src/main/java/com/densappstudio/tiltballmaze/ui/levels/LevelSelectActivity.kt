package com.densappstudio.tiltballmaze.ui.levels

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.densappstudio.tiltballmaze.R
import com.densappstudio.tiltballmaze.databinding.ActivityLevelSelectBinding
import com.densappstudio.tiltballmaze.game.data.GamePreferences
import com.densappstudio.tiltballmaze.game.ui.GameActivity
import com.densappstudio.tiltballmaze.ui.util.enablePressAnimations
import com.densappstudio.tiltballmaze.ui.util.setOnClickListenerWithBounce

class LevelSelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLevelSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLevelButtons()
    }

    private fun setupLevelButtons() {
        val buttons = listOf(
            binding.level1, binding.level2, binding.level3,
            binding.level4, binding.level5, binding.level6,
            binding.level7, binding.level8
        )

        buttons.forEachIndexed { index, button ->
            val level = index + 1
            button.text = getString(R.string.level_name, level)
            button.enablePressAnimations(this)
            button.setOnClickListenerWithBounce(this) { openLevel(level) }
        }
    }

    override fun onResume() {
        super.onResume()

        val track = GamePreferences.isTrackProgression(this)

        if (track) {
            binding.level2.isEnabled = GamePreferences.isLevelUnlocked(this, 2)
            binding.level3.isEnabled = GamePreferences.isLevelUnlocked(this, 3)
            binding.level4.isEnabled = GamePreferences.isLevelUnlocked(this, 4)
            binding.level5.isEnabled = GamePreferences.isLevelUnlocked(this, 5)
            binding.level6.isEnabled = GamePreferences.isLevelUnlocked(this, 6)
            binding.level7.isEnabled = GamePreferences.isLevelUnlocked(this, 7)
            binding.level8.isEnabled = GamePreferences.isLevelUnlocked(this, 8)
        } else {
            binding.level2.isEnabled = true
            binding.level3.isEnabled = true
            binding.level4.isEnabled = true
            binding.level5.isEnabled = true
            binding.level6.isEnabled = true
            binding.level7.isEnabled = true
            binding.level8.isEnabled = true
        }

        updateLevelIcons()
    }

    private fun updateLevelIcons() {
        updateButtonIcon(binding.level1, 1)
        updateButtonIcon(binding.level2, 2)
        updateButtonIcon(binding.level3, 3)
        updateButtonIcon(binding.level4, 4)
        updateButtonIcon(binding.level5, 5)
        updateButtonIcon(binding.level6, 6)
        updateButtonIcon(binding.level7, 7)
        updateButtonIcon(binding.level8, 8)
    }

    private fun updateButtonIcon(button: android.widget.Button, level: Int) {
        val unlocked = GamePreferences.isLevelUnlocked(this, level)
        val completed = GamePreferences.isLevelCompleted(this, level)
        val hasBestTime = GamePreferences.getBestTime(this, level) != Long.MAX_VALUE

        val leftIcon = when {
            !unlocked -> com.densappstudio.tiltballmaze.R.drawable.ic_lock
            completed -> com.densappstudio.tiltballmaze.R.drawable.ic_check
            else -> com.densappstudio.tiltballmaze.R.drawable.ic_unlock
        }
        val rightIcon = if (hasBestTime) com.densappstudio.tiltballmaze.R.drawable.ic_star else 0

        button.setCompoundDrawablesWithIntrinsicBounds(leftIcon, 0, rightIcon, 0)
    }

    private fun openLevel(level: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("level_id", level)
        startActivity(intent)
    }
}
