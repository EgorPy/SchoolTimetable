package com.application.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import java.io.*
import java.util.*

var language = Locale.getDefault().language.uppercase()

var timetablesCount = 0
var timetableNames = mutableListOf<String>()
var timetableDates = mutableListOf<String>()

val savedSubjectNames = mutableListOf<MutableList<MutableList<String>>>()

//                      ^list of timetables ^list of grades ^list of subjects(String)
val savedSubjectTimes = mutableListOf<MutableList<MutableList<String>>>()

//                      ^list of timetables ^list of grades ^list of subject times(String)
val savedNumberOfEveryGrade = mutableListOf<MutableList<Int>>()

//                            ^list of timetables ^list of number of grades
val savedLessonsScheduleStartTimes = mutableListOf<MutableList<String>>()

//                                  ^list of timetables ^list of lessons start time
val savedLessonsScheduleEndTimes = mutableListOf<MutableList<String>>()

//                                  ^list of timetables ^list of lessons end time
val savedLessonsScheduleBrakeTimes = mutableListOf<MutableList<String>>()

//                                  ^list of timetables ^list of lessons brake time
val savedIsTimetableGenerated = mutableListOf<Boolean>()

//                              ^list of boolean variables of generated timetable for every timetable
val savedSubjectsList = mutableListOf<MutableList<MutableList<MutableList<MutableList<String>>>>>()

//                      ^list of timetables ^daysInWeek ^maxNUmberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of Strings with names of subjects
val savedClassroomsList = mutableListOf<MutableList<MutableList<MutableList<MutableList<String>>>>>()

//                      ^list of timetables ^daysInWeek ^maxNUmberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of Strings with names of classrooms

var saveTimetable = false
var saveTimetableIndex = 0

var loadTimetable = false
var loadTimetableIndex = 0

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guideButton = findViewById<Button>(R.id.GuideButton)
        val settingsButton = findViewById<Button>(R.id.SettingsButton)
        val createNewTimetableButton = findViewById<Button>(R.id.CreateNewTimetableButton)
        val noTimetablesText = findViewById<TextView>(R.id.NoTimetablesText)
        val timetablesVerticalLayout = findViewById<LinearLayout>(R.id.TimetablesVerticalLayout)
        val deleteTimetableButton = findViewById<Button>(R.id.DeleteTimetableButton)

        // trying to load data
        loadData()
        if (timetablesCount > 0) {
            Toast.makeText(this, "Data loaded", Toast.LENGTH_LONG).show()
        }
        // after loading data, I need to create saved timetable layouts
        for (timetable in 0 until timetablesCount) {
            val layout = LinearLayout(this)
            layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).also {
                it.bottomMargin = 10
            }
            layout.background = Color.LTGRAY.toDrawable()
            layout.orientation = LinearLayout.HORIZONTAL
            layout.gravity = Gravity.CENTER
            val timetableNameButton = Button(this)
            timetableNameButton.textSize = 14F
            val timetableNameButtonParam =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            timetableNameButtonParam.weight = 1F
            timetableNameButton.layoutParams = timetableNameButtonParam
            timetableNameButton.setPadding(0, 0, 0, 0)
            timetableNameButton.text = timetableNames[timetable]
            timetableNameButton.background = Color.LTGRAY.toDrawable()
            val timetableDateButton = Button(this)
            timetableDateButton.textSize = 14F
            val timetableDateButtonParam =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            timetableDateButtonParam.weight = 1F
            timetableDateButton.layoutParams = timetableDateButtonParam
            timetableDateButton.setPadding(0, 0, 0, 0)
            timetableDateButton.text = timetableDates[timetable]
            timetableDateButton.background = Color.LTGRAY.toDrawable()

            layout.addView(timetableNameButton)
            layout.addView(timetableDateButton)

            timetablesVerticalLayout.addView(layout)

            // I need to get index of clicked button
            timetableNameButton.setOnClickListener {
                loadTimetable = true
                loadTimetableIndex = timetablesVerticalLayout.indexOfChild(layout)

                val intent = Intent(this, CreateNewTimetableActivity::class.java)
                startActivity(intent)
            }
            timetableDateButton.setOnClickListener {
                loadTimetable = true
                loadTimetableIndex = timetablesVerticalLayout.indexOfChild(layout)

//                println("saveTimetableIndex: ${saveTimetableIndex - 1}") // I need to subtract 1 from saveTimetableIndex to get actual index

                val intent = Intent(this, CreateNewTimetableActivity::class.java)
                startActivity(intent)
            }

            saveTimetableIndex += 1
        }

        deleteTimetableButton.setOnClickListener {
            if (timetablesVerticalLayout.childCount > 0) {
                timetablesVerticalLayout.removeViewAt(timetablesVerticalLayout.childCount - 1)
                timetablesCount -= 1

                saveTimetableIndex -= 1

                // now I need to remove last timetable from all of the saved lists
                timetableNames.removeLast()
                timetableDates.removeLast()
                savedSubjectNames.removeLast()
                savedSubjectTimes.removeLast()
                savedNumberOfEveryGrade.removeLast()
                savedLessonsScheduleStartTimes.removeLast()
                savedLessonsScheduleEndTimes.removeLast()
                savedLessonsScheduleBrakeTimes.removeLast()
                savedIsTimetableGenerated.removeLast()
                savedSubjectsList.removeLast()
                savedClassroomsList.removeLast()

                // now I need to set files data to blank string
                // saving timetablesCount
                inputData("timetablesCount", timetablesCount.toString())
                // saving timetableNames
                inputData("timetableNames $timetablesCount", "")
                // saving timetableDates
                inputData("timetableDates $timetablesCount", "")
                // saving savedSubjectNames
                for (grade in 0 until numberOfGrades) {
                    for (subject in 0 until maxNumberOfSubjects) {
                        try {
                            inputData("savedSubjectNames $timetablesCount $grade $subject", "")
                        } catch (e: IndexOutOfBoundsException) {
                            inputData("savedSubjectNames $timetablesCount $grade $subject", "")
                        }
                    }
                }
                // saving savedSubjectTimes
                for (grade in 0 until numberOfGrades) {
                    for (subject in 0 until maxNumberOfSubjects) {
                        try {
                            inputData("savedSubjectTimes $timetablesCount $grade $subject", "")
                        } catch (e: IndexOutOfBoundsException) {
                            inputData("savedSubjectTimes $timetablesCount $grade $subject", "")
                        }
                    }
                }
                // saving savedNumberOfEveryGrade
                for (grade in 0 until numberOfGrades) {
                    try {
                        inputData("savedNumberOfEveryGrade $timetablesCount $grade", "")
                    } catch (e: IndexOutOfBoundsException) {
                        inputData("savedNumberOfEveryGrade $timetablesCount $grade", "")
                    }
                }
                // saving savedLessonsScheduleStartTimes
                for (lesson in 0 until maxNumberOfLessons) {
                    try {
                        inputData("savedLessonsScheduleStartTimes $timetablesCount $lesson", "")
                    } catch (e: IndexOutOfBoundsException) {
                        inputData("savedLessonsScheduleStartTimes $timetablesCount $lesson", "")
                    }
                }
                // saving savedLessonsScheduleEndTimes
                for (lesson in 0 until maxNumberOfLessons) {
                    try {
                        inputData("savedLessonsScheduleEndTimes $timetablesCount $lesson", "")
                    } catch (e: IndexOutOfBoundsException) {
                        inputData("savedLessonsScheduleEndTimes $timetablesCount $lesson", "")
                    }
                }
                // saving savedLessonsScheduleBrakeTimes
                for (lesson in 0 until maxNumberOfLessons) {
                    try {
                        inputData("savedLessonsScheduleBrakeTimes $timetablesCount $lesson", "")
                    } catch (e: IndexOutOfBoundsException) {
                        inputData("savedLessonsScheduleBrakeTimes $timetablesCount $lesson", "")
                    }
                }
                // saving savedIsTimetableGenerated
                try {
                    inputData("savedIsTimetableGenerated $timetablesCount", "")
                } catch (e: IndexOutOfBoundsException) {
                    inputData("savedIsTimetableGenerated $timetablesCount", "")
                }
                // saving savedSubjectsList
                for (week_day in 0 until daysInWeek) {
                    for (lesson in 0 until maxNumberOfLessons) {
                        for (grade in 0 until numberOfGrades) {
                            try {
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    inputData(
                                        "savedSubjectsList $timetablesCount $week_day $lesson $grade $grade_letter",
                                        ""
                                    )
                                }
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                    }
                }
                // saving savedClassroomsList
                for (week_day in 0 until daysInWeek) {
                    for (lesson in 0 until maxNumberOfLessons) {
                        for (grade in 0 until numberOfGrades) {
                            try {
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    inputData(
                                        "savedClassroomsList $timetablesCount $week_day $lesson $grade $grade_letter",
                                        ""
                                    )
                                }
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                    }
                }

                if (timetablesVerticalLayout.childCount < 1) {
                    noTimetablesText.visibility = TextView.VISIBLE
                } else {
                    noTimetablesText.visibility = TextView.GONE
                }
            }
        }

        if (timetablesVerticalLayout.childCount < 1) {
            noTimetablesText.visibility = TextView.VISIBLE
        } else {
            noTimetablesText.visibility = TextView.GONE
        }

        guideButton.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }

        createNewTimetableButton.setOnClickListener {
            val intent = Intent(this, CreateNewTimetableActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // changing language
        if (language == "RU") {
            settingsButton.text = "Настройки"
            guideButton.text = "Помощь"
            val timetablesTitle = findViewById<TextView>(R.id.TimetablesTitle)
            timetablesTitle.text = "Таблицы"
            createNewTimetableButton.text = "Создать новую таблицу"
            deleteTimetableButton.text = "Удалить таблицу"
            noTimetablesText.text = "Здесь пока нет таблиц. Вы можете создать новую таблицу нажав на кнопку \"Создать новую таблицу\""
        }
        if (language == "UZ") {
            settingsButton.text = "Sozlamalar"
            guideButton.text = "Hidoyat"
            val timetablesTitle = findViewById<TextView>(R.id.TimetablesTitle)
            timetablesTitle.text = "Jadvallar"
            createNewTimetableButton.text = "Yangi jadval yarating"
            deleteTimetableButton.text = "Jadvalni o'chirish"
            noTimetablesText.text = "Bu yerda hali stol yo'q. \"Yangi jadval yaratish\" tugmasini bosish orqali yangi jadval yaratishingiz mumkin"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onRestart() {
        super.onRestart()

        val timetablesVerticalLayout = findViewById<LinearLayout>(R.id.TimetablesVerticalLayout)
        val noTimetablesText = findViewById<TextView>(R.id.NoTimetablesText)
        val deleteTimetableButton = findViewById<Button>(R.id.DeleteTimetableButton)

        if (saveTimetable) {
//            try {
            val layout = LinearLayout(this)
            layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).also {
                it.bottomMargin = 10
            }
            layout.background = Color.LTGRAY.toDrawable()
            layout.orientation = LinearLayout.HORIZONTAL
            layout.gravity = Gravity.CENTER
            val timetableNameButton = Button(this)
            timetableNameButton.textSize = 14F
            val timetableNameButtonParam =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            timetableNameButtonParam.weight = 1F
            timetableNameButton.layoutParams = timetableNameButtonParam
            timetableNameButton.setPadding(0, 0, 0, 0)
            timetableNameButton.text = timetableNames[saveTimetableIndex]
            timetableNameButton.background = Color.LTGRAY.toDrawable()
            val timetableDateButton = Button(this)
            timetableDateButton.textSize = 14F
            val timetableDateButtonParam =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            timetableDateButtonParam.weight = 1F
            timetableDateButton.layoutParams = timetableDateButtonParam
            timetableDateButton.setPadding(0, 0, 0, 0)
            timetableDateButton.text = timetableDates[saveTimetableIndex]
            timetableDateButton.background = Color.LTGRAY.toDrawable()

            layout.addView(timetableNameButton)
            layout.addView(timetableDateButton)

            timetablesVerticalLayout.addView(layout)

            // I need to get index of clicked button
            timetableNameButton.setOnClickListener {
                loadTimetable = true
                loadTimetableIndex = timetablesVerticalLayout.indexOfChild(layout)

                println("savedIsTimetableGenerated $savedIsTimetableGenerated")

                val intent = Intent(this, CreateNewTimetableActivity::class.java)
                startActivity(intent)
            }
            timetableDateButton.setOnClickListener {
                loadTimetable = true
                loadTimetableIndex = timetablesVerticalLayout.indexOfChild(layout)

//                println("saveTimetableIndex: ${saveTimetableIndex - 1}") // I need to subtract 1 from saveTimetableIndex to get actual index

                val intent = Intent(this, CreateNewTimetableActivity::class.java)
                startActivity(intent)
            }

            saveTimetableIndex += 1

            deleteTimetableButton.setOnClickListener {
                if (timetablesVerticalLayout.childCount > 0) {
                    timetablesVerticalLayout.removeViewAt(timetablesVerticalLayout.childCount - 1)
                    timetablesCount -= 1

                    saveTimetableIndex -= 1

                    // now I need to remove last timetable from all of the saved lists
                    timetableNames.removeLast()
                    timetableDates.removeLast()
                    savedSubjectNames.removeLast()
                    savedSubjectTimes.removeLast()
                    savedNumberOfEveryGrade.removeLast()
                    savedLessonsScheduleStartTimes.removeLast()
                    savedLessonsScheduleEndTimes.removeLast()
                    savedLessonsScheduleBrakeTimes.removeLast()
                    savedIsTimetableGenerated.removeLast()
                    savedSubjectsList.removeLast()
                    savedClassroomsList.removeLast()

                    // now I need to set files data to blank string
                    // saving timetablesCount
                    inputData("timetablesCount", timetablesCount.toString())
                    // saving timetableNames
                    inputData("timetableNames $timetablesCount", "")
                    // saving timetableDates
                    inputData("timetableDates $timetablesCount", "")
                    // saving savedSubjectNames
                    for (grade in 0 until numberOfGrades) {
                        for (subject in 0 until maxNumberOfSubjects) {
                            try {
                                inputData("savedSubjectNames $timetablesCount $grade $subject", "")
                            } catch (e: IndexOutOfBoundsException) {
                                inputData("savedSubjectNames $timetablesCount $grade $subject", "")
                            }
                        }
                    }
                    // saving savedSubjectTimes
                    for (grade in 0 until numberOfGrades) {
                        for (subject in 0 until maxNumberOfSubjects) {
                            try {
                                inputData("savedSubjectTimes $timetablesCount $grade $subject", "")
                            } catch (e: IndexOutOfBoundsException) {
                                inputData("savedSubjectTimes $timetablesCount $grade $subject", "")
                            }
                        }
                    }
                    // saving savedNumberOfEveryGrade
                    for (grade in 0 until numberOfGrades) {
                        try {
                            inputData("savedNumberOfEveryGrade $timetablesCount $grade", "")
                        } catch (e: IndexOutOfBoundsException) {
                            inputData("savedNumberOfEveryGrade $timetablesCount $grade", "")
                        }
                    }
                    // saving savedLessonsScheduleStartTimes
                    for (lesson in 0 until maxNumberOfLessons) {
                        try {
                            inputData("savedLessonsScheduleStartTimes $timetablesCount $lesson", "")
                        } catch (e: IndexOutOfBoundsException) {
                            inputData("savedLessonsScheduleStartTimes $timetablesCount $lesson", "")
                        }
                    }
                    // saving savedLessonsScheduleEndTimes
                    for (lesson in 0 until maxNumberOfLessons) {
                        try {
                            inputData("savedLessonsScheduleEndTimes $timetablesCount $lesson", "")
                        } catch (e: IndexOutOfBoundsException) {
                            inputData("savedLessonsScheduleEndTimes $timetablesCount $lesson", "")
                        }
                    }
                    // saving savedLessonsScheduleBrakeTimes
                    for (lesson in 0 until maxNumberOfLessons) {
                        try {
                            inputData("savedLessonsScheduleBrakeTimes $timetablesCount $lesson", "")
                        } catch (e: IndexOutOfBoundsException) {
                            inputData("savedLessonsScheduleBrakeTimes $timetablesCount $lesson", "")
                        }
                    }
                    // saving savedIsTimetableGenerated
                    try {
                        inputData("savedIsTimetableGenerated $timetablesCount", "")
                    } catch (e: IndexOutOfBoundsException) {
                        inputData("savedIsTimetableGenerated $timetablesCount", "")
                    }
                    // saving savedSubjectsList
                    for (week_day in 0 until daysInWeek) {
                        for (lesson in 0 until maxNumberOfLessons) {
                            for (grade in 0 until numberOfGrades) {
                                try {
                                    for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                        inputData(
                                            "savedSubjectsList $timetablesCount $week_day $lesson $grade $grade_letter",
                                            ""
                                        )
                                    }
                                } catch (e: IndexOutOfBoundsException) {
                                }
                            }
                        }
                    }
                    // saving savedClassroomsList
                    for (week_day in 0 until daysInWeek) {
                        for (lesson in 0 until maxNumberOfLessons) {
                            for (grade in 0 until numberOfGrades) {
                                try {
                                    for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                        inputData(
                                            "savedClassroomsList $timetablesCount $week_day $lesson $grade $grade_letter",
                                            ""
                                        )
                                    }
                                } catch (e: IndexOutOfBoundsException) {
                                }
                            }
                        }
                    }

                    if (timetablesVerticalLayout.childCount < 1) {
                        noTimetablesText.visibility = TextView.VISIBLE
                    } else {
                        noTimetablesText.visibility = TextView.GONE
                    }
                }
            }

            // saving all data after all
            saveData()

            if (timetablesVerticalLayout.childCount < 1) {
                noTimetablesText.visibility = TextView.VISIBLE
            } else {
                noTimetablesText.visibility = TextView.GONE
            }

            saveTimetable = false
        }

        // changing language
        if (language == "EN") {
            val settingsButton = findViewById<Button>(R.id.SettingsButton)
            settingsButton.text = "Settings"
            val guideButton = findViewById<Button>(R.id.GuideButton)
            guideButton.text = "Guide"
            val timetablesTitle = findViewById<TextView>(R.id.TimetablesTitle)
            timetablesTitle.text = "Timetables"
            val createNewTimetableButton = findViewById<Button>(R.id.CreateNewTimetableButton)
            createNewTimetableButton.text = "Create new timetable"
            deleteTimetableButton.text = "Delete timetable"
            noTimetablesText.text = "There are no timetables in this list. You can create one by pressing the \"Create new timetable\" button"
        }
        if (language == "RU") {
            val settingsButton = findViewById<Button>(R.id.SettingsButton)
            settingsButton.text = "Настройки"
            val guideButton = findViewById<Button>(R.id.GuideButton)
            guideButton.text = "Помощь"
            val timetablesTitle = findViewById<TextView>(R.id.TimetablesTitle)
            timetablesTitle.text = "Таблицы"
            val createNewTimetableButton = findViewById<Button>(R.id.CreateNewTimetableButton)
            createNewTimetableButton.text = "Создать новую таблицу"
            deleteTimetableButton.text = "Удалить таблицу"
            noTimetablesText.text = "Здесь пока нет таблиц. Вы можете создать новую таблицу нажав на кнопку \"Создать новую таблицу\""
        }
        if (language == "UZ") {
            val settingsButton = findViewById<Button>(R.id.SettingsButton)
            settingsButton.text = "Sozlamalar"
            val guideButton = findViewById<Button>(R.id.GuideButton)
            guideButton.text = "Hidoyat"
            val timetablesTitle = findViewById<TextView>(R.id.TimetablesTitle)
            timetablesTitle.text = "Jadvallar"
            val createNewTimetableButton = findViewById<Button>(R.id.CreateNewTimetableButton)
            createNewTimetableButton.text = "Yangi jadval yarating"
            deleteTimetableButton.text = "Jadvalni o'chirish"
            noTimetablesText.text = "Bu yerda hali stol yo'q. \"Yangi jadval yaratish\" tugmasini bosish orqali yangi jadval yaratishingiz mumkin"
        }
    }

    private fun saveData() {
        // what I need to save:
        // timetablesCount
        // timetableNames
        // timetableDates
        // savedSubjectNames
        // savedSubjectTimes
        // savedNumberOfEveryGrade
        // savedLessonsScheduleStartTimes
        // savedLessonsScheduleEndTimes
        // savedLessonsScheduleBrakeTimes
        // savedIsTimetableGenerated
        // savedSubjectsList
        // savedClassroomsList

        // saving timetablesCount
        inputData("timetablesCount", timetablesCount.toString())
        // saving timetableNames
        for (timetable in 0 until timetablesCount) {
            inputData("timetableNames $timetable", timetableNames[timetable])
        }
        // saving timetableDates
        for (timetable in 0 until timetablesCount) {
            inputData("timetableDates $timetable", timetableDates[timetable])
        }
        // saving savedSubjectNames
        for (timetable in 0 until timetablesCount) {
            for (grade in 0 until numberOfGrades) {
                for (subject in 0 until maxNumberOfSubjects) {
                    try {
                        inputData("savedSubjectNames $timetable $grade $subject", savedSubjectNames[timetable][grade][subject])
                    } catch (e: IndexOutOfBoundsException) {
                        inputData("savedSubjectNames $timetable $grade $subject", "")
                    }
                }
            }
        }
        // saving savedSubjectTimes
        for (timetable in 0 until timetablesCount) {
            for (grade in 0 until numberOfGrades) {
                for (subject in 0 until maxNumberOfSubjects) {
                    try {
                        inputData("savedSubjectTimes $timetable $grade $subject", savedSubjectTimes[timetable][grade][subject])
                    } catch (e: IndexOutOfBoundsException) {
                        inputData("savedSubjectTimes $timetable $grade $subject", "")
                    }
                }
            }
        }
        // saving savedNumberOfEveryGrade
        for (timetable in 0 until timetablesCount) {
            for (grade in 0 until numberOfGrades) {
                try {
                    inputData("savedNumberOfEveryGrade $timetable $grade", savedNumberOfEveryGrade[timetable][grade].toString())
                } catch (e: IndexOutOfBoundsException) {
                    inputData("savedNumberOfEveryGrade $timetable $grade", "")
                }
            }
        }
        // saving savedLessonsScheduleStartTimes
        for (timetable in 0 until timetablesCount) {
            for (lesson in 0 until maxNumberOfLessons) {
                try {
                    inputData("savedLessonsScheduleStartTimes $timetable $lesson", savedLessonsScheduleStartTimes[timetable][lesson])
                } catch (e: IndexOutOfBoundsException) {
                    inputData("savedLessonsScheduleStartTimes $timetable $lesson", "")
                }
            }
        }
        // saving savedLessonsScheduleEndTimes
        for (timetable in 0 until timetablesCount) {
            for (lesson in 0 until maxNumberOfLessons) {
                try {
                    inputData("savedLessonsScheduleEndTimes $timetable $lesson", savedLessonsScheduleEndTimes[timetable][lesson])
                } catch (e: IndexOutOfBoundsException) {
                    inputData("savedLessonsScheduleEndTimes $timetable $lesson", "")
                }
            }
        }
        // saving savedLessonsScheduleBrakeTimes
        for (timetable in 0 until timetablesCount) {
            for (lesson in 0 until maxNumberOfLessons) {
                try {
                    inputData("savedLessonsScheduleBrakeTimes $timetable $lesson", savedLessonsScheduleBrakeTimes[timetable][lesson])
                } catch (e: IndexOutOfBoundsException) {
                    inputData("savedLessonsScheduleBrakeTimes $timetable $lesson", "")
                }
            }
        }
        // saving savedIsTimetableGenerated
        for (timetable in 0 until timetablesCount) {
            try {
                inputData("savedIsTimetableGenerated $timetable", savedIsTimetableGenerated[timetable].toString())
            } catch (e: IndexOutOfBoundsException) {
                inputData("savedIsTimetableGenerated $timetable", false.toString())
            }
        }
        // saving savedSubjectsList
        for (timetable in 0 until timetablesCount) {
            for (week_day in 0 until daysInWeek) {
                for (lesson in 0 until maxNumberOfLessons) {
                    for (grade in 0 until numberOfGrades) {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            inputData(
                                "savedSubjectsList $timetable $week_day $lesson $grade $grade_letter",
                                savedSubjectsList[timetable][week_day][lesson][grade][grade_letter]
                            )
                        }
                    }
                }
            }
        }
        // saving savedClassroomsList
        for (timetable in 0 until timetablesCount) {
            for (week_day in 0 until daysInWeek) {
                for (lesson in 0 until maxNumberOfLessons) {
                    for (grade in 0 until numberOfGrades) {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            inputData(
                                "savedClassroomsList $timetable $week_day $lesson $grade $grade_letter",
                                savedClassroomsList[timetable][week_day][lesson][grade][grade_letter]
                            )
                        }
                    }
                }
            }
        }
        Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
    }

    private fun inputData(fileName: String, dataToSave: String) {
        try {
            val fileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
            fileOutputStream.write(dataToSave.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun outputData(fileName: String, whatToReturnIfSomethingGoesWrong: String): String {
        if (fileName.trim() != "") {
            try {
                val fileInputStream = openFileInput(fileName)
                val inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var text: String?
                while (run {
                        text = bufferedReader.readLine()
                        text
                    } != null) {
                    stringBuilder.append(text)
                }
                fileInputStream.close()
                return stringBuilder.toString()
            } catch (e: FileNotFoundException) {
                return whatToReturnIfSomethingGoesWrong
            }
        } else {
            return whatToReturnIfSomethingGoesWrong
        }
    }

    private fun loadData() {
        // resetting all before loading
        timetableNames.removeAll(timetableNames)
        timetableDates.removeAll(timetableDates)
        savedSubjectNames.removeAll(savedSubjectNames)
        savedSubjectTimes.removeAll(savedSubjectTimes)
        savedNumberOfEveryGrade.removeAll(savedNumberOfEveryGrade)
        savedLessonsScheduleStartTimes.removeAll(savedLessonsScheduleStartTimes)
        savedLessonsScheduleEndTimes.removeAll(savedLessonsScheduleEndTimes)
        savedLessonsScheduleBrakeTimes.removeAll(savedLessonsScheduleBrakeTimes)
        savedIsTimetableGenerated.removeAll(savedIsTimetableGenerated)
        savedSubjectsList.removeAll(savedSubjectsList)
        savedClassroomsList.removeAll(savedClassroomsList)

        timetablesCount = outputData("timetablesCount", "0").toInt()
        // loading timetableNames
        for (timetable in 0 until timetablesCount) {
            timetableNames.add(outputData("timetableNames $timetable", ""))
        }
        // loading timetableDates
        for (timetable in 0 until timetablesCount) {
            timetableDates.add(outputData("timetableDates $timetable", ""))
        }
        // loading savedSubjectNames
        for (timetable in 0 until timetablesCount) {
            savedSubjectNames.add(mutableListOf())
            for (grade in 0 until numberOfGrades) {
                savedSubjectNames[timetable].add(mutableListOf())
                for (lesson in 0 until maxNumberOfSubjects) {
                    savedSubjectNames[timetable][grade].add(
                        outputData("savedSubjectNames $timetable $grade $lesson", "")
                    )
                }
            }
        }
        // loading savedSubjectTimes
        for (timetable in 0 until timetablesCount) {
            savedSubjectTimes.add(mutableListOf())
            for (grade in 0 until numberOfGrades) {
                savedSubjectTimes[timetable].add(mutableListOf())
                for (lesson in 0 until maxNumberOfSubjects) {
                    savedSubjectTimes[timetable][grade].add(
                        outputData("savedSubjectTimes $timetable $grade $lesson", "")
                    )
                }
            }
        }
        // loading savedNumberOfEveryGrade
        for (timetable in 0 until timetablesCount) {
            savedNumberOfEveryGrade.add(mutableListOf())
            for (grade in 0 until numberOfGrades) {
                savedNumberOfEveryGrade[timetable].add(
                    outputData("savedNumberOfEveryGrade $timetable $grade", "1").toInt()
                )
            }
        }
        // loading savedLessonsScheduleStartTimes
        for (timetable in 0 until timetablesCount) {
            savedLessonsScheduleStartTimes.add(mutableListOf())
            for (lesson in 0 until maxNumberOfLessons) {
                try {
                    savedLessonsScheduleStartTimes[timetable].add(
                        outputData("savedLessonsScheduleStartTimes $timetable $lesson", "")
                    )
                } catch (e: IndexOutOfBoundsException) {
                    savedLessonsScheduleStartTimes[timetable].add(
                        outputData("savedLessonsScheduleStartTimes $timetable $lesson", "")
                    )
                }
            }
        }
        // loading savedLessonsScheduleEndTimes
        for (timetable in 0 until timetablesCount) {
            savedLessonsScheduleEndTimes.add(mutableListOf())
            for (lesson in 0 until maxNumberOfLessons) {
                try {
                    savedLessonsScheduleEndTimes[timetable].add(
                        outputData("savedLessonsScheduleEndTimes $timetable $lesson", "")
                    )
                } catch (e: IndexOutOfBoundsException) {
                    savedLessonsScheduleEndTimes[timetable].add(
                        outputData("savedLessonsScheduleEndTimes $timetable $lesson", "")
                    )
                }
            }
        }
        // loading savedLessonsScheduleBrakeTimes
        for (timetable in 0 until timetablesCount) {
            savedLessonsScheduleBrakeTimes.add(mutableListOf())
            for (lesson in 0 until maxNumberOfLessons) {
                try {
                    savedLessonsScheduleBrakeTimes[timetable].add(
                        outputData("savedLessonsScheduleBrakeTimes $timetable $lesson", "")
                    )
                } catch (e: IndexOutOfBoundsException) {
                    savedLessonsScheduleBrakeTimes[timetable].add(
                        outputData("savedLessonsScheduleBrakeTimes $timetable $lesson", "")
                    )
                }
            }
        }
        // loading savedIsTimetableGenerated
        for (timetable in 0 until timetablesCount) {
            savedIsTimetableGenerated.add(outputData("savedIsTimetableGenerated $timetable", "false").toBoolean())
        }
        // loading savedSubjectsList
        for (timetable in 0 until timetablesCount) {
            savedSubjectsList.add(mutableListOf())
            for (week_day in 0 until daysInWeek) {
                savedSubjectsList[timetable].add(mutableListOf())
                for (lesson in 0 until maxNumberOfLessons) {
                    savedSubjectsList[timetable][week_day].add(mutableListOf())
                    for (grade in 0 until numberOfGrades) {
                        savedSubjectsList[timetable][week_day][lesson].add(mutableListOf())
                        for (grade_letter in 0 until savedNumberOfEveryGrade[timetable][grade]) {
                            savedSubjectsList[timetable][week_day][lesson][grade].add(
                                outputData("savedSubjectsList $timetable $week_day $lesson $grade $grade_letter", "")
                            )
                        }
                    }
                }
            }
        }
        // loading savedClassroomsList
        for (timetable in 0 until timetablesCount) {
            savedClassroomsList.add(mutableListOf())
            for (week_day in 0 until daysInWeek) {
                savedClassroomsList[timetable].add(mutableListOf())
                for (lesson in 0 until maxNumberOfLessons) {
                    savedClassroomsList[timetable][week_day].add(mutableListOf())
                    for (grade in 0 until numberOfGrades) {
                        savedClassroomsList[timetable][week_day][lesson].add(mutableListOf())
                        for (grade_letter in 0 until savedNumberOfEveryGrade[timetable][grade]) {
                            savedClassroomsList[timetable][week_day][lesson][grade].add(
                                outputData("savedClassroomsList $timetable $week_day $lesson $grade $grade_letter", "")
                            )
                        }
                    }
                }
            }
        }
    }
}
