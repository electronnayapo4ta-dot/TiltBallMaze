package com.yourcompany.tiltballmaze.game.model

data class VerticalPairObstacle(
    var left1: Float,
    var top1: Float,
    var right1: Float,
    var bottom1: Float,

    var left2: Float,
    var top2: Float,
    var right2: Float,
    var bottom2: Float,

    var minX: Float,
    var maxX: Float,
    var speed: Float,
    var direction: Int = 1 // 1 → вправо, -1 → влево
)
