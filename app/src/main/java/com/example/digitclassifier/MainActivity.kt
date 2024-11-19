package com.example.digitclassifier

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var paintView: PaintView
    private lateinit var digitClassifier: DigitClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        paintView = findViewById(R.id.paint_view)
        digitClassifier = DigitClassifier(this)
        digitClassifier.loadModel()

        findViewById<Button>(R.id.clear_button).setOnClickListener {
            paintView.clearCanvas()
        }

        findViewById<Button>(R.id.predict_button).setOnClickListener {
            val bitmap = paintView.getBitmap()
            val (digit, confidence) = digitClassifier.classify(bitmap)
            findViewById<TextView>(R.id.prediction_text).text =
                "Prediction: $digit (Confidence: ${(confidence * 100).toInt()}%)"
        }
    }
}
