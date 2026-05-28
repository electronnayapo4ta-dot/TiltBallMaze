package com.yourcompany.tiltballmaze.ui.util

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.yourcompany.tiltballmaze.R

fun View.enablePressAnimations(context: Context) {
    val press = AnimationUtils.loadAnimation(context, R.anim.button_press)
    val release = AnimationUtils.loadAnimation(context, R.anim.button_release)

    setOnTouchListener { v, event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> v.startAnimation(press)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.startAnimation(release)
        }
        false
    }
}

fun View.setOnClickListenerWithBounce(context: Context, listener: (View) -> Unit) {
    val bounce = AnimationUtils.loadAnimation(context, R.anim.button_bounce)
    setOnClickListener { v ->
        v.startAnimation(bounce)
        listener(v)
    }
}

