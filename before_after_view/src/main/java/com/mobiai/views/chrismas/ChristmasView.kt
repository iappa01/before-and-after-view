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
import com.mobiai.views.utils.resizeWithLimitWidth
import com.mobiai.views.utils.toGrayscale

class ChristmasView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet,) {


    var obj : Bitmap
    var bg : Bitmap
    var objGray : Bitmap
    val paint = Paint()
    val paint2 = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    }

    init {
        obj = BitmapFactory.decodeResource(context.resources, R.drawable.obj ).resizeWithLimitWidth()
        objGray = obj.toGrayscale()

        bg  = BitmapFactory.decodeResource(context.resources, R.drawable.vietnam_flag )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // view wight / view height = bg.wight/ bg.height

        // height = width * bg,height / bg.width
        // width = height * bg,width / bg.height



        val bgRect = Rect(0, 0, bg.width, bg.height)
        val viewRect = Rect(
            0, 0, width, (width * bg.height) / bg.width
        )







        val objGrayRect = Rect(0, 0, objGray.width, objGray.height)
        var objGrayRectV = Rect(0, 0, width, (width * objGray.height) / objGray.width)

//        if (obj.height > obj.width) {
//            objGrayRectV = Rect(0, 0, height * bg.width/ bg.height, height).apply {
//                right = bg.width -
//            }
//        }

        canvas.drawBitmap(bg, null, viewRect, paint)

//        paint2.xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY);
        canvas.drawBitmap(objGray, null, objGrayRectV, paint2)

        paint2.alpha = 33;

        canvas.drawBitmap(bg, Rect(0, 0, bg.width, bg.height), viewRect, paint2);



    }



}