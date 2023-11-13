package com.mobiai.views.utils

import android.graphics.Bitmap

fun compressBitmap(originalBitmap: Bitmap, limitWidth: Int = 720): Bitmap {

    val orgW = originalBitmap.width
    val orgH = originalBitmap.height

    val w = limitWidth

    if (w < orgW) {
        var h = (w * orgH / orgW)
        if (h % 2 != 0) {
            h += 1
        }
        return Bitmap.createScaledBitmap(originalBitmap, w, h, false)
    } else {
        val w = orgW
        var h = orgH
        if (h % 2 != 0) {
            h += 1
        }

        return Bitmap.createScaledBitmap(originalBitmap, w, h, false)
    }





}