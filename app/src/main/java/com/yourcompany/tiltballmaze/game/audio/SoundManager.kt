package com.yourcompany.tiltballmaze.game.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.yourcompany.tiltballmaze.R   // ← ОБЯЗАТЕЛЬНО

object SoundManager {

    var soundEnabled = true

    private lateinit var soundPool: SoundPool

    private var collisionSound = 0
    private var winSound = 0
    private var explosionSound = 0

    fun init(context: Context) {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attrs)
            .build()

        collisionSound = soundPool.load(context, R.raw.collision, 1)
        winSound = soundPool.load(context, R.raw.win, 1)
        explosionSound = soundPool.load(context, R.raw.explosion, 1)
    }

    fun playCollision() {
        if (!soundEnabled) return
        soundPool.play(collisionSound, 1f, 1f, 1, 0, 1f)
    }

    fun playWin() {
        if (!soundEnabled) return
        soundPool.play(winSound, 1f, 1f, 1, 0, 1f)
    }

    fun playExplosion() {
        if (!soundEnabled) return
        soundPool.play(explosionSound, 1f, 1f, 1, 0, 1f)
    }
}
