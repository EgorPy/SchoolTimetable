package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.application.schooltimetable.R

class GuideActivity4 : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide4)

        val backButton4 = findViewById<Button>(R.id.BackButton4)
        val nextButton4 = findViewById<Button>(R.id.NextButton4)

        backButton4.setOnClickListener {
            finish()
        }
        nextButton4.setOnClickListener {
            val intent = Intent(this, GuideActivity5::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            backButton4.text = "назад"
            nextButton4.text = "далее"
            val guideText4 = findViewById<TextView>(R.id.GuideText4)
            guideText4.text = "Здесь вы можете указать сколько каждых классов есть"
        }
        if (language == "UZ") {
            backButton4.text = "orqaga"
            nextButton4.text = "uzoqroq"
            val guideText4 = findViewById<TextView>(R.id.GuideText4)
            guideText4.text = "Bu yerda siz har bir sinfdan nechtasini belgilashingiz mumkin"
        }
    }
}