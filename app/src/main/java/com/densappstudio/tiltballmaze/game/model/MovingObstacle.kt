package com.densappstudio.tiltballmaze.game.model

data class MovingObstacle(
    var left: Float,
    var top: Float,
    var right: Float,
    var bottom: Float,

    // параметры движения
    var minX: Float,
    var maxX: Float,
    var speed: Float,
    var direction: Int = 1 // 1 → вправо, -1 → влево
)
