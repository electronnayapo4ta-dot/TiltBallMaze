package com.densappstudio.tiltballmaze.ui.support

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.densappstudio.tiltballmaze.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Анимация появления
        binding.supportContainer.alpha = 0f
        binding.supportContainer.animate().alpha(1f).setDuration(500).start()

        // Кнопки поддержки (пока без IAP)
        binding.btnCoffee.setOnClickListener {
            openExternal("https://boosty.to/densappstudio")
        }

        binding.btnSupport.setOnClickListener {
            openExternal("https://boosty.to/densappstudio")
        }

        binding.btnSuperSupport.setOnClickListener {
            openExternal("https://boosty.to/densappstudio")
        }
    }

    private fun openExternal(url: String) {
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
        intent.data = android.net.Uri.parse(url)
        startActivity(intent)
    }
}
