package com.example.schooltimetable

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GuideActivity10 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide10)

        val backButton10 = findViewById<Button>(R.id.BackButton10)
        backButton10.setOnClickListener {
            finish()
        }

        // changing language
        if (language == "RU") {
            backButton10.text = "назад"
            val guideText10 = findViewById<TextView>(R.id.GuideText10)
            guideText10.text = "Чтобы сохранить всю введённую информацию, нажмите на эту кнопку"
        }
        if (language == "UZ") {
            backButton10.text = "orqaga"
            val guideText10 = findViewById<TextView>(R.id.GuideText10)
            guideText10.text = "Barcha kiritilgan ma'lumotlarni saqlash uchun ushbu tugmani bosing."
        }
    }
}