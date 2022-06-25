package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.application.schooltimetable.R

class GuideActivity2 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide2)

        val backButton2 = findViewById<Button>(R.id.BackButton2)
        val nextButton2 = findViewById<Button>(R.id.NextButton2)

        backButton2.setOnClickListener {
            finish()
        }
        nextButton2.setOnClickListener {
            val intent = Intent(this, GuideActivity3::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton2.text = "назад"
            nextButton2.text = "далее"
            val guideText2 = findViewById<TextView>(R.id.GuideText2)
            guideText2.text = "Чтобы выбрать класс нажмите на эту кнопку и выберите класс"
        }
        if (language == "UZ") {
            backButton2.text = "orqaga"
            nextButton2.text = "uzoqroq"
            val guideText2 = findViewById<TextView>(R.id.GuideText2)
            guideText2.text = "Sinfni tanlash uchun ushbu tugmani bosing va sinfni tanlang"
        }
    }
}