package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.application.schooltimetable.R

class GuideActivity9 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide9)

        val backButton9 = findViewById<Button>(R.id.BackButton9)
        val nextButton9 = findViewById<Button>(R.id.NextButton9)

        backButton9.setOnClickListener {
            finish()
        }
        nextButton9.setOnClickListener {
            val intent = Intent(this, GuideActivity10::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton9.text = "назад"
            nextButton9.text = "далее"
            val guideText9 = findViewById<TextView>(R.id.GuideText9)
            guideText9.text =
                "Чтобы сохранить таблицу в галерее, необходимо выбрать класс с помощью кнопок \"предыдущий класс\" и \"следующий класс\", а затем нажать кнопку \"сохранить в галерею\""
        }
        if (language == "UZ") {
            backButton9.text = "orqaga"
            nextButton9.text = "uzoqroq"
            val guideText9 = findViewById<TextView>(R.id.GuideText9)
            guideText9.text =
                "Jadvalni galereyada saqlash uchun siz \"oldingi sinf\" va \"keyingi sinf\" tugmalari yordamida sinfni tanlashingiz kerak va keyin \"galereyaga saqlash\" tugmasini bosing."
        }
    }
}