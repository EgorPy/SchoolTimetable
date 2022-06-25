package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.application.schooltimetable.R

class GuideActivity6 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide6)

        val backButton6 = findViewById<Button>(R.id.BackButton6)
        val nextButton6 = findViewById<Button>(R.id.NextButton6)

        backButton6.setOnClickListener {
            finish()
        }
        nextButton6.setOnClickListener {
            val intent = Intent(this, GuideActivity7::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton6.text = "назад"
            nextButton6.text = "далее"
            val guideText6 = findViewById<TextView>(R.id.GuideText6)
            guideText6.text = "Здесь вы можете настроить расписание звонков"
        }
        if (language == "UZ") {
            backButton6.text = "orqaga"
            nextButton6.text = "uzoqroq"
            val guideText6 = findViewById<TextView>(R.id.GuideText6)
            guideText6.text = "Bu erda siz qo'ng'iroqlar jadvalini o'rnatishingiz mumkin"
        }
    }
}