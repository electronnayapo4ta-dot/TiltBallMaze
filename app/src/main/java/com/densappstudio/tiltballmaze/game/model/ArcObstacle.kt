package com.densappstudio.tiltballmaze.game.model

data class ArcObstacle(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
    val startAngle: Float, // Degrees
    val sweepAngle: Float, // Degrees
    val thickness: Float
)
