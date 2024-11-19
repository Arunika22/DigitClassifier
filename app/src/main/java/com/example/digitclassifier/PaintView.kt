package com.example.digitclassifier


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PaintView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var path = Path()
    private val paint = Paint().apply {
        color = 0xFF000000.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> path.moveTo(event.x, event.y)
            MotionEvent.ACTION_MOVE -> path.lineTo(event.x, event.y)
            MotionEvent.ACTION_UP -> canvas.drawPath(path, paint)
        }
        invalidate()
        return true
    }

    fun clearCanvas() {
        path.reset()
        bitmap.eraseColor(0xFFFFFFFF.toInt())
        invalidate()
    }

    fun getBitmap(): Bitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, true)
}
