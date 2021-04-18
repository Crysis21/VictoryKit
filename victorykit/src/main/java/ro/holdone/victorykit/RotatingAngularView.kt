package ro.holdone.victorykit

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class RotatingAngularView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var numberOfStripes = 24
    var drawRadius = 0.0F
    var animationDuration = 1000
    var autoplay: Boolean = true
    var animationOffsetAngle = 0.0f
    var drawColor = Color.CYAN
    var yOffset = 0.3F

    private var center = PointF()
    private val drawAngle: Double
        get() {
            return 2 * PI / numberOfStripes
        }

    private val stripePaint = Paint().apply {
        this.style = Paint.Style.FILL
        this.isAntiAlias = true
        this.color = Color.WHITE
    }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.RotatingAngularView, 0, 0)
        numberOfStripes = array.getInt(R.styleable.RotatingAngularView_stripesCount, 24)
        drawColor = array.getColor(R.styleable.RotatingAngularView_stripesColor, drawColor)
        animationDuration = array.getInt(R.styleable.RotatingAngularView_animationDuration, 1000)
        autoplay = array.getBoolean(R.styleable.RotatingAngularView_autoplay, true)
        array.recycle()

        stripePaint.color = drawColor

        if (autoplay) {
            post {
                startAnimation()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center.x = w / 2.0f
        center.y = yOffset * h
        drawRadius = center.distance(PointF(w.toFloat(), h.toFloat()))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        repeat(numberOfStripes) {
            drawTriangle(canvas, (it * drawAngle + animationOffsetAngle).toFloat())
        }
    }

    private fun drawTriangle(canvas: Canvas?, offset: Float) {
        val path = Path()
        val point1 = PointF(
            (drawRadius * cos(drawAngle / 2 + offset)).toFloat() + center.x,
            (drawRadius * sin(drawAngle / 2 + offset)).toFloat() + center.y
        )
        val point2 = PointF(
            (drawRadius * cos(drawAngle + offset)).toFloat() + center.x,
            (drawRadius * sin(drawAngle + offset)).toFloat() + center.y
        )
        path.moveTo(center.x, center.y)
        path.lineTo(point1.x, point1.y)
        path.lineTo(point2.x, point2.y)
        path.lineTo(center.x, center.y)
        path.close()
        canvas?.drawPath(path, stripePaint)
    }

    var valueAnimator: ValueAnimator? = null

    fun startAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator?.addUpdateListener {
            animationOffsetAngle = (it.animatedFraction * drawAngle).toFloat()
            invalidate()
        }
        valueAnimator?.duration = animationDuration.toLong()
        valueAnimator?.repeatMode = ValueAnimator.RESTART
        valueAnimator?.repeatCount = ValueAnimator.INFINITE
        valueAnimator?.interpolator = LinearInterpolator()

        valueAnimator?.start()
    }

    fun stopAnimation() {
        valueAnimator?.cancel()
        valueAnimator = null
    }
}