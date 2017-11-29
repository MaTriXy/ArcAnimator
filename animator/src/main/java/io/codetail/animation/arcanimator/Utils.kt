package io.codetail.animation.arcanimator

import android.view.View

internal object Utils {

    fun sin(degree: Double): Float {
        return Math.sin(Math.toRadians(degree)).toFloat()
    }

    fun cos(degree: Double): Float {
        return Math.cos(Math.toRadians(degree)).toFloat()
    }

    fun asin(value: Double): Float {
        return Math.toDegrees(Math.asin(value)).toFloat()
    }

    fun acos(value: Double): Float {
        return Math.toDegrees(Math.acos(value)).toFloat()
    }

    fun centerX(view: View): Float {
        return view.x + view.width / 2
    }

    fun centerY(view: View): Float {
        return view.y + view.height / 2
    }

}
