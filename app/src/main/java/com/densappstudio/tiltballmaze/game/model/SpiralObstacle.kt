package com.densappstudio.tiltballmaze.game.model

data class SpiralObstacle(
    val centerX: Float,
    val centerY: Float,
    val startRadius: Float,
    val endRadius: Float,
    val numTurns: Float,
    var rotationAngle: Float = 0f,
    val rotationSpeed: Float, // degrees per second
    val thickness: Float
)
