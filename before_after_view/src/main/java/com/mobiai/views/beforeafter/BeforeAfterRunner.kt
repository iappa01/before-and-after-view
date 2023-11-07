package com.mobiai.views.beforeafter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class BeforeAfterRunner(val beforeAfter: BeforeAfter, val slideWidth: Int): DefaultLifecycleObserver {

    var isRunning = false
    var a = -1
    val delay = 1L
    private var isDestroy = false
    private var bitmaps = ArrayList<Bitmap>()

    enum class DIRECTORY {
        RIGHT,
        LEFT
    }
    private var directory = DIRECTORY.RIGHT // 1: RIGHT, -1: LEFT
    private val beforeAfterSlider =
        beforeAfter.findViewById<BeforeAfterSlider>(R.id.before_after_slider);

    var step = 40


    val handler = Handler(Looper.getMainLooper())

    fun start() {
        if (isRunning) return
        isRunning = true
        handler.postDelayed(runner, delay)
    }

    private fun isContinue() : Boolean {
        return !isDestroy && isRunning
    }

    override fun onDestroy(owner: LifecycleOwner) {
        isDestroy = true
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }


    private val runner = object: Runnable {
        override fun run() {
            if (directory == DIRECTORY.RIGHT && beforeAfterSlider.translationX >= slideWidth / 2) {
                directory = DIRECTORY.LEFT
            }

            if (directory == DIRECTORY.LEFT && beforeAfterSlider.translationX <= -1 * slideWidth / 2) {
                directory = DIRECTORY.RIGHT
            }

            if (directory == DIRECTORY.RIGHT) {
                a = step
            } else if (directory == DIRECTORY.LEFT) {
                a = step * -1
            }

            beforeAfterSlider.moveSlideAndBeforeAfterView(a.toFloat())
            val bitmap: Bitmap = loadBitmapFromView(beforeAfter)
            bitmaps.add(bitmap)

            if (isContinue())
                handler.postDelayed(this, delay)
        }
    }




    private fun loadBitmapFromView(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }


}