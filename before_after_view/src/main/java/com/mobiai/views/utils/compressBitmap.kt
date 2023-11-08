package com.mobiai.views.utils

import android.graphics.Bitmap

fun compressBitmap(originalBitmap: Bitmap): Bitmap {

    val orgW = originalBitmap.width
    val orgH = originalBitmap.height

    val w = 600

    if (w < orgW) {
        var h = (w * orgH / orgW)
        if (h % 2 != 0) {
            h += 1
        }
        val b = Bitmap.createScaledBitmap(originalBitmap, w, h, false)
        return b
    } else {
        val b = Bitmap.createScaledBitmap(originalBitmap, orgW, orgH, false)
        return b
    }





}