package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GuideActivity10 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide10)

        val backButton10 = findViewById<Button>(R.id.BackButton10)
        val finishGuideButton = findViewById<Button>(R.id.FinishGuideButton)
        val guideText10 = findViewById<TextView>(R.id.GuideText10)

        backButton10.setOnClickListener {
            finish()
        }

        finishGuideButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton10.text = "назад"
            finishGuideButton.text = "завершить"
            guideText10.text = "Чтобы сохранить всю введённую информацию, нажмите на эту кнопку"
        }
        if (language == "UZ") {
            backButton10.text = "orqaga"
            finishGuideButton.text = "tugatish"
            guideText10.text = "Barcha kiritilgan ma'lumotlarni saqlash uchun ushbu tugmani bosing."
        }
    }
}