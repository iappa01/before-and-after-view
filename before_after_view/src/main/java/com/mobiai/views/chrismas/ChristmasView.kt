package com.mobiai.views.chrismas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.scale
import com.mobiai.views.beforeafter.R
import com.mobiai.views.utils.toGrayscale

class ChristmasView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet,) {


    var obj : Bitmap
    var objRed : Bitmap
    var bg : Bitmap
    var objGray : Bitmap
    val paint = Paint()
    val paint2 = Paint()

    init {
        obj = BitmapFactory.decodeResource(context.resources, R.drawable.org )
        objRed = BitmapFactory.decodeResource(context.resources, R.drawable.red ).scale(obj.width, obj.height, false)




        objGray = obj.toGrayscale()

        bg  = BitmapFactory.decodeResource(context.resources, R.drawable.ba )
    }

    fun setObject() {
        val obj = BitmapFactory.decodeResource(context.resources, R.drawable.obj )
        val objGray = obj.toGrayscale()

        val bg = BitmapFactory.decodeResource(context.resources, R.drawable.f )



    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bgRect = Rect(0, 0, objRed.width, objRed.height)
        val viewRect1 = Rect(
            0, 0, width, (width * obj.height) / obj.width
        )

//        val viewRect2 = Rect(
//            0, 0, width, (width * objRed.height) / objRed.width
//        )

//        canvas.drawBitmap(obj, bgRect, viewRect1, null);
//
//
//        paint.alpha = (255 * 0.5).toInt()
//
//        canvas.drawBitmap(objRed, bgRect, viewRect1, paint)


        canvas.drawBitmap(mixBitmap(obj, objRed, 0), bgRect, viewRect1,null)

    }

    private fun mixBitmap(bitmap: Bitmap, bitmap2: Bitmap, percentInput : Int) : Bitmap{

        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)


        val percent = if (percentInput <= 0 ) 0 else if (percentInput >= 100) 100 else percentInput
        val canvas  = Canvas(bmp)

        canvas.drawBitmap(bitmap, 0f, 0f, null)

        val paint = Paint().apply {
            alpha = (255 * percent / 100).toInt()
        }
        canvas.drawBitmap(bitmap2, 0f, 0f, paint)


        return bmp
    }



}