package com.mobiai.views.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import java.util.Arrays


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

fun Bitmap.crop(): Bitmap {
    val height = height
    val width = width
    var empty = IntArray(width)
    var buffer = IntArray(width)
    Arrays.fill(empty, 0)
    var top = 0
    var left = 0
    var botton = height
    var right = width
    for (y in 0 until height) {
        getPixels(buffer, 0, width, 0, y, width, 1)
        if (!empty.contentEquals(buffer)) {
            top = y
            break
        }
    }
    for (y in height - 1 downTo top + 1) {
        getPixels(buffer, 0, width, 0, y, width, 1)
        if (!empty.contentEquals(buffer)) {
            botton = y
            break
        }
    }
    val bufferSize = botton - top + 1
    empty = IntArray(bufferSize)
    buffer = IntArray(bufferSize)
    Arrays.fill(empty, 0)
    for (x in 0 until width) {
        getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize)
        if (!Arrays.equals(empty, buffer)) {
            left = x
            break
        }
    }
    Arrays.fill(empty, 0)
    for (x in width - 1 downTo left + 1) {
        getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize)
        if (!Arrays.equals(empty, buffer)) {
            right = x
            break
        }
    }
    return Bitmap.createBitmap(this, left, top, right - left, botton - top)
}