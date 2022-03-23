package com.example.schooltimetable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


@Suppress("DEPRECATION")
class MyCanvasActivity : AppCompatActivity() {
    private lateinit var main: View
    private lateinit var imageView: ImageView
    private lateinit var b: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_canvas)

        // drawing timetable
        main = findViewById(R.id.MyCanvasLayout)
        imageView = findViewById(R.id.MyImageView)
        val lastScreenshotButton = findViewById<Button>(R.id.LastScreenshotButton)

        lastScreenshotButton.setOnClickListener {
            b = takeScreenshotOfRootView(imageView)
            b.width = 650
            b.height = 1100

            val canvas = Canvas(b)
            val textPaint = Paint().apply {
                color = Color.parseColor("#000000")
                style = Paint.Style.FILL
                isAntiAlias = true

                textSize = 14F
                textAlign = Paint.Align.CENTER
            }
            val tableNamesRectPaint = Paint().apply {
                color = Color.parseColor("#CCCCCC")
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            val subjectNamesPaint = Paint().apply {
                color = Color.parseColor("#EEEEEE")
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            val classroomsPaint = Paint().apply {
                color = Color.parseColor("#F9F9F9")
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            // drawing 1st row rect
            val classTextWidth = textPaint.measureText("${selectedClassGrade + 1}${letters[selectedClassLetter]}")
            canvas.drawRect(0F, 0F, 650F, 10F, tableNamesRectPaint)
            // drawing 1st row text
            canvas.drawText("${selectedClassGrade + 1}${letters[selectedClassLetter]}", 50F + (600F - classTextWidth) / 2F, 10F, textPaint)
            // drawing 2nd row rect
            canvas.drawRect(0F, 10F, 650F, 30F, tableNamesRectPaint)
            // drawing 2nd row textS
            if (language == "UZ") {
                val subjectTextWidth = textPaint.measureText("Element")
                val classroomTextWidth = textPaint.measureText("Kabinet")
                canvas.drawText("Element", 50F + (400F - subjectTextWidth) / 2, 20F, textPaint)
                canvas.drawText("Kabinet", 100F + 400F + (200F - classroomTextWidth) / 2, 20F, textPaint)
            } else if (language == "RU") {
                val subjectTextWidth = textPaint.measureText("Предмет")
                val classroomTextWidth = textPaint.measureText("Кабинет")
                canvas.drawText("Предмет", 50F + (400F - subjectTextWidth) / 2, 20F, textPaint)
                canvas.drawText("Кабинет", 100F + 400F + (200F - classroomTextWidth) / 2, 20F, textPaint)
            } else {
                val subjectTextWidth = textPaint.measureText("Subject")
                val classroomTextWidth = textPaint.measureText("Classroom")
                canvas.drawText("Subject", 50F + (400F - subjectTextWidth) / 2, 20F, textPaint)
                canvas.drawText("Classroom", 100F + 400F + (200F - classroomTextWidth) / 2, 20F, textPaint)
            }
            if (language == "EN") {
                var count = 0
                for (week_day in 0 until daysInWeek) { // this is for y
                    for (lesson in 0 until (maxNumberOfLessons + 1)) { // this is also for y
                        if (lesson < maxNumberOfLessons) {
                            canvas.drawRect(0F, 30F + count * 13, 50F, 43F + count * 13, tableNamesRectPaint)
                            val weekdayLetterWidth = textPaint.measureText("M")
                            canvas.drawText("${weekDays[week_day][lesson]}", 5F, 43F + count * 13, textPaint)
                            canvas.drawText("${(lesson + 1)}", weekdayLetterWidth * 2, 43F + count * 13, textPaint)

                            canvas.drawRect(50F, 30F + count * 13, 450F, 43F + count * 13, subjectNamesPaint)
                            canvas.drawRect(450F, 30F + count * 13, 650F, 43F + count * 13, classroomsPaint)
                            try {
                                val subjectWidth = textPaint.measureText(subjectsList[week_day][lesson][selectedClassGrade][selectedClassLetter])
                                canvas.drawText(
                                    subjectsList[week_day][lesson][selectedClassGrade][selectedClassLetter],
                                    50F + (400 - subjectWidth) / 2,
                                    43F + count * 13,
                                    textPaint
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                            try {
                                val classroomWidth = textPaint.measureText(classroomsList[week_day][lesson][selectedClassGrade][selectedClassLetter])
                                canvas.drawText(
                                    classroomsList[week_day][lesson][selectedClassGrade][selectedClassLetter],
                                    450F + (200 - classroomWidth) / 2,
                                    43F + count * 13,
                                    textPaint
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        } else {
                            canvas.drawRect(0F, 30F + count * 13, 650F, 43F + count * 13, tableNamesRectPaint)
                        }
                        count += 1
                    }
                }
            }
            if (language == "RU") {
                var count = 0
                for (week_day in 0 until daysInWeek) { // this is for y
                    for (lesson in 0 until (maxNumberOfLessons + 1)) { // this is also for y
                        if (lesson < maxNumberOfLessons) {
                            canvas.drawRect(0F, 30F + count * 13, 50F, 43F + count * 13, tableNamesRectPaint)
                            val weekdayLetterWidth = textPaint.measureText("M")
                            canvas.drawText("${weekDaysRu[week_day][lesson]}", 5F, 43F + count * 13, textPaint)
                            canvas.drawText("${(lesson + 1)}", weekdayLetterWidth * 2, 43F + count * 13, textPaint)

                            canvas.drawRect(50F, 30F + count * 13, 450F, 43F + count * 13, subjectNamesPaint)
                            canvas.drawRect(450F, 30F + count * 13, 650F, 43F + count * 13, classroomsPaint)
                            try {
                                val subjectWidth = textPaint.measureText(subjectsList[week_day][lesson][selectedClassGrade][selectedClassLetter])
                                canvas.drawText(
                                    subjectsList[week_day][lesson][selectedClassGrade][selectedClassLetter],
                                    50F + (400 - subjectWidth) / 2,
                                    43F + count * 13,
                                    textPaint
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                            try {
                                val classroomWidth = textPaint.measureText(classroomsList[week_day][lesson][selectedClassGrade][selectedClassLetter])
                                canvas.drawText(
                                    classroomsList[week_day][lesson][selectedClassGrade][selectedClassLetter],
                                    450F + (200 - classroomWidth) / 2,
                                    43F + count * 13,
                                    textPaint
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        } else {
                            canvas.drawRect(0F, 30F + count * 13, 650F, 43F + count * 13, tableNamesRectPaint)
                        }
                        count += 1
                    }
                }
            }
            if (language == "UZ") {
                var count = 0
                for (week_day in 0 until daysInWeek) { // this is for y
                    for (lesson in 0 until (maxNumberOfLessons + 1)) { // this is also for y
                        if (lesson < maxNumberOfLessons) {
                            canvas.drawRect(0F, 30F + count * 13, 50F, 43F + count * 13, tableNamesRectPaint)
                            val weekdayLetterWidth = textPaint.measureText("M")
                            canvas.drawText("${weekDaysUz[week_day][lesson]}", 5F, 43F + count * 13, textPaint)
                            canvas.drawText("${(lesson + 1)}", weekdayLetterWidth * 2, 43F + count * 13, textPaint)

                            canvas.drawRect(50F, 30F + count * 13, 450F, 43F + count * 13, subjectNamesPaint)
                            canvas.drawRect(450F, 30F + count * 13, 650F, 43F + count * 13, classroomsPaint)
                            try {
                                val subjectWidth = textPaint.measureText(subjectsList[week_day][lesson][selectedClassGrade][selectedClassLetter])
                                canvas.drawText(
                                    subjectsList[week_day][lesson][selectedClassGrade][selectedClassLetter],
                                    50F + (400 - subjectWidth) / 2,
                                    43F + count * 13,
                                    textPaint
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                            try {
                                val classroomWidth = textPaint.measureText(classroomsList[week_day][lesson][selectedClassGrade][selectedClassLetter])
                                canvas.drawText(
                                    classroomsList[week_day][lesson][selectedClassGrade][selectedClassLetter],
                                    450F + (200 - classroomWidth) / 2,
                                    43F + count * 13,
                                    textPaint
                                )
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        } else {
                            canvas.drawRect(0F, 30F + count * 13, 650F, 43F + count * 13, tableNamesRectPaint)
                        }
                        count += 1
                    }
                }
            }

            imageView.setImageBitmap(b)

            println("MyCanvasActivity.kt: drawing finished")

            // Save the image to gallery and get saved image Uri
            saveImage(Calendar.getInstance().time.toString())
            if (language == "EN") {
                Toast.makeText(this, "Timetable for ${selectedClassGrade + 1}${letters[selectedClassLetter]} saved to the gallery", Toast.LENGTH_LONG)
                    .show()
            }
            if (language == "RU") {
                Toast.makeText(this, "Таблица для ${selectedClassGrade + 1}${letters[selectedClassLetter]} сохранена в галерею", Toast.LENGTH_LONG)
                    .show()
            }
            if (language == "UZ") {
                Toast.makeText(this, "${selectedClassGrade + 1}${letters[selectedClassLetter]} uchun jadval galereyaga saqlangan", Toast.LENGTH_LONG)
                    .show()
            }

            println("Timetable image saved to the gallery")
        }

        if (language == "RU") {
            lastScreenshotButton.text = "Сохранить в галерею"
        }
        if (language == "UZ") {
            lastScreenshotButton.text = "Galereyaga saqlang"
        }
    }

    companion object Screenshot {
        private fun takeScreenshot(view: View): Bitmap {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache(true)
            val b = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            return b
        }

        fun takeScreenshotOfRootView(v: View): Bitmap {
            return takeScreenshot(v.rootView)
        }
    }

    // Method to save an image to gallery and return uri
    private fun saveImage(title: String): Uri {
        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            b,
            title,
            "Image of $title"
        )

        // Parse the gallery image url to uri
        return Uri.parse(savedImageURL)
    }
}
