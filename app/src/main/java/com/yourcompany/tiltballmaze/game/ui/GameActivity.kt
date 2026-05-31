package com.yourcompany.tiltballmaze.game.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.yourcompany.tiltballmaze.R
import com.yourcompany.tiltballmaze.game.engine.GameSurface
import com.yourcompany.tiltballmaze.game.sensors.TiltSensor
import com.yourcompany.tiltballmaze.game.audio.SoundManager
import com.yourcompany.tiltballmaze.ui.util.enablePressAnimations
import com.yourcompany.tiltballmaze.ui.util.setOnClickListenerWithBounce

class GameActivity : AppCompatActivity() {

    private lateinit var gameSurface: GameSurface
    private lateinit var tiltSensor: TiltSensor
    private var levelId: Int = 1

    private var winShown = false
    private var winOverlay: View? = null
    private var fireworks: FireworksView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // ←←← ВОТ ТУТ ПРАВИЛЬНО
        SoundManager.init(this)

        levelId = intent.getIntExtra("level_id", 1)

        gameSurface = findViewById(R.id.gameSurface)
        gameSurface.setLevel(levelId)
        gameSurface.onWinListener = {
            runOnUiThread { showWinOverlay() }
        }

        winOverlay = findViewById(R.id.winOverlay)
        fireworks = findViewById(R.id.fireworks)

        val btnRestart = findViewById<View>(R.id.btnRestart)
        val btnNext = findViewById<View>(R.id.btnNextLevel)

        btnRestart.enablePressAnimations(this)
        btnNext.enablePressAnimations(this)

        btnRestart.setOnClickListenerWithBounce(this) {
            hideWinOverlay()
            gameSurface.restartLevel()
        }

        btnNext.setOnClickListenerWithBounce(this) {
            val next = levelId + 1
            if (next <= MAX_LEVEL) {
                hideWinOverlay()
                startActivity(Intent(this, GameActivity::class.java).putExtra("level_id", next))
                finish()
            } else {
                finish()
            }
        }

        tiltSensor = TiltSensor(this) { ax, ay ->
            gameSurface.setTilt(ax, ay)
        }
    }

    override fun onResume() {
        super.onResume()
        tiltSensor.start()
    }

    override fun onPause() {
        super.onPause()
        tiltSensor.stop()
    }

    private fun showWinOverlay() {
        if (winShown) return
        winShown = true

        winOverlay?.let { overlay ->
            overlay.visibility = View.VISIBLE
            overlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.win_popup))
        }
        fireworks?.start()
    }

    private fun hideWinOverlay() {
        winShown = false
        fireworks?.stop()
        winOverlay?.visibility = View.GONE
    }

    companion object {
        private const val MAX_LEVEL = 5
    }
}
