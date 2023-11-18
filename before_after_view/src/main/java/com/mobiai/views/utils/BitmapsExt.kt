package com.mobiai.views.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint



fun Bitmap.toGrayscale(): Bitmap {
    // Create a grayscale color matrix
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)

    // Apply the color matrix to a paint object
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

    // Create a new bitmap and draw the original bitmap onto it using the paint object
    val grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(grayBitmap)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return grayBitmap
}


fun Bitmap.resizeWithLimitWidth(limitWidth: Int = 720): Bitmap {

    val orgW = width
    val orgH = height

    val w = limitWidth

    if (w < orgW) {
        var h = (w * orgH / orgW)
        if (h % 2 != 0) {
            h += 1
        }
        val b = Bitmap.createScaledBitmap(this, w, h, false)
        return b
    } else {
        val b = Bitmap.createScaledBitmap(this, orgW, orgH, false)
        return b
    }
}


fun Bitmap.mix(bitmap2: Bitmap, percentInput : Int) : Bitmap {

    val bmp = copy(Bitmap.Config.RGB_565, true)

    val percent = if (percentInput <= 0 ) 0 else if (percentInput >= 100) 100 else percentInput
    val canvas  = Canvas(bmp)

    canvas.drawBitmap(this, 0f, 0f, null)

    val paint = Paint().apply {
        alpha = (255 * percent / 100)
    }
    canvas.drawBitmap(bitmap2, 0f, 0f, paint)

    return bmp
}