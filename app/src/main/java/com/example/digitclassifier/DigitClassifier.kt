//package com.example.digit classifier
//
//
//
//import android.content.Context
//import org.tensorflow.lite.Interpreter
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//import java.io.FileInputStream
//import android.graphics.Bitmap
//class DigitClassifier(private val context: Context) {
//    private late init var interpreter: Interpreter
//
//    fun loadModel() {
//        val modelFile = context.assets.openFd("mnist.tflite")
//        val inputStream = FileInputStream(modelFile.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = modelFile.startOffset
//        val declaredLength = modelFile.declaredLength
//        val mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//        interpreter = Interpreter(mappedByteBuffer)
//    }
//
//    fun classify(bitmap: Bitmap): Pair<Int, Float> {
//        val input = Array(1) { Array(28) { FloatArray(28) } }
//        val output = Array(1) { FloatArray(10) }
//
//        for (y in 0 until 28) {
//            for (x in 0 until 28) {
//                val pixel = bitmap.getPixel(x, y)
//                input[0][y][x] = (0xFF - (pixel and 0xFF)) / 255.0f
//            }
//        }
//
//        interpreter.run(input, output)
//
//        val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: -1
//        return Pair(maxIdx, output[0][maxIdx])
//    }
//}
package com.example.digitclassifier

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.channels.FileChannel

class DigitClassifier(private val context: Context) {
    private lateinit var interpreter: Interpreter

    // Method to load the TFLite model
    fun loadModel() {
        try {
            Log.d("DigitClassifier", "Loading model...")
            val modelFile = context.assets.openFd("mnist.tflite")
            val inputStream = FileInputStream(modelFile.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = modelFile.startOffset
            val declaredLength = modelFile.declaredLength
            val mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            interpreter = Interpreter(mappedByteBuffer)
            Log.d("DigitClassifier", "Model loaded successfully.")
        } catch (e: Exception) {
            Log.e("DigitClassifier", "Error loading model: ${e.message}")
            e.printStackTrace()
        }
    }

    // Method to classify the given bitmap
    fun classify(bitmap: Bitmap): Pair<Int, Float> {
        try {
            Log.d("DigitClassifier", "Classifying image...")

            // Prepare the input array from the bitmap
            val input = Array(1) { Array(28) { FloatArray(28) } }
            val output = Array(1) { FloatArray(10) }

            for (y in 0 until 28) {
                for (x in 0 until 28) {
                    val pixel = bitmap.getPixel(x, y)
                    // Convert to grayscale and normalize
                    input[0][y][x] = (0xFF - (pixel and 0xFF)) / 255.0f
                }
            }

            // Run the model on the input
            interpreter.run(input, output)
            Log.d("DigitClassifier", "Model inference completed.")

            // Get the index of the maximum value from the output
            val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: -1
            Log.d("DigitClassifier", "Predicted label: $maxIdx with confidence: ${output[0][maxIdx]}")

            return Pair(maxIdx, output[0][maxIdx])
        } catch (e: Exception) {
            Log.e("DigitClassifier", "Error during classification: ${e.message}")
            e.printStackTrace()
            return Pair(-1, 0f) // Return an invalid result in case of an error
        }
    }
}
