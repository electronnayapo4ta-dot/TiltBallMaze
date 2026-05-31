package com.yourcompany.tiltballmaze.game.engine

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.yourcompany.tiltballmaze.game.audio.SoundManager
import com.yourcompany.tiltballmaze.game.data.GamePreferences
import com.yourcompany.tiltballmaze.game.model.Ball
import com.yourcompany.tiltballmaze.game.model.Hole
import com.yourcompany.tiltballmaze.game.model.MovingObstacle
import com.yourcompany.tiltballmaze.game.model.Obstacle
import com.yourcompany.tiltballmaze.game.model.RotatingObstacle
import com.yourcompany.tiltballmaze.game.model.VerticalPairObstacle
import com.yourcompany.tiltballmaze.ui.dev.DevSettingsActivity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GameSurface @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private var gameLoop: GameLoop? = null
    private var levelId: Int = 1
    var onWinListener: (() -> Unit)? = null

    @Volatile
    private var pendingRestart = false

    private val ballPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val bgPaint = Paint().apply {
        color = Color.BLACK
    }

    private val obstacles = mutableListOf<Obstacle>()
    private val obstaclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL
    }

    private val movingObstacles = mutableListOf<MovingObstacle>()
    private val movingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.MAGENTA
        style = Paint.Style.FILL
    }

    private val verticalPairs = mutableListOf<VerticalPairObstacle>()
    private val deadlyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val rotatingObstacles = mutableListOf<RotatingObstacle>()
    private val rotatingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
        style = Paint.Style.FILL
    }

    private lateinit var hole: Hole
    private val holePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    private val winTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        textSize = 120f
        textAlign = Paint.Align.CENTER
        setShadowLayer(10f, 0f, 0f, Color.BLACK)
    }

    private val explosionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    private var ball = Ball(0f, 0f, 40f)
    private var ax = 0f
    private var ay = 0f

    private var widthF = 0f
    private var heightF = 0f
    private var isWin = false
    private var hasPlayedWinSound = false
    private var isExploding = false
    private var explosionTimer = 0f
    private var levelStartTime = 0L
    private var levelEndTime = 0L

    private val timerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 48f
        setShadowLayer(6f, 0f, 0f, Color.BLACK)
    }

    init {
        holder.addCallback(this)
    }

    fun setLevel(levelId: Int) {
        this.levelId = levelId
        if (widthF > 0f && heightF > 0f) {
            resetLevel()
        }
    }

    fun setTilt(ax: Float, ay: Float) {
        this.ax = ax
        this.ay = ay
    }

    fun restartLevel() {
        pendingRestart = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        widthF = width.toFloat()
        heightF = height.toFloat()
        resetLevel()

        gameLoop = GameLoop(
            onUpdate = { dt -> update(dt) },
            onRender = { drawFrame() }
        ).also { it.startLoop() }
    }

    private fun resetLevel() {
        isWin = false
        hasPlayedWinSound = false
        isExploding = false
        explosionTimer = 0f
        ball.vx = 0f
        ball.vy = 0f

        obstacles.clear()
        movingObstacles.clear()
        rotatingObstacles.clear()
        verticalPairs.clear()

        when (levelId) {
            1 -> loadLevel1()
            2 -> loadLevel2()
            3 -> loadLevel3()
            4 -> loadLevel4()
            5 -> loadLevel5()
            else -> loadLevel1()
        }

        levelStartTime = System.currentTimeMillis()
    }

    private fun loadLevel1() {
        obstacles += Obstacle(widthF * 0.2f, heightF * 0.4f, widthF * 0.8f, heightF * 0.45f)
        hole = Hole(widthF * 0.85f, heightF * 0.15f, 50f)
        ball.x = widthF * 0.1f
        ball.y = heightF * 0.85f
    }

    private fun loadLevel2() {
        val shutterHeight = heightF * 0.05f
        val topY = heightF * 0.3f
        val bottomY = heightF * 0.6f
        
        val narrowWidth = widthF * 0.5f
        val wideWidth = widthF * 0.7f

        movingObstacles += MovingObstacle(
            left = 0f,
            top = topY,
            right = narrowWidth,
            bottom = topY + shutterHeight,
            minX = 0f,
            maxX = widthF,
            speed = 850f,
            direction = 1
        )

        movingObstacles += MovingObstacle(
            left = widthF - wideWidth,
            top = bottomY,
            right = widthF,
            bottom = bottomY + shutterHeight,
            minX = 0f,
            maxX = widthF,
            speed = 450f,
            direction = -1
        )

        ball.x = widthF * 0.1f
        ball.y = heightF * 0.1f
        
        hole = Hole(
            x = widthF * 0.9f,
            y = heightF * 0.9f,
            radius = 50f
        )
    }

    private fun loadLevel3() {
        rotatingObstacles += RotatingObstacle(
            centerX = widthF / 2f,
            centerY = heightF / 2f,
            width = widthF,
            height = 40f,
            rotationSpeed = 90f
        )
        ball.x = widthF * 0.5f
        ball.y = heightF * 0.85f
        hole = Hole(x = widthF * 0.5f, y = heightF * 0.15f, radius = 50f)
    }

    private fun loadLevel4() {
        val gap = ball.radius * 3f
        val shutterHeight = heightF * 0.05f
        val shutterWidth = widthF * 0.75f
        val topY = heightF * 0.4f
        val bottomY = topY + shutterHeight + gap
        val minX = 0f
        val maxX = widthF

        movingObstacles += MovingObstacle(
            left = 0f,
            top = topY,
            right = shutterWidth,
            bottom = topY + shutterHeight,
            minX = minX,
            maxX = maxX,
            speed = 400f,
            direction = 1
        )

        movingObstacles += MovingObstacle(
            left = maxX - shutterWidth,
            top = bottomY,
            right = maxX,
            bottom = bottomY + shutterHeight,
            minX = minX,
            maxX = maxX,
            speed = 400f,
            direction = -1
        )

        ball.x = widthF * 0.5f
        ball.y = heightF * 0.85f
        hole = Hole(x = widthF * 0.5f, y = heightF * 0.1f, radius = 50f)
    }

    private fun loadLevel5() {
        val d = ball.radius * 2f
        val gapBetweenHoriz = d * 3.5f
        val gapBetweenVerticals = d * 5.0f // 1.5x wider than 3.2f

        val shutterHeight = heightF * 0.05f
        val centerY = heightF * 0.5f
        val topShutterBottom = centerY - gapBetweenHoriz / 2f
        val bottomShutterTop = centerY + gapBetweenHoriz / 2f

        obstacles += Obstacle(0f, topShutterBottom - shutterHeight, widthF * 0.7f, topShutterBottom)
        obstacles += Obstacle(widthF * 0.3f, bottomShutterTop, widthF, bottomShutterTop + shutterHeight)

        val pairCenterX = widthF * 0.6f
        val halfGap = gapBetweenVerticals / 2f
        val verticalWidth = d * 0.9f

        verticalPairs += VerticalPairObstacle(
            left1 = pairCenterX - halfGap - verticalWidth,
            top1 = topShutterBottom,
            right1 = pairCenterX - halfGap,
            bottom1 = bottomShutterTop,
            left2 = pairCenterX + halfGap,
            top2 = topShutterBottom,
            right2 = pairCenterX + halfGap + verticalWidth,
            bottom2 = bottomShutterTop,
            minX = 0f,
            maxX = widthF,
            speed = DevSettingsActivity.BASE_VERTICAL_SPEED * DevSettingsActivity.level5SpeedMultiplier(context),
            direction = -1
        )

        ball.x = widthF * 0.2f
        ball.y = heightF * 0.85f
        hole = Hole(widthF * 0.8f, heightF * 0.15f, 50f)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        widthF = width.toFloat()
        heightF = height.toFloat()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop?.stopLoop()
    }

    private fun update(dt: Float) {
        if (pendingRestart) {
            pendingRestart = false
            resetLevel()
            return
        }

        if (isWin) return

        if (isExploding) {
            explosionTimer += dt
            if (explosionTimer > 1.0f) resetLevel()
            return
        }

        val accel = 1500f
        ball.vx += ax * accel * dt
        ball.vy += ay * accel * dt

        val friction = 0.98f
        ball.vx *= friction
        ball.vy *= friction

        ball.x += ball.vx * dt
        ball.y += ball.vy * dt

        checkBoundaries()
        handleObstacleCollisions()
        updateMovingObstacles(dt)
        handleMovingObstacleCollisions()
        updateRotatingObstacles(dt)
        handleRotatingObstacleCollisions()
        updateVerticalPairs(dt)
        handleVerticalPairCollisions()

        if (checkWin()) {
            isWin = true
            onWin()
        }
    }

    private fun checkBoundaries() {
        if (ball.x - ball.radius < 0) {
            ball.x = ball.radius
            ball.vx = -ball.vx * 0.5f
        } else if (ball.x + ball.radius > widthF) {
            ball.x = widthF - ball.radius
            ball.vx = -ball.vx * 0.5f
        }
        if (ball.y - ball.radius < 0) {
            ball.y = ball.radius
            ball.vy = -ball.vy * 0.5f
        } else if (ball.y + ball.radius > heightF) {
            ball.y = heightF - ball.radius
            ball.vy = -ball.vy * 0.5f
        }
    }

    private fun updateMovingObstacles(dt: Float) {
        for (m in movingObstacles) {
            val width = m.right - m.left
            val dx = m.speed * dt * m.direction
            m.left += dx
            m.right += dx

            if (m.left < m.minX) {
                val overshoot = m.minX - m.left
                m.left = m.minX + overshoot
                m.right = m.left + width
                m.direction = 1
            } else if (m.right > m.maxX) {
                val overshoot = m.right - m.maxX
                m.right = m.maxX - overshoot
                m.left = m.right - width
                m.direction = -1
            }
        }
    }

    private fun handleMovingObstacleCollisions() {
        for (m in movingObstacles) {
            val closestX = ball.x.coerceIn(m.left, m.right)
            val closestY = ball.y.coerceIn(m.top, m.bottom)
            val dx = ball.x - closestX
            val dy = ball.y - closestY
            val dist2 = dx * dx + dy * dy

            if (dist2 < ball.radius * ball.radius) {
                val dist = sqrt(dist2.toDouble()).toFloat().coerceAtLeast(0.001f)
                val overlap = ball.radius - dist
                val nx = dx / dist
                val ny = dy / dist

                ball.x += nx * overlap
                ball.y += ny * overlap

                if (isPinned()) {
                    triggerExplosion()
                    return
                }

                val obsVx = m.speed * m.direction
                val relVx = ball.vx - obsVx
                val relVy = ball.vy
                val vnRel = relVx * nx + relVy * ny

                if (vnRel < 0f) {
                    val bounce = 0.5f
                    val impulse = -(1f + bounce) * vnRel
                    ball.vx += impulse * nx
                    ball.vy += impulse * ny
                    SoundManager.playCollision()
                }
            }
        }
    }

    private fun updateRotatingObstacles(dt: Float) {
        for (r in rotatingObstacles) {
            r.angle += r.rotationSpeed * dt
            if (r.angle >= 360f) r.angle -= 360f
        }
    }

    private fun handleRotatingObstacleCollisions() {
        for (r in rotatingObstacles) {
            val rad = Math.toRadians(r.angle.toDouble())
            val cosA = cos(rad).toFloat()
            val sinA = sin(rad).toFloat()

            val relX = ball.x - r.centerX
            val relY = ball.y - r.centerY
            val localX = relX * cosA + relY * sinA
            val localY = -relX * sinA + relY * cosA

            val halfW = r.width / 2f
            val halfH = r.height / 2f
            val closestX = localX.coerceIn(-halfW, halfW)
            val closestY = localY.coerceIn(-halfH, halfH)

            val ldx = localX - closestX
            val ldy = localY - closestY
            val dist2 = ldx * ldx + ldy * ldy

            if (dist2 < ball.radius * ball.radius) {
                val dist = sqrt(dist2.toDouble()).toFloat().coerceAtLeast(0.001f)
                val overlap = ball.radius - dist
                val nxLocal = ldx / dist
                val nyLocal = ldy / dist

                val nx = nxLocal * cosA - nyLocal * sinA
                val ny = nxLocal * sinA + nyLocal * cosA

                ball.x += nx * overlap
                ball.y += ny * overlap

                if (isPinned()) {
                    triggerExplosion()
                    return
                }

                // Obstacle velocity at collision point (simplified)
                val angVelRad = Math.toRadians(r.rotationSpeed.toDouble()).toFloat()
                val obsVx = -angVelRad * relY
                val obsVy = angVelRad * relX
                
                val relVx = ball.vx - obsVx
                val relVy = ball.vy - obsVy
                val vnRel = relVx * nx + relVy * ny

                if (vnRel < 0f) {
                    val bounce = 0.5f
                    val impulse = -(1f + bounce) * vnRel
                    ball.vx += impulse * nx
                    ball.vy += impulse * ny
                    SoundManager.playCollision()
                }
            }
        }
    }

    private fun updateVerticalPairs(dt: Float) {
        for (p in verticalPairs) {
            val dx = p.speed * dt * p.direction
            p.left1 += dx
            p.right1 += dx
            p.left2 += dx
            p.right2 += dx

            if (p.left1 < p.minX) {
                val overshoot = p.minX - p.left1
                val shift = 2f * overshoot
                p.left1 += shift; p.right1 += shift; p.left2 += shift; p.right2 += shift
                p.direction = 1
            } else if (p.right2 > p.maxX) {
                val overshoot = p.right2 - p.maxX
                val shift = -2f * overshoot
                p.left1 += shift; p.right1 += shift; p.left2 += shift; p.right2 += shift
                p.direction = -1
            }
        }
    }

    private fun handleVerticalPairCollisions() {
        for (p in verticalPairs) {
            val hit = rectCircleOverlap(ball.x, ball.y, ball.radius, p.left1, p.top1, p.right1, p.bottom1) ||
                      rectCircleOverlap(ball.x, ball.y, ball.radius, p.left2, p.top2, p.right2, p.bottom2)
            if (hit) {
                SoundManager.playExplosion()
                restartLevel()
                return
            }
        }
    }

    private fun rectCircleOverlap(cx: Float, cy: Float, r: Float, l: Float, t: Float, ri: Float, b: Float): Boolean {
        val closestX = cx.coerceIn(l, ri)
        val closestY = cy.coerceIn(t, b)
        val dx = cx - closestX
        val dy = cy - closestY
        return dx * dx + dy * dy < r * r
    }

    private fun isPinned(): Boolean {
        if (ball.x - ball.radius < -1f || ball.x + ball.radius > widthF + 1f ||
            ball.y - ball.radius < -1f || ball.y + ball.radius > heightF + 1f) return true

        for (o in obstacles) {
            if (rectCircleOverlap(ball.x, ball.y, ball.radius * 0.9f, o.left, o.top, o.right, o.bottom)) return true
        }
        return false
    }

    private fun triggerExplosion() {
        isExploding = true
        explosionTimer = 0f
        SoundManager.playExplosion()
    }

    private fun checkWin(): Boolean {
        val dx = ball.x - hole.x
        val dy = ball.y - hole.y
        return (dx * dx + dy * dy) < (hole.radius - ball.radius).let { it * it }
    }

    private fun onWin() {
        ball.vx = 0f
        ball.vy = 0f
        levelEndTime = System.currentTimeMillis()
        GamePreferences.setLevelCompleted(context, levelId)
        if (GamePreferences.isTrackTimer(context)) {
            GamePreferences.saveBestTime(context, levelId, levelEndTime - levelStartTime)
        }
        if (GamePreferences.isTrackProgression(context)) {
            GamePreferences.setLevelUnlocked(context, levelId + 1)
        }
        if (!hasPlayedWinSound) {
            hasPlayedWinSound = true
            SoundManager.playWin()
        }
        onWinListener?.invoke()
    }

    private fun handleObstacleCollisions() {
        for (o in obstacles) {
            val closestX = ball.x.coerceIn(o.left, o.right)
            val closestY = ball.y.coerceIn(o.top, o.bottom)
            val dx = ball.x - closestX
            val dy = ball.y - closestY
            val dist2 = dx * dx + dy * dy

            if (dist2 < ball.radius * ball.radius) {
                val dist = sqrt(dist2.toDouble()).toFloat().coerceAtLeast(0.001f)
                val overlap = ball.radius - dist
                val nx = dx / dist
                val ny = dy / dist

                ball.x += nx * overlap
                ball.y += ny * overlap

                val vn = ball.vx * nx + ball.vy * ny
                if (vn < 0f) {
                    val bounce = 0.5f
                    ball.vx -= (1f + bounce) * vn * nx
                    ball.vy -= (1f + bounce) * vn * ny
                    SoundManager.playCollision()
                }
            }
        }
    }

    private fun drawFrame() {
        val canvas: Canvas = holder.lockCanvas() ?: return
        try {
            canvas.drawRect(0f, 0f, widthF, heightF, bgPaint)
            canvas.drawCircle(hole.x, hole.y, hole.radius, holePaint)
            for (o in obstacles) canvas.drawRect(o.left, o.top, o.right, o.bottom, obstaclePaint)
            for (m in movingObstacles) canvas.drawRect(m.left, m.top, m.right, m.bottom, movingPaint)
            for (p in verticalPairs) {
                canvas.drawRect(p.left1, p.top1, p.right1, p.bottom1, deadlyPaint)
                canvas.drawRect(p.left2, p.top2, p.right2, p.bottom2, deadlyPaint)
            }
            for (r in rotatingObstacles) {
                canvas.save()
                canvas.rotate(r.angle, r.centerX, r.centerY)
                canvas.drawRect(r.centerX - r.width / 2f, r.centerY - r.height / 2f, r.centerX + r.width / 2f, r.centerY + r.height / 2f, rotatingPaint)
                canvas.restore()
            }
            if (isExploding) {
                explosionPaint.color = Color.rgb(255, (255 * (1 - explosionTimer)).toInt(), 0)
                canvas.drawCircle(ball.x, ball.y, ball.radius * (1 + explosionTimer * 3), explosionPaint)
            } else {
                canvas.drawCircle(ball.x, ball.y, ball.radius, ballPaint)
            }
            if (isWin) canvas.drawText("YOU WIN!", widthF / 2f, heightF / 2f, winTextPaint)
            if (!isWin && GamePreferences.isTrackTimer(context)) {
                val elapsed = System.currentTimeMillis() - levelStartTime
                canvas.drawText("${elapsed / 1000f}s", 50f, 100f, timerPaint)
            }
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }
}
