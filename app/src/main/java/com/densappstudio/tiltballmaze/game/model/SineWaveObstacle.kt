package com.densappstudio.tiltballmaze.game.model

data class SineWaveObstacle(
    val centerX: Float,
    val startY: Float,
    val endY: Float,
    val amplitude: Float,
    val cycles: Float,
    val gap: Float,
    val thickness: Float
)
