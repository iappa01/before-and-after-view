package com.mobiai.views.beforeafter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mobiai.views.beforeafter.BitMapConverter.loadBitmapFromView
import com.mobiai.views.utils.compressBitmap
import org.jcodec.api.android.AndroidSequenceEncoder
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class BeforeAfterRunner(val beforeAfter: BeforeAfter, val slideWidth: Int): DefaultLifecycleObserver {

    companion object {
        private val TAG = BeforeAfterRunner::class.java.simpleName
    }

    var isSlideRunning = false
    var detalX = -1


    private var isDestroy = false
    private var bitmaps = ArrayList<Frame>()
    private var bitmaps2 = arrayOf<Frame>()



    enum class DIRECTORY {
        RIGHT,
        LEFT
    }
    private var directory = DIRECTORY.RIGHT
    private val beforeAfterSlider =
        beforeAfter.findViewById<BeforeAfterSlider>(R.id.before_after_slider);

    var step = (60 * 0.61).toInt()
    var delay = 10L
    var limitWidth = 720

    data class Frame (val bitmap: Bitmap, val added: Boolean = false)

    val handler = Handler(Looper.getMainLooper())




    fun runSlide() {
        if (isSlideRunning) return
        isSlideRunning = true
        handler.postDelayed(runner, delay)
    }

    private var encodeThread : EncodeThread? = null
    private var isRunFullCycle = false
    private var record = false
    private var flipRight = 1
    private var flipLeft = 1
    private var onEncodedListener: OnEncodedListener? = null



    private fun modeRunStartRightToLeft() {
        flipRight = 1
        flipLeft = 1

        // beforeAfterSlider.translationX.toInt()  + slideWidth / 2  < step + 10
    }

    private fun isContinue() : Boolean {
        return !isDestroy && isSlideRunning
    }

    private fun isRecording() : Boolean {
        return record && !isRunFullCycle
    }

    private fun clearFrameQueue() {
        bitmaps.forEach {
            it.bitmap.recycle()
        }
        bitmaps.clear()
    }

    fun startSlideAndRecord(onEncodedListener: OnEncodedListener) {
        // setup slider before recording
        directory = DIRECTORY.LEFT
//        beforeAfterSlider.resetPosition(slideWidth / 2f)
        beforeAfterSlider.resetPosition(0F)
        record = true

        clearFrameQueue()

        // start run slide
        isSlideRunning = false
        isRunFullCycle = false
        flipRight = 1
        flipLeft = 1

        handler.removeCallbacksAndMessages(null)
        runSlide()

        this.onEncodedListener = onEncodedListener

        encode()
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
        isSlideRunning = false
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




            if (!isDestroy) {
                beforeAfterSlider.moveSlideAndBeforeAfterView(detalX.toFloat())

                if (isRecording()) {
                    val bitmap: Bitmap = loadBitmapFromView(beforeAfter)
                    bitmaps.add(Frame(bitmap))
                    Log.i(TAG, "bitmaps add : ${bitmaps.size}")
                }
            }

            // finish at left : beforeAfterSlider.translationX.toInt()  + slideWidth / 2  < step + 10
            // finish at center: beforeAfterSlider.translationX < 0

            if (!isRunFullCycle && flipLeft == 0 && flipRight == 0 && beforeAfterSlider.translationX <= 0) {
                isRunFullCycle = true
                encodeThread?.isRecordDone?.set(true)
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

        private var isEncoding = AtomicBoolean(false)
        var isRecordDone = AtomicBoolean(false)


        fun cancel() {
            isEncoding.set(false)
        }
        override fun run() {
            isEncoding.set(true)
            val startTime = System.currentTimeMillis()

            onEncodedListener?.onStart()
            while (isEncoding.get()) {

                if (bitmaps.size == 0 && isRecordDone.get()) break

                if (bitmaps.size == 0) continue
                if (bitmaps[0] == null) continue

                try {
                    val frame = bitmaps[0]
                    androidSequenceEncoder.encodeImage(frame.bitmap)
                    onEncodedListener?.onEncodedFrame(frame)

                    frame.bitmap.recycle()
                } catch (e: Exception) {

                }

                bitmaps.removeAt(0)

                Log.i(TAG2, "Encode Thread is running...bitmap remain ${bitmaps.size}")
            }

            androidSequenceEncoder.finish()
            onEncodedListener?.onCompleted(output.absolutePath)


            Log.i(TAG2, "Encode Thread stop: Size ${output.length()}, time: ${System.currentTimeMillis() - startTime}")
        }
    }

    private fun loadBitmapFromView(v: View): Bitmap {

        var width = v.width
        var height = v.height

//        val wishWight = 720
//
//        if (width > wishWight) {
//            width = wishWight
//            height = (wishWight * height / width)
//            if (height % 2 != 0) {
//                height += 1
//            }
//        }


        val b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)

//        return b

        val rs = compressBitmap(b, limitWidth)

        if (b != rs) {
            b.recycle()
            System.gc()
        }

        return rs
    }


}

