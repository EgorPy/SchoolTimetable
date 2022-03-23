package com.example.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GuideActivity8 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide8)

        val backButton8 = findViewById<Button>(R.id.BackButton8)
        val nextButton8 = findViewById<Button>(R.id.NextButton8)

        backButton8.setOnClickListener {
            finish()
        }
        nextButton8.setOnClickListener {
            val intent = Intent(this, GuideActivity9::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton8.text = "назад"
            nextButton8.text = "далее"
            val guideText8 = findViewById<TextView>(R.id.GuideText8)
            guideText8.text = "После того, как вы нажали кнопку \"Создать расписание\", вы можете изменить предметы и классы в таблице, нажав на них"
        }
        if (language == "UZ") {
            backButton8.text = "orqaga"
            nextButton8.text = "uzoqroq"
            val guideText8 = findViewById<TextView>(R.id.GuideText8)
            guideText8.text =
                "\"Jadval yaratish\" tugmasini bosganingizdan so'ng, ularni bosish orqali jadvaldagi elementlar va sinflarni o'zgartirishingiz mumkin."
        }
    }
}