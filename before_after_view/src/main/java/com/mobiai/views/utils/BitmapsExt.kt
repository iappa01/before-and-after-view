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