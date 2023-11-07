package com.mobiai.views.beforeafter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.jcodec.api.android.AndroidSequenceEncoder
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean


class BeforeAfterRunner(val beforeAfter: BeforeAfter, val slideWidth: Int): DefaultLifecycleObserver {

    companion object {
        private val TAG = BeforeAfterRunner::class.java.simpleName
    }

    var isRunning = false
    var detalX = -1

    var androidSequenceEncoder : AndroidSequenceEncoder? = null

    private var isDestroy = false
    private var bitmaps = ArrayList<Frame>()

    enum class DIRECTORY {
        RIGHT,
        LEFT
    }
    private var directory = DIRECTORY.RIGHT
    private val beforeAfterSlider =
        beforeAfter.findViewById<BeforeAfterSlider>(R.id.before_after_slider);

    var step = (60 * 0.61).toInt()
    var delay = 10L

    data class Frame (val bitmap: Bitmap, val added: Boolean = false)

    val handler = Handler(Looper.getMainLooper())




    fun start() {
        if (isRunning) return
        isRunning = true
        handler.postDelayed(runner, delay)
    }

    private var encodeThread : EncodeThread? = null
    private var isRunFullCycle = false
    private var record = false
    private var flipRight = 1
    private var flipLeft = 1
    private var onEncodedListener: OnEncodedListener? = null

    private fun isContinue() : Boolean {
        return !isDestroy && isRunning
    }

    private fun isRecording() : Boolean {
        return record && !isRunFullCycle
    }

    fun startSlideAndRecord(onEncodedListener: OnEncodedListener) {
        // restart
        handler.removeCallbacksAndMessages(null)

        bitmaps.forEach {
            it.bitmap.recycle()
        }
        bitmaps.clear()
        directory = DIRECTORY.LEFT
        beforeAfterSlider.resetPosition(slideWidth / 2f)
        record = true

        isRunning = false
        start()

        this.onEncodedListener = onEncodedListener
    }

    private fun encode() {
        if (encodeThread != null) {
            encodeThread?.cancel()
        }

        encodeThread = EncodeThread(beforeAfter.context, bitmaps)
        encodeThread?.onEncodedListener = onEncodedListener
        encodeThread?.start()
    }


    override fun onDestroy(owner: LifecycleOwner) {
        isDestroy = true
        isRunning = false
        handler.removeCallbacksAndMessages(null)

        encodeThread?.cancel()
    }




    private val runner = object: Runnable {
        override fun run() {
            Log.i(TAG, "Running...")
            if (directory == DIRECTORY.RIGHT && beforeAfterSlider.translationX >= slideWidth / 2) {
                directory = DIRECTORY.LEFT
                if (isRecording())
                    flipRight--
            }

            if (directory == DIRECTORY.LEFT && beforeAfterSlider.translationX <= -1 * slideWidth / 2) {
                directory = DIRECTORY.RIGHT
                if (isRecording())
                    flipLeft--
            }

            Log.i(TAG, "Running flip: right: $flipRight, left: $flipLeft, slider:${beforeAfterSlider.translationX}"  )

            if (directory == DIRECTORY.RIGHT) {
                detalX = step
            } else if (directory == DIRECTORY.LEFT) {
                detalX = step * -1
            }

            if (!isRunFullCycle && flipLeft == 0 && flipRight == 0 && beforeAfterSlider.translationX.toInt()  + slideWidth / 2  < step + 10) {
                isRunFullCycle = true
                encode()
            }


            if (!isDestroy) {
                beforeAfterSlider.moveSlideAndBeforeAfterView(detalX.toFloat())

                if (isRecording()) {
                    val bitmap: Bitmap = loadBitmapFromView(beforeAfter)
                    bitmaps.add(Frame(bitmap))
                    Log.i(TAG, "bitmaps add : ${bitmaps.size}")
                }
            }

            if (isContinue())
                handler.postDelayed(this, delay)
            else {
                Log.i(TAG, "Handler Stop")
            }
        }
    }

    interface OnEncodedListener {
        fun onStart()
        fun onCompleted(output: String)
        fun onEncodedFrame(frame: Frame)
    }

    class EncodeThread(
        private val context: Context,
        private var bitmaps: ArrayList<Frame>,
        )  : Thread() {

        var onEncodedListener: OnEncodedListener? = null
        companion object {
            val TAG2 = "EncodeThread"
        }

        private val output = File(context.filesDir, "before_after_slide.mp4")
        private val androidSequenceEncoder = AndroidSequenceEncoder.create30Fps(output)

        private var isRunning = AtomicBoolean(false)

        fun cancel() {
            isRunning.set(false)
        }
        override fun run() {
            isRunning.set(true)
            val startTime = System.currentTimeMillis()

            onEncodedListener?.onStart()
            while (isRunning.get()) {

                if (bitmaps.size == 0) break
                val frame = bitmaps[0]

                androidSequenceEncoder.encodeImage(frame.bitmap)
                onEncodedListener?.onEncodedFrame(frame)

                frame.bitmap.recycle()
                bitmaps.removeAt(0)

                Log.i(TAG2, "Encode Thread is running...bitmap remain ${bitmaps.size}")
            }
            Log.i(TAG2, "Encode Thread is stop ${System.currentTimeMillis() - startTime}")
            onEncodedListener?.onCompleted(output.absolutePath)
            androidSequenceEncoder.finish()
        }

    }

    private fun loadBitmapFromView(v: View): Bitmap {


//        var width = v.width / 2
//        if (width % 2 != 0) width++
//        var height = v.height / 2
//        if (height %2 != 0) height++

        var width = v.width
        var height = v.height


        val b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
//        v.layout(v.left, v.top, v.left + width, v.top + height)
        v.draw(c)

//        val re =  b.scale(v.width/2, v.height/2, false)
//
//        if (re != b) {
//            b.recycle()
//        }

        return b
    }


}