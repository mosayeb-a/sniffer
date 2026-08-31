package com.ma.sniffer.presentation.common

import android.graphics.*
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.IconCompat

object BitmapGenerator {
    private const val ICON_SIZE = 96

    private val speedPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        textSize = 65f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
    }

    private val unitPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        textSize = 40f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    fun createSpeedIcon(value: String, unit: String): IconCompat {
        val bitmap = createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawText(value, ICON_SIZE / 2f, 52f, speedPaint)
        canvas.drawText(unit, ICON_SIZE / 2f, 95f, unitPaint)

        return IconCompat.createWithBitmap(bitmap)
    }
}