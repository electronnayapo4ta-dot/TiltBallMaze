package com.densappstudio.tiltballmaze.game.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class FireworksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private data class Particle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        var life: Float,
        val color: Int,
        val radius: Float
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val particles = ArrayList<Particle>(220)

    private var animator: ValueAnimator? = null
    private var lastNanos = 0L

    fun start() {
        if (animator?.isRunning == true) return
        spawnBurst()
        lastNanos = 0L

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 30_000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { tick() }
            start()
        }
    }

    fun stop() {
        animator?.cancel()
        animator = null
        particles.clear()
        invalidate()
    }

    private fun tick() {
        val now = System.nanoTime()
        val dt = if (lastNanos == 0L) 0.016f else ((now - lastNanos) / 1_000_000_000f).coerceIn(0f, 0.033f)
        lastNanos = now

        val gravity = 900f
        val drag = 0.985f

        var alive = 0
        for (i in particles.indices) {
            val p = particles[i]
            if (p.life <= 0f) continue

            p.vx *= drag
            p.vy = p.vy * drag + gravity * dt
            p.x += p.vx * dt
            p.y += p.vy * dt
            p.life -= dt
            if (p.life > 0f) alive++
        }

        if (alive < 70 && width > 0 && height > 0) {
            spawnBurst()
        }

        invalidate()
    }

    private fun spawnBurst() {
        if (width <= 0 || height <= 0) return

        val cx = Random.nextFloat() * width
        val cy = height * (0.15f + Random.nextFloat() * 0.35f)
        val baseSpeed = 520f + Random.nextFloat() * 260f
        val count = 40 + Random.nextInt(25)

        val colors = intArrayOf(
            Color.parseColor("#F2AA4C"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#FF4D6D"),
            Color.parseColor("#FFFFFF")
        )

        repeat(count) {
            val a = Random.nextFloat() * (Math.PI.toFloat() * 2f)
            val s = baseSpeed * (0.35f + Random.nextFloat() * 0.65f)
            val vx = cos(a) * s
            val vy = sin(a) * s - (220f + Random.nextFloat() * 120f)
            particles.add(
                Particle(
                    x = cx,
                    y = cy,
                    vx = vx,
                    vy = vy,
                    life = 1.2f + Random.nextFloat() * 0.9f,
                    color = colors[Random.nextInt(colors.size)],
                    radius = 3f + Random.nextFloat() * 3.5f
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (p in particles) {
            if (p.life <= 0f) continue
            val alpha = (p.life / 2.2f).coerceIn(0f, 1f)
            paint.color = p.color
            paint.alpha = (alpha * 255).toInt()
            canvas.drawCircle(p.x, p.y, p.radius, paint)
        }
    }
}

