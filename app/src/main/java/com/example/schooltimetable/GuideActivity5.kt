package com.example.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GuideActivity5 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide5)

        val backButton5 = findViewById<Button>(R.id.BackButton5)
        val nextButton5 = findViewById<Button>(R.id.NextButton5)

        backButton5.setOnClickListener {
            finish()
        }
        nextButton5.setOnClickListener {
            val intent = Intent(this, GuideActivity6::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton5.text = "назад"
            nextButton5.text = "далее"
            val guideText5 = findViewById<TextView>(R.id.GuideText5)
            guideText5.text = "Чтобы добавить/удалить учителя для предмета нажмите на эти кнопки"
        }
        if (language == "UZ") {
            backButton5.text = "orqaga"
            nextButton5.text = "uzoqroq"
            val guideText5 = findViewById<TextView>(R.id.GuideText5)
            guideText5.text = "Mavzu boʻyicha oʻqituvchini qoʻshish/oʻchirish uchun ushbu tugmalarni bosing"
        }
    }
}