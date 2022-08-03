package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


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

        val pm = applicationContext.packageManager
        val pkgName = applicationContext.packageName
        var pkgInfo: PackageInfo? = null
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val ver = pkgInfo!!.versionName

        val settingsTitle = findViewById<TextView>(R.id.SettingsTitle)
        val languageSectionText = findViewById<TextView>(R.id.LanguageSectionText)
        val developersSectionText = findViewById<TextView>(R.id.DevelopersSectionText)
        val mainIdeaText = findViewById<TextView>(R.id.MainIdeaText)
        val implementationsText = findViewById<TextView>(R.id.ImplementationsText)
        val frontendText = findViewById<TextView>(R.id.FrontendText)
        val backendText = findViewById<TextView>(R.id.BackendText)
        val appVersionText = findViewById<TextView>(R.id.AppVersionText)

        // changing language
        if (language == "EN") {
            appVersionText.text = "App Version: $ver"
        }
        if (language == "RU") {
            settingsTitle.text = "Настройки"
            languageSectionText.text = "Язык"
            developersSectionText.text = "Разработчики"
            mainIdeaText.text = "Основная идея"
            implementationsText.text = "Дополнения"
            frontendText.text = "Фронтэнд"
            backendText.text = "Бэкэнд"
            appVersionText.text = "Версия приложения: $ver"
        }
        if (language == "UZ") {
            settingsTitle.text = "Sozlamalar"
            languageSectionText.text = "Til"
            developersSectionText.text = "Dasturchilar"
            mainIdeaText.text = "Asosiy fikr"
            implementationsText.text = "Qo'shimchalar"
            frontendText.text = "Frontend"
            backendText.text = "Backend"
            appVersionText.text = "Ilova versiyasi: $ver"
        }
    }
}
