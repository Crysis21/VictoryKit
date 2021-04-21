package ro.holdone.victorykit

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.math.cos
import kotlin.random.Random

class BalloonsPoppingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var minSize = 60
    var maxSize = 120
    var autoplay = false
    var loopPlayback = true
    var maxJiggle = 10

    val baloons = intArrayOf(
        R.drawable.ic_balloon_1,
        R.drawable.ic_balloon_2,
        R.drawable.ic_balloon_3,
        R.drawable.ic_balloon_4,
        R.drawable.ic_balloon_5,
        R.drawable.ic_balloon_6,
        R.drawable.ic_balloon_7
    )

    private var balloonsBag: MutableList<ImageView> = mutableListOf()

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.BalloonsPoppingView, 0, 0)
        minSize =
            array.getDimensionPixelSize(R.styleable.BalloonsPoppingView_minBalloonSize, minSize)
        maxSize =
            array.getDimensionPixelSize(R.styleable.BalloonsPoppingView_maxBalloonSize, maxSize)

        autoplay = array.getBoolean(R.styleable.BalloonsPoppingView_minBalloonSize, true)
        loopPlayback = array.getBoolean(R.styleable.BalloonsPoppingView_loopPlayback, true)
        array.recycle()

        if (autoplay) {
            post {
                startAnimation()
            }
        }

        // Load baloons
        repeat(2) { _ ->
            baloons.forEach {
                val imageView = ImageView(context)
                imageView.setImageResource(it)
                balloonsBag.add(imageView)
            }
        }

        balloonsBag.forEach {
            addView(it, generateParams())
        }

        post {
            startAnimation()
        }
    }

    private fun generateParams(): ViewGroup.LayoutParams {
        val size = Random.nextInt(minSize, maxSize)
        return LayoutParams(size, size)
    }

    private fun startAnimation() {
        balloonsBag.forEach {
            animateBalloon(it)
        }
    }

    private fun animateBalloon(view: View) {
        // Translate off-screen
        val translation = Random.nextInt(width).toFloat()
        view.translationX = translation
        view.translationY = height.toFloat()
        val jiggle = Random.nextInt(maxJiggle)

        val animator = ValueAnimator.ofFloat(height.toFloat(), -view.height.toFloat())
        animator.addUpdateListener {
            view.translationY = it.animatedValue as Float
            view.translationX = translation + cos(it.animatedFraction * 30) * jiggle
        }
        animator.duration = Random.nextLong(2500, 4000)
        animator.startDelay = Random.nextLong(4000)
        animator.repeatMode = ValueAnimator.RESTART
        if (loopPlayback) {
            animator.repeatCount = ValueAnimator.INFINITE
        }
        animator.start()
    }
}