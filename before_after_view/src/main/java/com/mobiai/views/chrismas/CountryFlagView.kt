package com.mobiai.views.chrismas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.mobiai.views.beforeafter.R
import com.mobiai.views.utils.decodeSampledBitmapFromPath
import com.mobiai.views.utils.decodeSampledBitmapFromResource
import com.mobiai.views.utils.toGrayscale

class CountryFlagView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet,) {


//    var obj : Bitmap
    private var bg : Bitmap? = null
    private var objGray : Bitmap? = null
    private val paint = Paint()
    private val paint2 = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    }

    private var isActionOneTime = true

    private var objGrayRectV: Rect = Rect()
    private var viewRect = Rect()

    public fun updateObject(objPath: String) {
        objGray = decodeSampledBitmapFromPath(objPath, 300, 300).toGrayscale()
    }

    public fun updateObject(objPath: Int) {
        objGray = decodeSampledBitmapFromResource(resources, objPath, 300, 300).toGrayscale()
    }

    fun updateBackground(bgRes: Int) {
        bg = decodeSampledBitmapFromResource(resources, bgRes, 500, 500)
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // view wight / view height = bg.wight/ bg.height

        // height = width * bg,height / bg.width
        // width = height * bg,width / bg.height

        if (bg == null || objGray == null) return

        if (isActionOneTime) {
            viewRect = Rect(
                0, 0, width, (width * bg!!.height) / bg!!.width
            )
            objGrayRectV = createCenterRect(viewRect, objGray!!)


            isActionOneTime = false
        }

        canvas.drawBitmap(bg!!, null, viewRect, paint)
        canvas.drawBitmap(objGray!!, null, objGrayRectV, paint)
        paint2.alpha = 33;
        canvas.drawBitmap(bg!!, Rect(0, 0, bg!!.width, bg!!.height), viewRect, paint2)



    }

    private fun createCenterRect(viewRect: Rect, objBmp: Bitmap) : Rect{
        val a = 8
        val wObj = viewRect.width() -  (2 * viewRect.width() / a)
        val hObj = wObj * objBmp.height / objBmp.width

        val left = (viewRect.width() - wObj)/2
        val top = (viewRect.height() - hObj)/2

        return Rect(left, top,
            left + wObj, top + hObj)
    }



}