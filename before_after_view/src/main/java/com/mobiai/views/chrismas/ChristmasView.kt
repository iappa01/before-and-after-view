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
import com.mobiai.views.beforeafter.R
import com.mobiai.views.utils.toGrayscale

class ChristmasView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet,) {


    var obj : Bitmap
    var bg : Bitmap
    var objGray : Bitmap
    val paint = Paint()
    val paint2 = Paint()

    init {
        obj = BitmapFactory.decodeResource(context.resources, R.drawable.obj )
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

        // view wight / view height = bg.wight/ bg.height

        // height = width * bg,height / bg.width



        val bgRect = Rect(0, 0, bg.width, bg.height)
        val viewRect = Rect(
            0, 0, width, (width * bg.height) / bg.width
        )


        val objGrayRect = Rect(0, 0, objGray.width, objGray.height)



        canvas.drawBitmap(bg, bgRect, viewRect, paint)

//        paint2.xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY);
        canvas.drawBitmap(objGray, objGrayRect, viewRect, paint2)

        paint2.alpha = 33;
        paint2.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        canvas.drawBitmap(bg, Rect(0, 0, bg.width, bg.height), viewRect, paint2);

//        canvas.drawColor(Color.BLUE, PorterDuff.Mode.SRC_ATOP)


    }



}