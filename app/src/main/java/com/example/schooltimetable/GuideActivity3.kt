package com.example.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GuideActivity3 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide3)

        val backButton3 = findViewById<Button>(R.id.BackButton3)
        val nextButton3 = findViewById<Button>(R.id.NextButton3)

        backButton3.setOnClickListener {
            finish()
        }
        nextButton3.setOnClickListener {
            val intent = Intent(this, GuideActivity4::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton3.text = "назад"
            nextButton3.text = "далее"
            val guideText3 = findViewById<TextView>(R.id.GuideText3)
            guideText3.text = "Чтобы добавить/удалить предмет для выбранного класса нажмите на эти кнопки"
        }
        if (language == "UZ") {
            backButton3.text = "orqaga"
            nextButton3.text = "uzoqroq"
            val guideText3 = findViewById<TextView>(R.id.GuideText3)
            guideText3.text = "Tanlangan sinfga element qo'shish/o'chirish uchun ushbu tugmalarni bosing"
        }
    }
}