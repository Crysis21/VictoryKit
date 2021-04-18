package ro.holdone.victorykit

import android.graphics.PointF
import kotlin.math.pow
import kotlin.math.sqrt

fun PointF.distance(point: PointF): Float {
    return sqrt((this.x - point.x).toDouble().pow(2.0) + (this.y - point.y).toDouble().pow(2.0)).toFloat()
}