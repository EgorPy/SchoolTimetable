package com.example.schooltimetable

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val englishButton = findViewById<Button>(R.id.EnglishButton)
        val russianButton = findViewById<Button>(R.id.RussianButton)
        val uzbekButton = findViewById<Button>(R.id.UzbekButton)

        englishButton.setOnClickListener {
            language = "EN"
            finish()
        }
        russianButton.setOnClickListener {
            language = "RU"
            finish()
        }
        uzbekButton.setOnClickListener {
            language = "UZ"
            finish()
        }

        // changing language
        if (language == "RU") {
            val settingsTitle = findViewById<TextView>(R.id.SettingsTitle)
            settingsTitle.text = "Настройки"
            val languageSectionText = findViewById<TextView>(R.id.LanguageSectionText)
            languageSectionText.text = "Язык"
            val developersSectionText = findViewById<TextView>(R.id.DevelopersSectionText)
            developersSectionText.text = "Разработчики"
            val mainIdeaText = findViewById<TextView>(R.id.MainIdeaText)
            mainIdeaText.text = "Основная идея"
            val implementationsText = findViewById<TextView>(R.id.ImplementationsText)
            implementationsText.text = "Дополнения"
            val frontendText = findViewById<TextView>(R.id.FrontendText)
            frontendText.text = "Фронтэнд"
            val backendText = findViewById<TextView>(R.id.BackendText)
            backendText.text = "Бэкэнд"
            val appVersionText = findViewById<TextView>(R.id.AppVersionText)
            appVersionText.text = "Версия приложения: 1.0"
        }
        if (language == "UZ") {
            val settingsTitle = findViewById<TextView>(R.id.SettingsTitle)
            settingsTitle.text = "Sozlamalar"
            val languageSectionText = findViewById<TextView>(R.id.LanguageSectionText)
            languageSectionText.text = "Til"
            val developersSectionText = findViewById<TextView>(R.id.DevelopersSectionText)
            developersSectionText.text = "Dasturchilar"
            val mainIdeaText = findViewById<TextView>(R.id.MainIdeaText)
            mainIdeaText.text = "Asosiy fikr"
            val implementationsText = findViewById<TextView>(R.id.ImplementationsText)
            implementationsText.text = "Qo'shimchalar"
            val frontendText = findViewById<TextView>(R.id.FrontendText)
            frontendText.text = "Frontend"
            val backendText = findViewById<TextView>(R.id.BackendText)
            backendText.text = "Backend"
            val appVersionText = findViewById<TextView>(R.id.AppVersionText)
            appVersionText.text = "Ilova versiyasi: 1.0"
        }
    }
}
