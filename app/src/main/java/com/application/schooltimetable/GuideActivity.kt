package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.application.schooltimetable.R

class GuideActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        val backButton = findViewById<Button>(R.id.BackButton)
        val nextButton = findViewById<Button>(R.id.NextButton)

        backButton.setOnClickListener {
            finish()
        }
        nextButton.setOnClickListener {
            val intent = Intent(this, GuideActivity2::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton.text = "назад"
            nextButton.text = "далее"
            val guideText = findViewById<TextView>(R.id.GuideText)
            guideText.text = "Чтобы создать новую таблицу нажмите на эту кнопку"
        }
        if (language == "UZ") {
            backButton.text = "orqaga"
            nextButton.text = "uzoqroq"
            val guideText = findViewById<TextView>(R.id.GuideText)
            guideText.text = "Yangi jadval yaratish uchun ushbu tugmani bosing"
        }
    }
}