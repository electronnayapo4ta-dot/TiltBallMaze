package com.densappstudio.tiltballmaze.game.model

data class RotatingObstacle(
    val centerX: Float,
    val centerY: Float,
    val width: Float,
    val height: Float,
    var angle: Float = 0f,
    val rotationSpeed: Float // degrees per second
)
