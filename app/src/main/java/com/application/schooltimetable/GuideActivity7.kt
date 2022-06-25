package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.application.schooltimetable.R

class GuideActivity7 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide7)

        val backButton7 = findViewById<Button>(R.id.BackButton7)
        val nextButton7 = findViewById<Button>(R.id.NextButton7)

        backButton7.setOnClickListener {
            finish()
        }
        nextButton7.setOnClickListener {
            val intent = Intent(this, GuideActivity8::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton7.text = "назад"
            nextButton7.text = "далее"
            val guideText7 = findViewById<TextView>(R.id.GuideText7)
            guideText7.text =
                "После того, как вы ввели информацию в поля выше, нажмите кнопку \"сгенерировать расписание\" чтобы сгенерировать расписание"
        }
        if (language == "UZ") {
            backButton7.text = "orqaga"
            nextButton7.text = "uzoqroq"
            val guideText7 = findViewById<TextView>(R.id.GuideText7)
            guideText7.text =
                "Yuqoridagi maydonlarga ma'lumotlarni kiritganingizdan so'ng, jadvalni yaratish uchun \"Jadvalni yaratish\" tugmasini bosing."
        }
    }
}