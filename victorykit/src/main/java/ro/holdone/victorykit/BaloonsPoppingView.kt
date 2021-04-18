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

class BaloonsPoppingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var minSize = 60.0
    var maxSize = 120.0

    val baloons = intArrayOf(
        R.drawable.ic_balloon_1,
        R.drawable.ic_balloon_2,
        R.drawable.ic_balloon_3,
        R.drawable.ic_balloon_4,
        R.drawable.ic_balloon_5,
        R.drawable.ic_balloon_6,
        R.drawable.ic_balloon_7
    )

    private var baloonsBag: MutableList<ImageView> = mutableListOf()

    init {
        // Load baloons

        repeat(2) { _ ->
            baloons.forEach {
                val imageView = ImageView(context)
                imageView.setImageResource(it)
                baloonsBag.add(imageView)
            }
        }

        baloonsBag.forEach {
            addView(it, generateParams())
        }

        post {
            startAnimation()
        }
    }

    private fun generateParams(): ViewGroup.LayoutParams {
        val size = Random.nextDouble(minSize, maxSize)
        val params = LayoutParams(size.toInt(), size.toInt())
        return params
    }

    private fun startAnimation() {
        baloonsBag.forEach {
            animateBalloon(it)
        }
    }

    private fun animateBalloon(view: View) {
        // Translate off-screen
        val translation = Random.nextInt(width).toFloat()
        view.translationX = translation
        view.translationY = height.toFloat()
        val jiggle = Random.nextInt(10)

        val animator = ValueAnimator.ofFloat(height.toFloat(), -view.height.toFloat())
        animator.addUpdateListener {
            view.translationY = it.animatedValue as Float
            view.translationX = translation + cos(it.animatedFraction * 30) * jiggle
        }
        animator.duration = Random.nextLong(2500, 4000)
        animator.startDelay = Random.nextLong(4000)
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }
}