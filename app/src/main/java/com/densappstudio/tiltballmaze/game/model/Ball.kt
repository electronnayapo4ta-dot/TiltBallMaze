package com.densappstudio.tiltballmaze.game.model

data class Ball(
    var x: Float,
    var y: Float,
    var radius: Float,
    var vx: Float = 0f,
    var vy: Float = 0f
)
