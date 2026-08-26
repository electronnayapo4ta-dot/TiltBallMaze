package com.densappstudio.tiltballmaze.game.engine

class GameLoop(
    private val onUpdate: (dt: Float) -> Unit,
    private val onRender: () -> Unit
) : Thread() {

    @Volatile
    private var running = false

    fun startLoop() {
        if (running) return
        running = true
        start()
    }

    fun stopLoop() {
        running = false
        joinSafely()
    }

    private fun joinSafely() {
        try {
            join()
        } catch (_: InterruptedException) {}
    }

    override fun run() {
        var lastTime = System.nanoTime()
        val targetDelta = 1_000_000_000f / 60f

        while (running) {
            val now = System.nanoTime()
            val delta = now - lastTime
            lastTime = now

            val dt = delta / 1_000_000_000f

            onUpdate(dt)
            onRender()

            val sleepTime = (targetDelta - (System.nanoTime() - now)) / 1_000_000
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime.toLong())
                } catch (_: InterruptedException) {}
            }
        }
    }
}
