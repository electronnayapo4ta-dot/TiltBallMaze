package com.densappstudio.tiltballmaze.ui.dev

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.densappstudio.tiltballmaze.databinding.ActivityDevSettingsBinding

class DevSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDevSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDevSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(PREFS_DEV, Context.MODE_PRIVATE)
        val speed = prefs.getFloat(KEY_LEVEL5_SPEED, DEFAULT_LEVEL5_SPEED)

        selectSpeed(speed)

        binding.radioSpeed.setOnCheckedChangeListener { _, checkedId ->
            val value = when (checkedId) {
                binding.speed02.id -> 0.2f
                binding.speed05.id -> 0.5f
                binding.speed15.id -> 1.5f
                else -> 1.0f
            }
            prefs.edit().putFloat(KEY_LEVEL5_SPEED, value).apply()
        }
    }

    private fun selectSpeed(speed: Float) {
        val buttonId = when (speed) {
            0.2f -> binding.speed02.id
            0.5f -> binding.speed05.id
            1.5f -> binding.speed15.id
            else -> binding.speed10.id
        }
        binding.radioSpeed.check(buttonId)
    }

    companion object {
        const val PREFS_DEV = "dev"
        const val KEY_LEVEL5_SPEED = "level5_speed"
        const val DEFAULT_LEVEL5_SPEED = 1.0f
        const val BASE_VERTICAL_SPEED = 250f

        fun level5SpeedMultiplier(context: Context): Float =
            context.getSharedPreferences(PREFS_DEV, Context.MODE_PRIVATE)
                .getFloat(KEY_LEVEL5_SPEED, DEFAULT_LEVEL5_SPEED)
    }
}
