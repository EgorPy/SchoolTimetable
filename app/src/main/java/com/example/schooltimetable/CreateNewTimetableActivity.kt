package com.example.schooltimetable

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException
import java.lang.Integer.parseInt
import java.text.DateFormat.getDateInstance
import java.util.*
import kotlin.NoSuchElementException


// variables for generating timetable
const val maxNumberOfLessons = 12
const val maxNumberOfSubjects = 20
const val numberOfGrades = 11
const val daysInWeek = 6
const val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
const val russianLetters = "АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЫЭЮЯ"
val weekDays = arrayOf(
    "MONDAY      ",
    "TUESDAY     ",
    "WEDNESDAY   ",
    "THURSDAY    ",
    "FRIDAY      ",
    "SATURDAY    "
)
val weekDaysRu = arrayOf(
    "ПОНЕДЕЛЬНИК ",
    "ВТОРНИК     ",
    "СРЕДА       ",
    "ЧЕТВЕРГ     ",
    "ПЯТНИЦА     ",
    "СУББОТА     "
)
val weekDaysUz = arrayOf(
    "DUSHANBA    ",
    "SESHANBA    ",
    "CHORSHANBA  ",
    "PAYSHANBA   ",
    "JUMA        ",
    "SHANBA      "
)
val numberOfEveryGrade = mutableListOf<Int>()
val finalSubjectsNamesViews = mutableListOf<MutableList<EditText>>()
val finalSubjectsTimesViews = mutableListOf<MutableList<EditText>>()
val classroomViewsList = mutableListOf<MutableList<MutableList<MutableList<EditText>>>>()

//                  ^daysInWeek   ^maxNumberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of TextViews with names of classrooms
val subjectViewsList = mutableListOf<MutableList<MutableList<MutableList<TextView>>>>()

//                  ^daysInWeek   ^maxNumberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of TextViews with names of subjects
val classroomsList = mutableListOf<MutableList<MutableList<MutableList<String>>>>()

//                  ^daysInWeek   ^maxNumberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of Strings with names of classrooms
val subjectsList = mutableListOf<MutableList<MutableList<MutableList<String>>>>()
//                 ^daysInWeek   ^maxNUmberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of Strings with names of subjects

var selectedClassGrade = 0 // index // index of grade
var selectedClassLetter = 0 // also index // index of grade letter

class CreateNewTimetableActivity : AppCompatActivity() {

//    val canvasImage = findViewById<ImageView>(R.id.CanvasImage)

    @SuppressLint("DiscouragedPrivateApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_timetable)

        if (!loadTimetable) {
            // resetting global variables
            numberOfEveryGrade.removeAll(numberOfEveryGrade)
            finalSubjectsNamesViews.removeAll(finalSubjectsNamesViews)
            finalSubjectsTimesViews.removeAll(finalSubjectsTimesViews)
            classroomsList.removeAll(classroomsList)
            subjectsList.removeAll(subjectsList)

            selectedClassGrade = 0
            selectedClassLetter = 0

            // initialising variables

            // lists
            val subjectsLayouts = mutableListOf<LinearLayout>()
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout2))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout3))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout4))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout5))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout6))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout7))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout8))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout9))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout10))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout11))
            val addSubjectButtons = mutableListOf<Button>()
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton2))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton3))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton4))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton5))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton6))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton7))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton8))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton9))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton10))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton11))
            val removeSubjectButtons = mutableListOf<Button>()
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton2))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton3))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton4))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton5))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton6))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton7))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton8))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton9))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton10))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton11))

            val finalTeacherSubjectTimesViews = mutableListOf<MutableList<TextView>>()
            val verticalLayoutsForTeacherNameAndTime = mutableListOf<MutableList<LinearLayout>>()
            //                                           ^ subject    ^ list of vertical layouts that contains layouts with teacher name and time

            val gradesEditTexts = mutableListOf<EditText>()
            gradesEditTexts.add(findViewById(R.id.editTextGrade))
            gradesEditTexts.add(findViewById(R.id.editTextGrade2))
            gradesEditTexts.add(findViewById(R.id.editTextGrade3))
            gradesEditTexts.add(findViewById(R.id.editTextGrade4))
            gradesEditTexts.add(findViewById(R.id.editTextGrade5))
            gradesEditTexts.add(findViewById(R.id.editTextGrade6))
            gradesEditTexts.add(findViewById(R.id.editTextGrade7))
            gradesEditTexts.add(findViewById(R.id.editTextGrade8))
            gradesEditTexts.add(findViewById(R.id.editTextGrade9))
            gradesEditTexts.add(findViewById(R.id.editTextGrade10))
            gradesEditTexts.add(findViewById(R.id.editTextGrade11))

            val lessonsScheduleStartTimes = mutableListOf<EditText>()
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime1))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime2))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime3))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime4))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime5))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime6))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime7))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime8))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime9))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime10))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime11))
            val lessonsScheduleEndTimes = mutableListOf<EditText>()
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime1))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime2))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime3))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime4))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime5))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime6))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime7))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime8))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime9))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime10))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime11))
            val lessonsScheduleBrakeTimes = mutableListOf<EditText>()
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime1))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime2))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime3))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime4))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime5))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime6))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime7))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime8))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime9))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime10))

            // buttons
            val gradesButton = findViewById<Button>(R.id.GradesButton)
            val enterSubjectsButton = findViewById<Button>(R.id.EnterSubjectsButton)
            val generateTimetableButton = findViewById<Button>(R.id.GenerateTimetableButton)
            val saveTimetableButton = findViewById<Button>(R.id.SaveTimetableButton)

            // other
            val timetableLayout = findViewById<LinearLayout>(R.id.TimetableLayout)
            val teachersVerticalLayout = findViewById<LinearLayout>(R.id.TeachersVerticalLayout)
            val timetableNameEditText = findViewById<EditText>(R.id.TimetableNameEditText)

            var isTimetableGenerated = false

            val prevClassButton = findViewById<Button>(R.id.PrevClassButton)
            val nextClassButton = findViewById<Button>(R.id.NextClassButton)
            val takeScreenshotButton = findViewById<Button>(R.id.TakeScreenshotButton)
            val classText = findViewById<TextView>(R.id.ClassText)

            val popupMenu = PopupMenu(applicationContext, gradesButton)

            // changing variables properties

            // setting some of the layouts invisible
            for (subjectLayout in subjectsLayouts) {
                if (subjectsLayouts.indexOf(subjectLayout) != 0) {
                    subjectLayout.visibility = LinearLayout.GONE
                }
            }
            // setting colors to normal
            for (addSubjectButton in addSubjectButtons) {
                addSubjectButton.setBackgroundColor(Color.parseColor("#2196F3"))
                addSubjectButton.setTextColor(Color.parseColor("#FFFFFF"))
            }
            for (removeSubjectButton in removeSubjectButtons) {
                removeSubjectButton.setBackgroundColor(Color.parseColor("#2196F3"))
                removeSubjectButton.setTextColor(Color.parseColor("#FFFFFF"))
            }


            // code

            // changing grades
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.grade1 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 1", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 1"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 1", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 1"
                        } else {
                            Toast.makeText(applicationContext, "Grade 1", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 1"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 0) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[0].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade2 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 2", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 2"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 2", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 2"
                        } else {
                            Toast.makeText(applicationContext, "Grade 2", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 2"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 1) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[1].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade3 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 3", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 3"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 3", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 3"
                        } else {
                            Toast.makeText(applicationContext, "Grade 3", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 3"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 2) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[2].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade4 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 4", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 4"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 4", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 4"
                        } else {
                            Toast.makeText(applicationContext, "Grade 4", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 4"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 3) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[3].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade5 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 5", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 5"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 5", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 5"
                        } else {
                            Toast.makeText(applicationContext, "Grade 5", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 5"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 4) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[4].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade6 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 6", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 6"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 6", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 6"
                        } else {
                            Toast.makeText(applicationContext, "Grade 6", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 6"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 5) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[5].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade7 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 7", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 7"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 7", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 7"
                        } else {
                            Toast.makeText(applicationContext, "Grade 7", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 7"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 6) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[6].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade8 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 8", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 8"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 8", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 8"
                        } else {
                            Toast.makeText(applicationContext, "Grade 8", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 8"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 7) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[7].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade9 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 9", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 9"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 9", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 9"
                        } else {
                            Toast.makeText(applicationContext, "Grade 9", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 9"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 8) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[8].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade10 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 10", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 10"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 10", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 10"
                        } else {
                            Toast.makeText(applicationContext, "Grade 10", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 10"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 9) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[9].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade11 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 11", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 11"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 11", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 11"
                        } else {
                            Toast.makeText(applicationContext, "Grade 11", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 11"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 10) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[10].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    else -> true
                }
            }
            gradesButton.setOnClickListener {
                try {
                    val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                    popup.isAccessible = true
                    val menu = popup.get(popupMenu)
                    menu.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(menu, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    popupMenu.show()
                }
            }
//            pass
            // adding/removing subjects
            for (addSubjectButton in addSubjectButtons) {
                addSubjectButton.setOnClickListener {
                    try {
                        val horScroll = HorizontalScrollView(this)

                        val linearLayout = LinearLayout(this)
                        linearLayout.layoutParams =
                            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        linearLayout.orientation = LinearLayout.HORIZONTAL
                        // программно создаем новый виджет(View) в данном случае это EditText
                        val editSubjectName = EditText(this)
                        editSubjectName.layoutParams = LinearLayout.LayoutParams(670, 200)
                        if (language == "UZ") {
                            editSubjectName.hint = "Element nomi"
                        } else if (language == "RU") {
                            editSubjectName.hint = "Название предмета"
                        } else {
                            editSubjectName.hint = "Subject name"
                        }
                        editSubjectName.inputType = InputType.TYPE_CLASS_TEXT
                        editSubjectName.setTextColor(Color.parseColor("#000000"))
                        editSubjectName.setHintTextColor(Color.parseColor("#555555"))
                        editSubjectName.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                        var filterArray = arrayOfNulls<InputFilter>(1)
                        filterArray[0] = LengthFilter(13)
                        editSubjectName.filters = filterArray
                        val editSubjectNumberInWeek = EditText(this)
                        editSubjectNumberInWeek.layoutParams = LinearLayout.LayoutParams(670, 200)
                        if (language == "UZ") {
                            editSubjectNumberInWeek.hint = "Haftada necha marta"
                        } else if (language == "RU") {
                            editSubjectNumberInWeek.hint = "Сколько раз в неделю"
                        } else {
                            editSubjectNumberInWeek.hint = "How many times a week"
                        }
                        editSubjectNumberInWeek.inputType = InputType.TYPE_CLASS_NUMBER
                        editSubjectNumberInWeek.setTextColor(Color.parseColor("#000000"))
                        editSubjectNumberInWeek.setHintTextColor(Color.parseColor("#555555"))
                        editSubjectNumberInWeek.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                        filterArray = arrayOfNulls(1)
                        filterArray[0] = LengthFilter(1)
                        editSubjectNumberInWeek.filters = filterArray

                        // добавляем виджет в layout
                        linearLayout.addView(editSubjectName)

                        finalSubjectsNamesViews.add(mutableListOf())
                        finalSubjectsNamesViews[addSubjectButtons.indexOf(addSubjectButton)].add(editSubjectName)
                        finalSubjectsTimesViews.add(mutableListOf())
                        finalSubjectsTimesViews[addSubjectButtons.indexOf(addSubjectButton)].add(editSubjectNumberInWeek)

                        linearLayout.addView(editSubjectNumberInWeek)
                        horScroll.addView(linearLayout)

                        subjectsLayouts[addSubjectButtons.indexOf(addSubjectButton)].addView(horScroll)
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()

                        if (language == "UZ") {
                            Toast.makeText(this, "Barcha maydonlarni ketma-ket to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполняйте все поля последовательно", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information sequentially", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            for (removeSubjectButton in removeSubjectButtons) {
                removeSubjectButton.setOnClickListener {
                    if (subjectsLayouts[removeSubjectButtons.indexOf(removeSubjectButton)].childCount > 1) {
                        subjectsLayouts[removeSubjectButtons.indexOf(removeSubjectButton)].removeViewAt(
                            subjectsLayouts[removeSubjectButtons.indexOf(removeSubjectButton)].childCount - 1
                        )
                        finalSubjectsNamesViews[removeSubjectButtons.indexOf(removeSubjectButton)].removeLast()
                        finalSubjectsTimesViews[removeSubjectButtons.indexOf(removeSubjectButton)].removeLast()
                    }
                }
            }

            // entering teachers and seeing how much lessons they should teach)))
            enterSubjectsButton.setOnClickListener {
                // ENTERING TEACHERS

                // resetting all
                teachersVerticalLayout.removeAllViews()
                verticalLayoutsForTeacherNameAndTime.removeAll(verticalLayoutsForTeacherNameAndTime)
                finalTeacherSubjectTimesViews.removeAll(finalTeacherSubjectTimesViews)

                // надо посчитать сколько ВСЕГО каждого урока
                val names = mutableListOf<String>()
                val times = mutableListOf<Int>()

                for (grade in finalSubjectsNamesViews) {
                    for (j in grade) {
                        names.add(j.text.toString())
                    }
                }
                for (grade in finalSubjectsTimesViews) {
                    for (j in grade) {
                        if (j.text.toString() != "") {
                            try {
                                times.add(j.text.toString().toInt())
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                            }
                        } else {
                            times.add(1)
                        }
                    }
                }
                val listOfUsed = mutableListOf<String>()
                val sortedList = mutableListOf<String>()
                val sumOfEverySubject = mutableListOf<Int>()

                // добавляем в список sortedList названия предметов
                for (i in 0 until names.size) {
                    if (names[i].uppercase() !in listOfUsed) {
                        sortedList.add(names[i].uppercase())
                        listOfUsed.add(names[i].uppercase())
                    }
                }

                // теперь проходим по каждому элементу sortedList и если элемент
                // списка names будет равнятся элементу, то заносим его в finalList
                for (j in sortedList) {
                    var c = 0
                    for (i in 0 until names.size) {
                        if (names[i].uppercase() == j) {
                            try {
                                c += times[i]
                            } catch (e: IndexOutOfBoundsException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    sumOfEverySubject.add(c)
                }

                // генерация лэяутов с виджетами
                for (i in listOfUsed) {
                    // generating layout for subject name TextView
                    val subjectNameLayout = LinearLayout(this)
                    subjectNameLayout.layoutParams =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    val subjectNameText = TextView(this)
                    subjectNameText.text = i
                    subjectNameText.setTextColor(Color.WHITE)
                    subjectNameText.textSize = 20F
                    // changing text style to bold
                    subjectNameText.typeface = Typeface.DEFAULT_BOLD

                    subjectNameText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    subjectNameText.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    val param: LinearLayout.LayoutParams =
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    param.weight = 1F
                    subjectNameText.layoutParams = param

                    subjectNameLayout.addView(subjectNameText)

                    // generating layout for add/remove teacher buttons
                    val teacherButtonsLayout = LinearLayout(this)
                    teacherButtonsLayout.layoutParams =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    val addTeacherButton = Button(this)
                    addTeacherButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                    if (language == "UZ") {
                        addTeacherButton.text = "o'qituvchi qo'shing"
                    } else if (language == "RU") {
                        addTeacherButton.text = "добавить учителя"
                    } else {
                        addTeacherButton.text = "add teacher"
                    }
                    addTeacherButton.setTextColor(Color.WHITE)
                    val addTeacherButtonParam = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        .also { it.setMargins(10, 10, 10, 10) }
                    addTeacherButton.layoutParams = addTeacherButtonParam
                    addTeacherButtonParam.weight = 1F
                    val removeTeacherButton = Button(this)
                    removeTeacherButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                    if (language == "UZ") {
                        removeTeacherButton.text = "o'qituvchini o'chirish"
                    } else if (language == "RU") {
                        removeTeacherButton.text = "удалить учителя"
                    } else {
                        removeTeacherButton.text = "remove teacher"
                    }
                    removeTeacherButton.setTextColor(Color.WHITE)
                    val removeTeacherButtonParam =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                            .also { it.setMargins(10, 10, 10, 10) }
                    removeTeacherButtonParam.weight = 1F
                    removeTeacherButton.layoutParams = removeTeacherButtonParam

                    // generating layout for teacher name and how much times he teaches subjects
                    val verticalLayoutForTeacherNameAndTime = LinearLayout(this)
                    verticalLayoutForTeacherNameAndTime.orientation = LinearLayout.VERTICAL
                    verticalLayoutForTeacherNameAndTime.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    finalTeacherSubjectTimesViews.add(mutableListOf())

                    addTeacherButton.setOnClickListener {
                        verticalLayoutsForTeacherNameAndTime.add(mutableListOf())

                        val teacherNameTimeLayout = LinearLayout(this)
                        teacherNameTimeLayout.layoutParams =
                            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                        try {
                            verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].add(verticalLayoutForTeacherNameAndTime)
                        } catch (e: java.lang.IndexOutOfBoundsException) {
                            e.printStackTrace()
                        }

                        val teacherNameEditText = EditText(this)
                        if (language == "UZ") {
                            teacherNameEditText.hint = "O'qituvchining ismi"
                        } else if (language == "RU") {
                            teacherNameEditText.hint = "Имя учителя"
                        } else {
                            teacherNameEditText.hint = "Teacher's name"
                        }
                        teacherNameEditText.textSize = 20F
                        teacherNameEditText.setTextColor(Color.BLACK)
                        teacherNameEditText.setHintTextColor(Color.BLACK)
                        val teacherNameEditTextParam =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        teacherNameEditTextParam.weight = 1F
                        teacherNameEditText.layoutParams = teacherNameEditTextParam
                        val teacherSubjectTimesText = TextView(this)

                        val finalTimesList = mutableListOf<Int>()
                        while (true) {
                            // generating float list for every teacher
                            val tempTimesList = mutableListOf<Float>()
                            for (e in 0 until verticalLayoutForTeacherNameAndTime.childCount + 1) {
                                tempTimesList.add((sumOfEverySubject[listOfUsed.indexOf(i)].toFloat() / (verticalLayoutForTeacherNameAndTime.childCount.toFloat() + 1F)))
                            }
                            // converting float elements from that list to int elements
                            for (e in tempTimesList) {
                                finalTimesList.add(roundNumber(e))
                            }
                            var sum = 0
                            for (e in finalTimesList) {
                                sum += e
                            }
                            for (e in 0 until finalTimesList.size) {
                                if (sum > sumOfEverySubject[listOfUsed.indexOf(i)]) {
                                    finalTimesList[e] -= 1
                                    sum -= 1
                                } else if (sum < sumOfEverySubject[listOfUsed.indexOf(i)]) {
                                    finalTimesList[e] += 1
                                    sum += 1
                                }
                            }
                            if (sum == sumOfEverySubject[listOfUsed.indexOf(i)]) {
                                break
                            }
                        }

                        teacherSubjectTimesText.textSize = 20F
                        teacherSubjectTimesText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        teacherSubjectTimesText.setTextColor(Color.BLACK)
                        val teacherSubjectTimesTextParam =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        teacherSubjectTimesTextParam.weight = 1F
                        teacherSubjectTimesText.layoutParams = teacherSubjectTimesTextParam

                        teacherNameTimeLayout.addView(teacherNameEditText)
                        teacherNameTimeLayout.addView(teacherSubjectTimesText)

                        verticalLayoutForTeacherNameAndTime.addView(teacherNameTimeLayout)

                        finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)].add(teacherSubjectTimesText)

                        for (j in 0 until finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)].size) {
                            try {
                                finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)][j].text =
                                    finalTimesList[j].toString()
                            } catch (e: IndexOutOfBoundsException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    removeTeacherButton.setOnClickListener {
                        if (verticalLayoutsForTeacherNameAndTime.size > 0) {
                            try {
                                if (verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].last().childCount > 0) {
                                    verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].last()
                                        .removeViewAt(verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].last().childCount - 1)
                                    verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].removeLast()
                                    finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)].removeLast()
                                }
                            } catch (e: NoSuchElementException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    teacherButtonsLayout.addView(addTeacherButton)
                    teacherButtonsLayout.addView(removeTeacherButton)

                    teachersVerticalLayout.addView(subjectNameLayout)
                    teachersVerticalLayout.addView(teacherButtonsLayout)
                    teachersVerticalLayout.addView(verticalLayoutForTeacherNameAndTime)
                }
            }

            // generating timetable
            generateTimetableButton.setOnClickListener {

                // resetting all
                timetableLayout.removeAllViews()
                numberOfEveryGrade.removeAll(numberOfEveryGrade)

                // adding number of every grade from grades edit texts to numberOfEveryGrade list
                try {
                    for (i in gradesEditTexts) {
                        if (i.text.toString() != "") {
                            numberOfEveryGrade.add(parseInt(i.text.toString()))
                        } else {
                            numberOfEveryGrade.add(1)
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                val timetable = TableLayout(this)
                timetable.setBackgroundColor(Color.LTGRAY)

                val yInfoLayout = LinearLayout(this)
                yInfoLayout.orientation = LinearLayout.VERTICAL
                yInfoLayout.setPadding(0, 0, 0, 0)
                for (i in 0 until 2) {
                    val paddingText = TextView(this)
                    paddingText.textSize = 14F
                    paddingText.width = 150
                    yInfoLayout.addView(paddingText)
                }
                // generating y info
                for (i in 0 until daysInWeek) {
                    for (j in 0 until (maxNumberOfLessons + 1)) {
                        try {
                            val layout = LinearLayout(this)
                            val text1 = TextView(this)
                            if (language == "UZ") {
                                text1.text = weekDaysUz[i][j].toString()
                            } else if (language == "RU") {
                                text1.text = weekDaysRu[i][j].toString()
                            } else {
                                text1.text = weekDays[i][j].toString()
                            }
                            text1.width = 75
                            text1.setTextColor(Color.BLACK)
//                        text1.height = 104
                            text1.textSize = 17.5F
                            text1.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            val text2 = TextView(this)
                            text2.text = (j + 1).toString()
                            text2.width = 75
                            text2.setTextColor(Color.BLACK)
//                        text2.height = 104
                            text2.textSize = 17.5F
                            text2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            layout.addView(text1)
                            layout.addView(text2)
                            yInfoLayout.addView(layout)
                        } catch (e: StringIndexOutOfBoundsException) {
                            val layout = LinearLayout(this)
                            val text1 = TextView(this)
                            text1.textSize = 17.5F
                            text1.setTextColor(Color.BLACK)
                            layout.addView(text1)
                            yInfoLayout.addView(layout)
                        }
                    }
                }

                val timetableList = mutableListOf<MutableList<MutableList<MutableList<EditText>>>>()
                //                  ^daysInWeek   ^maxNumberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of TextViews with names of subjects

                // generating grades row
                val row = TableRow(this)
                row.setPadding(0, 0, 0, 0)
                for (grade in 0 until numberOfGrades) {
                    try {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            try {
                                val element = TextView(this)
                                element.width = 600
                                if (language == "RU") {
                                    element.text = "${grade + 1}${russianLetters[grade_letter]}"
                                } else {
                                    element.text = "${grade + 1}${letters[grade_letter]}"
                                }
                                element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element.textSize = 14F
                                element.setTextColor(Color.BLACK)
                                row.addView(element)
                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
//                    e.printStackTrace()
                        if (language == "UZ") {
                            Toast.makeText(this, "Jadvalni yaratish uchun oldingi barcha maydonlarni to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполните все предыдущие поля чтобы сгенерировать таблицу", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information to generate timetable", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                timetable.addView(row)

                // generating subject|classroom row
                val subjectClassroomRow = TableRow(this)
                subjectClassroomRow.setPadding(0, 0, 0, 0)
                for (grade in 0 until numberOfGrades) {
                    try {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            try {
                                val layout = LinearLayout(this)
//                            layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                                val element = TextView(this)
                                element.width = 400
                                if (language == "UZ") {
                                    element.text = "Element"
                                } else if (language == "RU") {
                                    element.text = "Предмет"
                                } else {
                                    element.text = "Subject"
                                }
                                element.setTextColor(Color.BLACK)
                                element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element.textSize = 14F

                                val element2 = TextView(this)
                                element2.width = 200
                                if (language == "UZ") {
                                    element2.text = "Kabinet"
                                } else if (language == "RU") {
                                    element2.text = "Кабинет"
                                } else {
                                    element2.text = "Classroom"
                                }
                                element2.setTextColor(Color.BLACK)
                                element2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element2.textSize = 13F

                                layout.addView(element)
                                layout.addView(element2)

                                subjectClassroomRow.addView(layout)

                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
//                    e.printStackTrace()

                        if (language == "UZ") {
                            Toast.makeText(this, "Jadvalni yaratish uchun oldingi barcha maydonlarni to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполните все предыдущие поля чтобы сгенерировать таблицу", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information to generate timetable", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                timetable.addView(subjectClassroomRow)


                // generating timetable
                classroomViewsList.removeAll(classroomViewsList)
                subjectViewsList.removeAll(subjectViewsList)
                for (week_day in 0 until daysInWeek) { // this is for y
                    timetableList.add(mutableListOf())
                    classroomViewsList.add(mutableListOf())
                    subjectViewsList.add(mutableListOf())
                    for (lesson in 0 until maxNumberOfLessons) { // this is also for y
                        timetableList[week_day].add(mutableListOf())
                        classroomViewsList[week_day].add(mutableListOf())
                        subjectViewsList[week_day].add(mutableListOf())
                        val timetableRow = TableRow(this)
                        timetableRow.setPadding(0, 0, 0, 0)
                        timetableRow.setBackgroundColor(Color.parseColor("#EEEEEE"))
                        for (grade in 0 until numberOfGrades) { // this is for x
                            timetableList[week_day][lesson].add(mutableListOf())
                            classroomViewsList[week_day][lesson].add(mutableListOf())
                            subjectViewsList[week_day][lesson].add(mutableListOf())
                            try {
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) { // this is also for x
                                    val layout = LinearLayout(this)

                                    val element = EditText(this)
                                    element.width = 400
                                    element.setText("$week_day, $lesson, $grade, $grade_letter")
                                    element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                    element.setBackgroundResource(android.R.color.transparent)
                                    element.setPadding(0, 0, 0, 0)
                                    val filterArray = arrayOfNulls<InputFilter>(1)
                                    filterArray[0] = LengthFilter(13)
                                    element.filters = filterArray
                                    element.textSize = 13F
                                    element.setTextColor(Color.BLACK)

                                    val element2 = EditText(this)
                                    element2.width = 200
                                    element2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                    element2.setBackgroundResource(android.R.color.transparent)
                                    element2.background = Color.WHITE.toDrawable()
                                    element2.setPadding(0, 0, 0, 0)
                                    val filterArray2 = arrayOfNulls<InputFilter>(1)
                                    filterArray2[0] = LengthFilter(4)
                                    element2.filters = filterArray2
                                    element2.setTextColor(Color.BLACK)

                                    layout.addView(element)
                                    layout.addView(element2)

                                    timetableList[week_day][lesson][grade].add(element)
                                    classroomViewsList[week_day][lesson][grade].add(element2)
                                    subjectViewsList[week_day][lesson][grade].add(element)
                                    timetableRow.addView(layout)
                                }
                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }

                        timetable.addView(timetableRow)
                        // this is for padding between week days
                        if (lesson == maxNumberOfLessons - 1) {
                            val l = LinearLayout(this)
                            val t = TextView(this)
                            t.textSize = 14F
                            t.setTextColor(Color.BLACK)
                            l.addView(t)
                            timetable.addView(l)
                        }
                    }
                }

                timetableLayout.addView(yInfoLayout)
                timetableLayout.addView(timetable)

                // putting information in the timetable
                for (week_day in 0 until daysInWeek) { // this is for y
                    for (lesson in 0 until maxNumberOfLessons) { // this is also for y
                        for (grade in 0 until numberOfGrades) { // this is for x
                            for (grade_letter in 0 until numberOfEveryGrade[grade]) { // this is also for x
                                try {
                                    if (myInt(finalSubjectsTimesViews[grade][lesson].text.toString()) > week_day) {
                                        timetableList[week_day][lesson][grade][grade_letter].setText(
                                            finalSubjectsNamesViews[grade][lesson].text.toString()
                                        )
                                    } else {
                                        timetableList[week_day][lesson][grade][grade_letter].setText("")
                                    }
                                } catch (e: IndexOutOfBoundsException) {
                                    timetableList[week_day][lesson][grade][grade_letter].setText("")
                                }
                            }
                        }
                    }
                }

                isTimetableGenerated = true
            }

            // button to select next class
            classText.text = "${selectedClassGrade + 1}${letters[selectedClassLetter]}"
            nextClassButton.setOnClickListener {
                if (selectedClassLetter + 1 < numberOfEveryGrade[selectedClassGrade]) {
                    selectedClassLetter += 1
                } else {
                    selectedClassLetter = 0
                    if (selectedClassGrade < 10) {
                        selectedClassGrade += 1
                    }
                }

                classText.text = "${selectedClassGrade + 1}${letters[selectedClassLetter]}"
            }
            // button to select prev class
            prevClassButton.setOnClickListener {
                if (selectedClassLetter > 0) {
                    selectedClassLetter -= 1
                } else {
                    if (selectedClassGrade > 0) {
                        selectedClassGrade -= 1
                    }
                }

                classText.text = "${selectedClassGrade + 1}${letters[selectedClassLetter]}"
            }
            // let user take screenshot of the timetable
            takeScreenshotButton.setOnClickListener {
                classroomsList.removeAll(classroomsList)
                subjectsList.removeAll(subjectsList)

                for (week_day in 0 until daysInWeek) {
                    classroomsList.add(mutableListOf())
                    for (lesson in 0 until maxNumberOfLessons) {
                        classroomsList[week_day].add(mutableListOf())
                        for (grade in 0 until numberOfGrades) {
                            classroomsList[week_day][lesson].add(mutableListOf())
                            for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                classroomsList[week_day][lesson][grade].add(classroomViewsList[week_day][lesson][grade][grade_letter].text.toString())
                            }
                        }
                    }
                }
                for (week_day in 0 until daysInWeek) {
                    subjectsList.add(mutableListOf())
                    for (lesson in 0 until maxNumberOfLessons) {
                        subjectsList[week_day].add(mutableListOf())
                        for (grade in 0 until numberOfGrades) {
                            subjectsList[week_day][lesson].add(mutableListOf())
                            for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                subjectsList[week_day][lesson][grade].add(subjectViewsList[week_day][lesson][grade][grade_letter].text.toString())
                            }
                        }
                    }
                }

                val intent = Intent(this, MyCanvasActivity::class.java)
                startActivity(intent)
            }

            // SAVING DATA
            saveTimetableButton.setOnClickListener {
                saveTimetable = true

                var timetableName = "Timetable $timetablesCount"
                if (timetableNameEditText.text.toString() != "") {
                    timetableName = timetableNameEditText.text.toString()
                }
                if (timetableNameEditText.text.toString() in timetableNames) {
                    saveTimetable = false
                    Toast.makeText(this, "This name already exists", Toast.LENGTH_SHORT).show()
                }

                if (saveTimetable) {
                    // when I get here, I need to save all the information

                    // resetting lists
                    numberOfEveryGrade.removeAll(numberOfEveryGrade)
                    // adding number of every grade from grades edit texts to numberOfEveryGrade list
                    try {
                        for (i in gradesEditTexts) {
                            if (i.text.toString() != "") {
                                numberOfEveryGrade.add(parseInt(i.text.toString()))
                            } else {
                                numberOfEveryGrade.add(1)
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }

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

                    timetablesCount += 1
                    // getting current time in readable format
                    val sdf = getDateInstance()
                    val timetableDate = sdf.format(Date())
                    timetableNames.add(timetableName)
                    timetableDates.add(timetableDate)
                    savedSubjectNames.add(mutableListOf())
                    savedSubjectTimes.add(mutableListOf())
                    savedNumberOfEveryGrade.add(mutableListOf())
                    savedLessonsScheduleStartTimes.add(mutableListOf())
                    savedLessonsScheduleEndTimes.add(mutableListOf())
                    savedLessonsScheduleBrakeTimes.add(mutableListOf())
                    savedIsTimetableGenerated.add(isTimetableGenerated)
                    savedSubjectsList.add(mutableListOf())
                    savedClassroomsList.add(mutableListOf())

                    // saving subject names
                    for (grade in 0 until numberOfGrades) {
                        savedSubjectNames[timetablesCount - 1].add(mutableListOf())
                        for (subject in 0 until maxNumberOfSubjects) {
                            try {
                                savedSubjectNames[timetablesCount - 1][grade].add(finalSubjectsNamesViews[grade][subject].text.toString())
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                    }

                    // saving subject times
                    for (grade in 0 until numberOfGrades) {
                        savedSubjectTimes[timetablesCount - 1].add(mutableListOf())
                        for (subject in 0 until maxNumberOfSubjects) {
                            try {
                                savedSubjectTimes[timetablesCount - 1][grade].add(finalSubjectsTimesViews[grade][subject].text.toString())
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                    }

                    // saving list of number of every grade
                    for (i in 0 until gradesEditTexts.size) {
                        if (gradesEditTexts[i].text.toString() != "") {
                            savedNumberOfEveryGrade[timetablesCount - 1].add(gradesEditTexts[i].text.toString().toInt())
                        } else {
                            savedNumberOfEveryGrade[timetablesCount - 1].add(1)
                        }
                    }

                    // saving lessons schedule
                    for (i in lessonsScheduleStartTimes) {
                        savedLessonsScheduleStartTimes[timetablesCount - 1].add(i.text.toString())
                    }
                    for (i in lessonsScheduleEndTimes) {
                        savedLessonsScheduleEndTimes[timetablesCount - 1].add(i.text.toString())
                    }
                    for (i in lessonsScheduleBrakeTimes) {
                        savedLessonsScheduleBrakeTimes[timetablesCount - 1].add(i.text.toString())
                    }

                    // updating lists of timetable views information
                    classroomsList.removeAll(classroomsList)
                    subjectsList.removeAll(subjectsList)
                    for (week_day in 0 until daysInWeek) {
                        classroomsList.add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            classroomsList[week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                classroomsList[week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    try {
                                        classroomsList[week_day][lesson][grade].add(classroomViewsList[week_day][lesson][grade][grade_letter].text.toString())
                                    } catch (e: IndexOutOfBoundsException) {
                                        // if I get here, then timetable is not generated
                                        // so I need to put blank Strings into classroomsList
                                        classroomsList[week_day][lesson][grade].add("")
                                    }
                                }
                            }
                        }
                    }
                    for (week_day in 0 until daysInWeek) {
                        subjectsList.add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            subjectsList[week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                subjectsList[week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    try {
                                        subjectsList[week_day][lesson][grade].add(subjectViewsList[week_day][lesson][grade][grade_letter].text.toString())
                                    } catch (e: IndexOutOfBoundsException) {
                                        // if I get here, then timetable is not generated
                                        // so I need to put blank Strings into subjectsList
                                        subjectsList[week_day][lesson][grade].add("")
                                    }
                                }
                            }
                        }
                    }
                    // saving information in all views in current timetable
                    for (week_day in 0 until daysInWeek) {
                        savedSubjectsList[timetablesCount - 1].add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            savedSubjectsList[timetablesCount - 1][week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                savedSubjectsList[timetablesCount - 1][week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    savedSubjectsList[timetablesCount - 1][week_day][lesson][grade].add(subjectsList[week_day][lesson][grade][grade_letter])
                                }
                            }
                        }
                    }
                    for (week_day in 0 until daysInWeek) {
                        savedClassroomsList[timetablesCount - 1].add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            savedClassroomsList[timetablesCount - 1][week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                savedClassroomsList[timetablesCount - 1][week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    savedClassroomsList[timetablesCount - 1][week_day][lesson][grade].add(classroomsList[week_day][lesson][grade][grade_letter])
                                }
                            }
                        }
                    }

                    // returning to main page
                    // closing this page (activity) and returning to MainActivity.kt
                    finish()
                }
            }

            // changing language
            if (language == "RU") {
                val createTimetableTitle = findViewById<TextView>(R.id.CreateTimetableTitle)
                createTimetableTitle.text = "Создать таблицу"
                val subjectsSectionText = findViewById<TextView>(R.id.SubjectsSectionText)
                subjectsSectionText.text = "Предметы"
                gradesButton.text = "Класс 1"
                for (addSubjectButton in addSubjectButtons) {
                    addSubjectButton.text = "Добавить предмет"
                }
                for (removeSubjectButton in removeSubjectButtons) {
                    removeSubjectButton.text = "Удалить предмет"
                }
                enterSubjectsButton.text = "Ввести предметы"
                val numberOfGradesSectionText = findViewById<TextView>(R.id.NumberOfGradesSectionText)
                numberOfGradesSectionText.text = "Число классов"
                for (gradesEditText in gradesEditTexts) {
                    gradesEditText.hint = "класс ${gradesEditTexts.indexOf(gradesEditText) + 1}"
                }
                val teachersSectionText = findViewById<TextView>(R.id.TeachersSectionText)
                teachersSectionText.text = "Учителя"
                val callScheduleSectionText = findViewById<TextView>(R.id.CallScheduleSectionText)
                callScheduleSectionText.text = "Расписание звонков"
                val startOfLessonText = findViewById<TextView>(R.id.StartOfLessonText)
                startOfLessonText.text = "Начало урока"
                val endOfLessonText = findViewById<TextView>(R.id.EndOfLessonText)
                endOfLessonText.text = "Конец урока"
                val brakeTimeText = findViewById<TextView>(R.id.BrakeTimeText)
                brakeTimeText.text = "Перемена"
                val viewTimetableSectionText = findViewById<TextView>(R.id.ViewTimetableSectionText)
                viewTimetableSectionText.text = "Таблица расписания уроков"
                generateTimetableButton.text = "Сгенерировать расписание уроков"
                prevClassButton.text = "Предыдущий"
                nextClassButton.text = "Следующий"
                takeScreenshotButton.text = "Сохранить в галерею"
                val saveTimetableText = findViewById<TextView>(R.id.SaveTimetableText)
                saveTimetableText.text = "Сохранение таблицы"
                timetableNameEditText.hint = "Название таблицы"
                saveTimetableButton.text = "Сохранить таблицу"
            }
            if (language == "UZ") {
                val createTimetableTitle = findViewById<TextView>(R.id.CreateTimetableTitle)
                createTimetableTitle.text = "Jadval yaratish"
                val subjectsSectionText = findViewById<TextView>(R.id.SubjectsSectionText)
                subjectsSectionText.text = "Elementlar"
                gradesButton.text = "1-sinf"
                for (addSubjectButton in addSubjectButtons) {
                    addSubjectButton.text = "Element qo'shish"
                }
                for (removeSubjectButton in removeSubjectButtons) {
                    removeSubjectButton.text = "Elementni o'chirish"
                }
                enterSubjectsButton.text = "Elementlarni kiriting"
                val numberOfGradesSectionText = findViewById<TextView>(R.id.NumberOfGradesSectionText)
                numberOfGradesSectionText.text = "Sinflar soni"
                for (gradesEditText in gradesEditTexts) {
                    gradesEditText.hint = "sinf ${gradesEditTexts.indexOf(gradesEditText) + 1}"
                }
                val teachersSectionText = findViewById<TextView>(R.id.TeachersSectionText)
                teachersSectionText.text = "O'qituvchilar"
                val callScheduleSectionText = findViewById<TextView>(R.id.CallScheduleSectionText)
                callScheduleSectionText.text = "Qo'ng'iroqlar jadvali"
                val startOfLessonText = findViewById<TextView>(R.id.StartOfLessonText)
                startOfLessonText.text = "Dars boshlanishi"
                val endOfLessonText = findViewById<TextView>(R.id.EndOfLessonText)
                endOfLessonText.text = "Darsning oxiri"
                val brakeTimeText = findViewById<TextView>(R.id.BrakeTimeText)
                brakeTimeText.text = "Maktabda o'zgarish"
                val viewTimetableSectionText = findViewById<TextView>(R.id.ViewTimetableSectionText)
                viewTimetableSectionText.text = "Dars jadvali"
                generateTimetableButton.text = "Dars jadvalini tuzing"
                prevClassButton.text = "Oldingi"
                nextClassButton.text = "Keyingi"
                takeScreenshotButton.text = "Galereyaga saqlang"
                val saveTimetableText = findViewById<TextView>(R.id.SaveTimetableText)
                saveTimetableText.text = "Jadvalni saqlash"
                timetableNameEditText.hint = "Jadval nomi"
                saveTimetableButton.text = "Jadvalni saqlash"
            }
        } else {
            loadTimetable = false
            // IF WE TRYING TO LOAD TIMETABLE, THEN

            // resetting global variables
            numberOfEveryGrade.removeAll(numberOfEveryGrade)
            finalSubjectsNamesViews.removeAll(finalSubjectsNamesViews)
            finalSubjectsTimesViews.removeAll(finalSubjectsTimesViews)
            classroomsList.removeAll(classroomsList)
            subjectsList.removeAll(subjectsList)

            selectedClassGrade = 0
            selectedClassLetter = 0

            // initialising variables

            // lists
            val subjectsLayouts = mutableListOf<LinearLayout>()
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout2))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout3))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout4))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout5))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout6))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout7))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout8))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout9))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout10))
            subjectsLayouts.add(findViewById(R.id.SubjectsLayout11))
            val addSubjectButtons = mutableListOf<Button>()
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton2))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton3))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton4))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton5))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton6))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton7))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton8))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton9))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton10))
            addSubjectButtons.add(findViewById(R.id.AddSubjectButton11))
            val removeSubjectButtons = mutableListOf<Button>()
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton2))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton3))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton4))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton5))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton6))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton7))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton8))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton9))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton10))
            removeSubjectButtons.add(findViewById(R.id.RemoveSubjectButton11))

            val gradesEditTexts = mutableListOf<TextInputEditText>()
            gradesEditTexts.add(findViewById(R.id.editTextGrade))
            gradesEditTexts.add(findViewById(R.id.editTextGrade2))
            gradesEditTexts.add(findViewById(R.id.editTextGrade3))
            gradesEditTexts.add(findViewById(R.id.editTextGrade4))
            gradesEditTexts.add(findViewById(R.id.editTextGrade5))
            gradesEditTexts.add(findViewById(R.id.editTextGrade6))
            gradesEditTexts.add(findViewById(R.id.editTextGrade7))
            gradesEditTexts.add(findViewById(R.id.editTextGrade8))
            gradesEditTexts.add(findViewById(R.id.editTextGrade9))
            gradesEditTexts.add(findViewById(R.id.editTextGrade10))
            gradesEditTexts.add(findViewById(R.id.editTextGrade11))

            val lessonsScheduleStartTimes = mutableListOf<EditText>()
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime1))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime2))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime3))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime4))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime5))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime6))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime7))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime8))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime9))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime10))
            lessonsScheduleStartTimes.add(findViewById(R.id.startTime11))
            val lessonsScheduleEndTimes = mutableListOf<EditText>()
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime1))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime2))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime3))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime4))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime5))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime6))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime7))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime8))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime9))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime10))
            lessonsScheduleEndTimes.add(findViewById(R.id.endTime11))
            val lessonsScheduleBrakeTimes = mutableListOf<EditText>()
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime1))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime2))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime3))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime4))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime5))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime6))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime7))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime8))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime9))
            lessonsScheduleBrakeTimes.add(findViewById(R.id.brakeTime10))

            val finalTeacherSubjectTimesViews = mutableListOf<MutableList<TextView>>()
            val verticalLayoutsForTeacherNameAndTime = mutableListOf<MutableList<LinearLayout>>()
            //                                           ^ subject    ^ list of vertical layouts that contains layouts with teacher name and time

            // buttons
            val gradesButton = findViewById<Button>(R.id.GradesButton)
            val enterSubjectsButton = findViewById<Button>(R.id.EnterSubjectsButton)
            val generateTimetableButton = findViewById<Button>(R.id.GenerateTimetableButton)
            val saveTimetableButton = findViewById<Button>(R.id.SaveTimetableButton)

            // other
            val timetableLayout = findViewById<LinearLayout>(R.id.TimetableLayout)
            val teachersVerticalLayout = findViewById<LinearLayout>(R.id.TeachersVerticalLayout)
            val timetableNameEditText = findViewById<EditText>(R.id.TimetableNameEditText)

            val prevClassButton = findViewById<Button>(R.id.PrevClassButton)
            val nextClassButton = findViewById<Button>(R.id.NextClassButton)
            val takeScreenshotButton = findViewById<Button>(R.id.TakeScreenshotButton)
            val classText = findViewById<TextView>(R.id.ClassText)

            val popupMenu = PopupMenu(applicationContext, gradesButton)

            // changing variables properties

            // setting some of the layouts invisible
            for (subjectLayout in subjectsLayouts) {
                if (subjectsLayouts.indexOf(subjectLayout) != 0) {
                    subjectLayout.visibility = LinearLayout.GONE
                }
            }
            // setting colors to normal
            for (addSubjectButton in addSubjectButtons) {
                addSubjectButton.setBackgroundColor(Color.parseColor("#2196F3"))
                addSubjectButton.setTextColor(Color.parseColor("#FFFFFF"))
            }
            for (removeSubjectButton in removeSubjectButtons) {
                removeSubjectButton.setBackgroundColor(Color.parseColor("#2196F3"))
                removeSubjectButton.setTextColor(Color.parseColor("#FFFFFF"))
            }


            // code

            // changing grades
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.grade1 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 1", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 1"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 1", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 1"
                        } else {
                            Toast.makeText(applicationContext, "Grade 1", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 1"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 0) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[0].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade2 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 2", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 2"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 2", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 2"
                        } else {
                            Toast.makeText(applicationContext, "Grade 2", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 2"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 1) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[1].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade3 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 3", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 3"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 3", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 3"
                        } else {
                            Toast.makeText(applicationContext, "Grade 3", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 3"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 2) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[2].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade4 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 4", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 4"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 4", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 4"
                        } else {
                            Toast.makeText(applicationContext, "Grade 4", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 4"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 3) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[3].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade5 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 5", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 5"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 5", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 5"
                        } else {
                            Toast.makeText(applicationContext, "Grade 5", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 5"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 4) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[4].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade6 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 6", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 6"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 6", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 6"
                        } else {
                            Toast.makeText(applicationContext, "Grade 6", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 6"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 5) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[5].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade7 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 7", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 7"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 7", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 7"
                        } else {
                            Toast.makeText(applicationContext, "Grade 7", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 7"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 6) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[6].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade8 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 8", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 8"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 8", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 8"
                        } else {
                            Toast.makeText(applicationContext, "Grade 8", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 8"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 7) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[7].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade9 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 9", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 9"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 9", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 9"
                        } else {
                            Toast.makeText(applicationContext, "Grade 9", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 9"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 8) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[8].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade10 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 10", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 10"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 10", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 10"
                        } else {
                            Toast.makeText(applicationContext, "Grade 10", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 10"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 9) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[9].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    R.id.grade11 -> {
                        if (language == "UZ") {
                            Toast.makeText(applicationContext, "Sinf 11", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Sinf 11"
                        } else if (language == "RU") {
                            Toast.makeText(applicationContext, "Класс 11", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Класс 11"
                        } else {
                            Toast.makeText(applicationContext, "Grade 11", Toast.LENGTH_SHORT).show()
                            gradesButton.text = "Grade 11"
                        }
                        for (subjectLayout in subjectsLayouts) {
                            if (subjectsLayouts.indexOf(subjectLayout) != 10) {
                                subjectLayout.visibility = LinearLayout.GONE
                            } else {
                                subjectsLayouts[10].visibility = LinearLayout.VISIBLE
                            }
                        }
                        true
                    }
                    else -> true
                }
            }
            gradesButton.setOnClickListener {
                try {
                    val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                    popup.isAccessible = true
                    val menu = popup.get(popupMenu)
                    menu.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(menu, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    popupMenu.show()
                }
            }

            // OK, NOW I NEED TO PUT SAVED INFORMATION INTO ALL VIEWS

            // here I need to put saved information into the all views
            // for this I will create global lists with this information
            for (grade in 0 until numberOfGrades) {
                try {
                    for (lesson in 0 until savedSubjectNames[loadTimetableIndex][grade].size) {
                        try {
                            if (savedSubjectNames[loadTimetableIndex][grade][lesson] != "") {
                                val horScroll = HorizontalScrollView(this)

                                val linearLayout = LinearLayout(this)
                                linearLayout.layoutParams =
                                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                linearLayout.orientation = LinearLayout.HORIZONTAL
                                // программно создаем новый виджет(View) в данном случае это EditText
                                val editSubjectName = EditText(this)
                                editSubjectName.layoutParams = LinearLayout.LayoutParams(370, 200)
                                if (language == "UZ") {
                                    editSubjectName.hint = "Element nomi"
                                } else if (language == "RU") {
                                    editSubjectName.hint = "Название предмета"
                                } else {
                                    editSubjectName.hint = "Subject name"
                                }
                                editSubjectName.inputType = InputType.TYPE_CLASS_TEXT
                                editSubjectName.setTextColor(Color.parseColor("#000000"))
                                editSubjectName.setHintTextColor(Color.parseColor("#555555"))
                                editSubjectName.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                                var filterArray = arrayOfNulls<InputFilter>(1)
                                filterArray[0] = LengthFilter(13)
                                editSubjectName.filters = filterArray
                                editSubjectName.setText(savedSubjectNames[loadTimetableIndex][grade][lesson])
                                val editSubjectNumberInWeek = EditText(this)
                                editSubjectNumberInWeek.layoutParams = LinearLayout.LayoutParams(570, 200)
                                if (language == "UZ") {
                                    editSubjectNumberInWeek.hint = "Haftada necha marta"
                                } else if (language == "RU") {
                                    editSubjectNumberInWeek.hint = "Сколько раз в неделю"
                                } else {
                                    editSubjectNumberInWeek.hint = "How many times a week"
                                }
                                editSubjectNumberInWeek.inputType = InputType.TYPE_CLASS_NUMBER
                                editSubjectNumberInWeek.setTextColor(Color.parseColor("#000000"))
                                editSubjectNumberInWeek.setHintTextColor(Color.parseColor("#555555"))
                                editSubjectNumberInWeek.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                                editSubjectNumberInWeek.setText(savedSubjectTimes[loadTimetableIndex][grade][lesson])
                                filterArray = arrayOfNulls(1)
                                filterArray[0] = LengthFilter(1)
                                editSubjectNumberInWeek.filters = filterArray

                                // добавляем виджет в layout
                                linearLayout.addView(editSubjectName)

                                finalSubjectsNamesViews.add(mutableListOf())
                                finalSubjectsNamesViews[grade].add(editSubjectName)
                                finalSubjectsTimesViews.add(mutableListOf())
                                finalSubjectsTimesViews[grade].add(editSubjectNumberInWeek)

                                linearLayout.addView(editSubjectNumberInWeek)
                                horScroll.addView(linearLayout)

                                subjectsLayouts[grade].addView(horScroll)
                            }
                        } catch (e: IndexOutOfBoundsException) {
                            // if I get here, it means that no more subjects left for current grade
                            println("No more subjects left or no more addSubjectButtons left")
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }

            // adding/removing subjects
            for (addSubjectButton in addSubjectButtons) {
                addSubjectButton.setOnClickListener {
                    try {
                        val horScroll = HorizontalScrollView(this)

                        val linearLayout = LinearLayout(this)
                        linearLayout.layoutParams =
                            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        linearLayout.orientation = LinearLayout.HORIZONTAL
                        // программно создаем новый виджет(View) в данном случае это EditText
                        val editSubjectName = EditText(this)
                        editSubjectName.layoutParams = LinearLayout.LayoutParams(670, 200)
                        if (language == "UZ") {
                            editSubjectName.hint = "Element nomi"
                        } else if (language == "RU") {
                            editSubjectName.hint = "Название предмета"
                        } else {
                            editSubjectName.hint = "Subject name"
                        }
                        editSubjectName.inputType = InputType.TYPE_CLASS_TEXT
                        editSubjectName.setTextColor(Color.parseColor("#000000"))
                        editSubjectName.setHintTextColor(Color.parseColor("#555555"))
                        editSubjectName.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                        var filterArray = arrayOfNulls<InputFilter>(1)
                        filterArray[0] = LengthFilter(13)
                        editSubjectName.filters = filterArray
                        val editSubjectNumberInWeek = EditText(this)
                        editSubjectNumberInWeek.layoutParams = LinearLayout.LayoutParams(670, 200)
                        if (language == "UZ") {
                            editSubjectNumberInWeek.hint = "Haftada necha marta"
                        } else if (language == "RU") {
                            editSubjectNumberInWeek.hint = "Сколько раз в неделю"
                        } else {
                            editSubjectNumberInWeek.hint = "How many times a week"
                        }
                        editSubjectNumberInWeek.inputType = InputType.TYPE_CLASS_NUMBER
                        editSubjectNumberInWeek.setTextColor(Color.parseColor("#000000"))
                        editSubjectNumberInWeek.setHintTextColor(Color.parseColor("#555555"))
                        editSubjectNumberInWeek.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                        filterArray = arrayOfNulls(1)
                        filterArray[0] = LengthFilter(1)
                        editSubjectNumberInWeek.filters = filterArray

                        // добавляем виджет в layout
                        linearLayout.addView(editSubjectName)

                        finalSubjectsNamesViews.add(mutableListOf())
                        finalSubjectsNamesViews[addSubjectButtons.indexOf(addSubjectButton)].add(editSubjectName)
                        finalSubjectsTimesViews.add(mutableListOf())
                        finalSubjectsTimesViews[addSubjectButtons.indexOf(addSubjectButton)].add(editSubjectNumberInWeek)

                        linearLayout.addView(editSubjectNumberInWeek)
                        horScroll.addView(linearLayout)

                        subjectsLayouts[addSubjectButtons.indexOf(addSubjectButton)].addView(horScroll)
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()

                        if (language == "UZ") {
                            Toast.makeText(this, "Barcha maydonlarni ketma-ket to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполняйте все поля последовательно", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information sequentially", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            for (removeSubjectButton in removeSubjectButtons) {
                removeSubjectButton.setOnClickListener {
                    if (subjectsLayouts[removeSubjectButtons.indexOf(removeSubjectButton)].childCount > 1) {
                        subjectsLayouts[removeSubjectButtons.indexOf(removeSubjectButton)].removeViewAt(
                            subjectsLayouts[removeSubjectButtons.indexOf(removeSubjectButton)].childCount - 1
                        )
                        finalSubjectsNamesViews[removeSubjectButtons.indexOf(removeSubjectButton)].removeLast()
                        finalSubjectsTimesViews[removeSubjectButtons.indexOf(removeSubjectButton)].removeLast()
                    }
                }
            }

            // entering teachers and seeing how much lessons they should teach)))
            enterSubjectsButton.setOnClickListener {
                // ENTERING TEACHERS

                // resetting all
                teachersVerticalLayout.removeAllViews()
                verticalLayoutsForTeacherNameAndTime.removeAll(verticalLayoutsForTeacherNameAndTime)
                finalTeacherSubjectTimesViews.removeAll(finalTeacherSubjectTimesViews)

                // надо посчитать сколько ВСЕГО каждого урока
                val names = mutableListOf<String>()
                val times = mutableListOf<Int>()

                for (grade in finalSubjectsNamesViews) {
                    for (j in grade) {
                        names.add(j.text.toString())
                    }
                }
                for (grade in finalSubjectsTimesViews) {
                    for (j in grade) {
                        if (j.text.toString() != "") {
                            try {
                                times.add(j.text.toString().toInt())
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                            }
                        } else {
                            times.add(1)
                        }
                    }
                }
                val listOfUsed = mutableListOf<String>()
                val sortedList = mutableListOf<String>()
                val sumOfEverySubject = mutableListOf<Int>()

                // добавляем в список sortedList названия предметов
                for (i in 0 until names.size) {
                    if (names[i].uppercase() !in listOfUsed) {
                        sortedList.add(names[i].uppercase())
                        listOfUsed.add(names[i].uppercase())
                    }
                }

                // теперь проходим по каждому элементу sortedList и если элемент
                // списка names будет равнятся элементу, то заносим его в finalList
                for (j in sortedList) {
                    var c = 0
                    for (i in 0 until names.size) {
                        if (names[i].uppercase() == j) {
                            try {
                                c += times[i]
                            } catch (e: IndexOutOfBoundsException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    sumOfEverySubject.add(c)
                }

                // генерация лэяутов с виджетами
                for (i in listOfUsed) {
                    // generating layout for subject name TextView
                    val subjectNameLayout = LinearLayout(this)
                    subjectNameLayout.layoutParams =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    val subjectNameText = TextView(this)
                    subjectNameText.text = i
                    subjectNameText.setTextColor(Color.WHITE)
                    subjectNameText.textSize = 20F
                    // changing text style to bold
                    subjectNameText.typeface = Typeface.DEFAULT_BOLD

                    subjectNameText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    subjectNameText.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    val param: LinearLayout.LayoutParams =
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    param.weight = 1F
                    subjectNameText.layoutParams = param

                    subjectNameLayout.addView(subjectNameText)

                    // generating layout for add/remove teacher buttons
                    val teacherButtonsLayout = LinearLayout(this)
                    teacherButtonsLayout.layoutParams =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    val addTeacherButton = Button(this)
                    addTeacherButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                    if (language == "UZ") {
                        addTeacherButton.text = "o'qituvchi qo'shing"
                    } else if (language == "RU") {
                        addTeacherButton.text = "добавить учителя"
                    } else {
                        addTeacherButton.text = "add teacher"
                    }
                    addTeacherButton.setTextColor(Color.WHITE)
                    val addTeacherButtonParam = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        .also { it.setMargins(10, 10, 10, 10) }
                    addTeacherButton.layoutParams = addTeacherButtonParam
                    addTeacherButtonParam.weight = 1F
                    val removeTeacherButton = Button(this)
                    removeTeacherButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                    if (language == "UZ") {
                        removeTeacherButton.text = "o'qituvchini o'chirish"
                    } else if (language == "RU") {
                        removeTeacherButton.text = "удалить учителя"
                    } else {
                        removeTeacherButton.text = "remove teacher"
                    }
                    removeTeacherButton.setTextColor(Color.WHITE)
                    val removeTeacherButtonParam =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                            .also { it.setMargins(10, 10, 10, 10) }
                    removeTeacherButtonParam.weight = 1F
                    removeTeacherButton.layoutParams = removeTeacherButtonParam

                    // generating layout for teacher name and how much times he teaches subjects
                    val verticalLayoutForTeacherNameAndTime = LinearLayout(this)
                    verticalLayoutForTeacherNameAndTime.orientation = LinearLayout.VERTICAL
                    verticalLayoutForTeacherNameAndTime.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    finalTeacherSubjectTimesViews.add(mutableListOf())

                    addTeacherButton.setOnClickListener {
                        verticalLayoutsForTeacherNameAndTime.add(mutableListOf())

                        val teacherNameTimeLayout = LinearLayout(this)
                        teacherNameTimeLayout.layoutParams =
                            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                        try {
                            verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].add(verticalLayoutForTeacherNameAndTime)
                        } catch (e: java.lang.IndexOutOfBoundsException) {
                            e.printStackTrace()
                        }

                        val teacherNameEditText = EditText(this)
                        if (language == "UZ") {
                            teacherNameEditText.hint = "O'qituvchining ismi"
                        } else if (language == "RU") {
                            teacherNameEditText.hint = "Имя учителя"
                        } else {
                            teacherNameEditText.hint = "Teacher's name"
                        }
                        teacherNameEditText.textSize = 20F
                        teacherNameEditText.setTextColor(Color.BLACK)
                        teacherNameEditText.setHintTextColor(Color.BLACK)
                        val teacherNameEditTextParam =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        teacherNameEditTextParam.weight = 1F
                        teacherNameEditText.layoutParams = teacherNameEditTextParam
                        val teacherSubjectTimesText = TextView(this)

                        val finalTimesList = mutableListOf<Int>()
                        while (true) {
                            // generating float list for every teacher
                            val tempTimesList = mutableListOf<Float>()
                            for (e in 0 until verticalLayoutForTeacherNameAndTime.childCount + 1) {
                                tempTimesList.add((sumOfEverySubject[listOfUsed.indexOf(i)].toFloat() / (verticalLayoutForTeacherNameAndTime.childCount.toFloat() + 1F)))
                            }
                            // converting float elements from that list to int elements
                            for (e in tempTimesList) {
                                finalTimesList.add(roundNumber(e))
                            }
                            var sum = 0
                            for (e in finalTimesList) {
                                sum += e
                            }
                            for (e in 0 until finalTimesList.size) {
                                if (sum > sumOfEverySubject[listOfUsed.indexOf(i)]) {
                                    finalTimesList[e] -= 1
                                    sum -= 1
                                } else if (sum < sumOfEverySubject[listOfUsed.indexOf(i)]) {
                                    finalTimesList[e] += 1
                                    sum += 1
                                }
                            }
                            if (sum == sumOfEverySubject[listOfUsed.indexOf(i)]) {
                                break
                            }
                        }

                        teacherSubjectTimesText.textSize = 20F
                        teacherSubjectTimesText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        teacherSubjectTimesText.setTextColor(Color.BLACK)
                        val teacherSubjectTimesTextParam =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        teacherSubjectTimesTextParam.weight = 1F
                        teacherSubjectTimesText.layoutParams = teacherSubjectTimesTextParam

                        teacherNameTimeLayout.addView(teacherNameEditText)
                        teacherNameTimeLayout.addView(teacherSubjectTimesText)

                        verticalLayoutForTeacherNameAndTime.addView(teacherNameTimeLayout)

                        finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)].add(teacherSubjectTimesText)

                        for (j in 0 until finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)].size) {
                            try {
                                finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)][j].text =
                                    finalTimesList[j].toString()
                            } catch (e: IndexOutOfBoundsException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    removeTeacherButton.setOnClickListener {
                        if (verticalLayoutsForTeacherNameAndTime.size > 0) {
                            try {
                                if (verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].last().childCount > 0) {
                                    verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].last()
                                        .removeViewAt(verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].last().childCount - 1)
                                    verticalLayoutsForTeacherNameAndTime[listOfUsed.indexOf(i)].removeLast()
                                    finalTeacherSubjectTimesViews[listOfUsed.indexOf(i)].removeLast()
                                }
                            } catch (e: NoSuchElementException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    teacherButtonsLayout.addView(addTeacherButton)
                    teacherButtonsLayout.addView(removeTeacherButton)

                    teachersVerticalLayout.addView(subjectNameLayout)
                    teachersVerticalLayout.addView(teacherButtonsLayout)
                    teachersVerticalLayout.addView(verticalLayoutForTeacherNameAndTime)
                }
            }

            // now I need to put saved number of every grade in the inputs
            for (i in 0 until gradesEditTexts.size) {
                gradesEditTexts[i].setText(savedNumberOfEveryGrade[loadTimetableIndex][i].toString())
            }

            // loading lessons schedule
            for (i in 0 until lessonsScheduleStartTimes.size) {
                lessonsScheduleStartTimes[i].setText(savedLessonsScheduleStartTimes[loadTimetableIndex][i])
            }
            for (i in 0 until lessonsScheduleEndTimes.size) {
                lessonsScheduleEndTimes[i].setText(savedLessonsScheduleEndTimes[loadTimetableIndex][i])
            }
            for (i in 0 until lessonsScheduleBrakeTimes.size) {
                lessonsScheduleBrakeTimes[i].setText(savedLessonsScheduleBrakeTimes[loadTimetableIndex][i])
            }

            // loading generated timetable
            if (savedIsTimetableGenerated[loadTimetableIndex]) {
                // resetting all
                timetableLayout.removeAllViews()
                numberOfEveryGrade.removeAll(numberOfEveryGrade)

                // adding number of every grade from grades edit texts to numberOfEveryGrade list
                try {
                    for (i in gradesEditTexts) {
                        if (i.text.toString() != "") {
                            numberOfEveryGrade.add(parseInt(i.text.toString()))
                        } else {
                            numberOfEveryGrade.add(1)
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                val timetable = TableLayout(this)
                timetable.setBackgroundColor(Color.LTGRAY)

                val yInfoLayout = LinearLayout(this)
                yInfoLayout.orientation = LinearLayout.VERTICAL
                yInfoLayout.setPadding(0, 0, 0, 0)
                for (i in 0 until 2) {
                    val paddingText = TextView(this)
                    paddingText.textSize = 14F
                    paddingText.width = 150
                    yInfoLayout.addView(paddingText)
                }
                // generating y info
                for (i in 0 until daysInWeek) {
                    for (j in 0 until (maxNumberOfLessons + 1)) {
                        try {
                            val layout = LinearLayout(this)
                            val text1 = TextView(this)
                            if (language == "UZ") {
                                text1.text = weekDaysUz[i][j].toString()
                            } else if (language == "RU") {
                                text1.text = weekDaysRu[i][j].toString()
                            } else {
                                text1.text = weekDays[i][j].toString()
                            }
                            text1.width = 75
                            text1.setTextColor(Color.BLACK)
//                        text1.height = 104
                            text1.textSize = 17.5F
                            text1.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            val text2 = TextView(this)
                            text2.text = (j + 1).toString()
                            text2.width = 75
                            text2.setTextColor(Color.BLACK)
//                        text2.height = 104
                            text2.textSize = 17.5F
                            text2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            layout.addView(text1)
                            layout.addView(text2)
                            yInfoLayout.addView(layout)
                        } catch (e: StringIndexOutOfBoundsException) {
                            val layout = LinearLayout(this)
                            val text1 = TextView(this)
                            text1.textSize = 17.5F
                            text1.setTextColor(Color.BLACK)
                            layout.addView(text1)
                            yInfoLayout.addView(layout)
                        }
                    }
                }

                val timetableList = mutableListOf<MutableList<MutableList<MutableList<EditText>>>>()
                //                  ^daysInWeek   ^maxNumberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of TextViews with names of subjects

                // generating grades row
                val row = TableRow(this)
                row.setPadding(0, 0, 0, 0)
                for (grade in 0 until numberOfGrades) {
                    try {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            try {
                                val element = TextView(this)
                                element.width = 600
                                if (language == "RU") {
                                    element.text = "${grade + 1}${russianLetters[grade_letter]}"
                                } else {
                                    element.text = "${grade + 1}${letters[grade_letter]}"
                                }
                                element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element.textSize = 14F
                                element.setTextColor(Color.BLACK)
                                row.addView(element)
                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
//                    e.printStackTrace()

                        if (language == "UZ") {
                            Toast.makeText(this, "Barcha maydonlarni ketma-ket to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполняйте все поля последовательно", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information sequentially", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                timetable.addView(row)

                // generating subject|classroom row
                val subjectClassroomRow = TableRow(this)
                subjectClassroomRow.setPadding(0, 0, 0, 0)
                for (grade in 0 until numberOfGrades) {
                    try {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            try {
                                val layout = LinearLayout(this)
//                            layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                                val element = TextView(this)
                                element.width = 400
                                if (language == "UZ") {
                                    element.text = "Element"
                                } else if (language == "RU") {
                                    element.text = "Предмет"
                                } else {
                                    element.text = "Subject"
                                }
                                element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element.textSize = 14F
                                element.setTextColor(Color.BLACK)

                                val element2 = TextView(this)
                                element2.width = 200
                                if (language == "UZ") {
                                    element2.text = "Kabinet"
                                } else if (language == "RU") {
                                    element2.text = "Кабинет"
                                } else {
                                    element2.text = "Classroom"
                                }
                                element2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element2.textSize = 13F
                                element2.setTextColor(Color.BLACK)

                                layout.addView(element)
                                layout.addView(element2)

                                subjectClassroomRow.addView(layout)

                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
//                    e.printStackTrace()

                        if (language == "UZ") {
                            Toast.makeText(this, "Jadvalni yaratish uchun oldingi barcha maydonlarni to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполните все предыдущие поля чтобы сгенерировать таблицу", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information to generate timetable", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                timetable.addView(subjectClassroomRow)


                // generating timetable
                classroomViewsList.removeAll(classroomViewsList)
                subjectViewsList.removeAll(subjectViewsList)
                for (week_day in 0 until daysInWeek) { // this is for y
                    timetableList.add(mutableListOf())
                    classroomViewsList.add(mutableListOf())
                    subjectViewsList.add(mutableListOf())
                    for (lesson in 0 until maxNumberOfLessons) { // this is also for y
                        timetableList[week_day].add(mutableListOf())
                        classroomViewsList[week_day].add(mutableListOf())
                        subjectViewsList[week_day].add(mutableListOf())
                        val timetableRow = TableRow(this)
                        timetableRow.setPadding(0, 0, 0, 0)
                        timetableRow.setBackgroundColor(Color.parseColor("#EEEEEE"))
                        for (grade in 0 until numberOfGrades) { // this is for x
                            timetableList[week_day][lesson].add(mutableListOf())
                            classroomViewsList[week_day][lesson].add(mutableListOf())
                            subjectViewsList[week_day][lesson].add(mutableListOf())
                            try {
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) { // this is also for x
                                    val layout = LinearLayout(this)

                                    val element = EditText(this)
                                    element.width = 400
                                    element.setText("$week_day, $lesson, $grade, $grade_letter")
                                    element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                    element.setBackgroundResource(android.R.color.transparent)
                                    element.setPadding(0, 0, 0, 0)
                                    val filterArray = arrayOfNulls<InputFilter>(1)
                                    filterArray[0] = LengthFilter(13)
                                    element.filters = filterArray
                                    element.textSize = 13F
                                    element.setTextColor(Color.BLACK)

                                    val element2 = EditText(this)
                                    element2.width = 200
                                    element2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                    element2.setBackgroundResource(android.R.color.transparent)
                                    element2.background = Color.WHITE.toDrawable()
                                    element2.setPadding(0, 0, 0, 0)
                                    val filterArray2 = arrayOfNulls<InputFilter>(1)
                                    filterArray2[0] = LengthFilter(4)
                                    element2.filters = filterArray2
                                    element2.setTextColor(Color.BLACK)

                                    layout.addView(element)
                                    layout.addView(element2)

                                    timetableList[week_day][lesson][grade].add(element)
                                    classroomViewsList[week_day][lesson][grade].add(element2)
                                    subjectViewsList[week_day][lesson][grade].add(element)
                                    timetableRow.addView(layout)
                                }
                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }

                        timetable.addView(timetableRow)
                        // this is for padding between week days
                        if (lesson == maxNumberOfLessons - 1) {
                            val l = LinearLayout(this)
                            val t = TextView(this)
                            t.textSize = 14F
                            t.setTextColor(Color.BLACK)
                            l.addView(t)
                            timetable.addView(l)
                        }
                    }
                }

                timetableLayout.addView(yInfoLayout)
                timetableLayout.addView(timetable)

                // putting information in the timetable
                for (week_day in 0 until daysInWeek) {
                    for (lesson in 0 until maxNumberOfLessons) {
                        for (grade in 0 until numberOfGrades) {
                            for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                timetableList[week_day][lesson][grade][grade_letter].setText(savedSubjectsList[loadTimetableIndex][week_day][lesson][grade][grade_letter])
                                classroomViewsList[week_day][lesson][grade][grade_letter].setText(savedClassroomsList[loadTimetableIndex][week_day][lesson][grade][grade_letter])
                            }
                        }
                    }
                }
            }
            // generating timetable
            generateTimetableButton.setOnClickListener {

                // resetting all
                timetableLayout.removeAllViews()
                numberOfEveryGrade.removeAll(numberOfEveryGrade)

                // adding number of every grade from grades edit texts to numberOfEveryGrade list
                try {
                    for (i in gradesEditTexts) {
                        if (i.text.toString() != "") {
                            numberOfEveryGrade.add(parseInt(i.text.toString()))
                        } else {
                            numberOfEveryGrade.add(1)
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                val timetable = TableLayout(this)
                timetable.setBackgroundColor(Color.LTGRAY)

                val yInfoLayout = LinearLayout(this)
                yInfoLayout.orientation = LinearLayout.VERTICAL
                yInfoLayout.setPadding(0, 0, 0, 0)
                for (i in 0 until 2) {
                    val paddingText = TextView(this)
                    paddingText.textSize = 14F
                    paddingText.width = 150
                    yInfoLayout.addView(paddingText)
                }
                // generating y info
                for (i in 0 until daysInWeek) {
                    for (j in 0 until (maxNumberOfLessons + 1)) {
                        try {
                            val layout = LinearLayout(this)
                            val text1 = TextView(this)
                            if (language == "UZ") {
                                text1.text = weekDaysUz[i][j].toString()
                            } else if (language == "RU") {
                                text1.text = weekDaysRu[i][j].toString()
                            } else {
                                text1.text = weekDays[i][j].toString()
                            }
                            text1.width = 75
                            text1.setTextColor(Color.BLACK)
//                        text1.height = 104
                            text1.textSize = 17.5F
                            text1.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            val text2 = TextView(this)
                            text2.text = (j + 1).toString()
                            text2.width = 75
                            text2.setTextColor(Color.BLACK)
//                        text2.height = 104
                            text2.textSize = 17.5F
                            text2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            layout.addView(text1)
                            layout.addView(text2)
                            yInfoLayout.addView(layout)
                        } catch (e: StringIndexOutOfBoundsException) {
                            val layout = LinearLayout(this)
                            val text1 = TextView(this)
                            text1.textSize = 17.5F
                            text1.setTextColor(Color.BLACK)
                            layout.addView(text1)
                            yInfoLayout.addView(layout)
                        }
                    }
                }

                val timetableList = mutableListOf<MutableList<MutableList<MutableList<EditText>>>>()
                //                  ^daysInWeek   ^maxNumberOfLessons ^numberOfGrades ^numberOfEveryGrade ^list of TextViews with names of subjects

                // generating grades row
                val row = TableRow(this)
                row.setPadding(0, 0, 0, 0)
                for (grade in 0 until numberOfGrades) {
                    try {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            try {
                                val element = TextView(this)
                                element.width = 600
                                if (language == "RU") {
                                    element.text = "${grade + 1}${russianLetters[grade_letter]}"
                                } else {
                                    element.text = "${grade + 1}${letters[grade_letter]}"
                                }
                                element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element.textSize = 14F
                                element.setTextColor(Color.BLACK)
                                row.addView(element)
                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
//                    e.printStackTrace()

                        if (language == "UZ") {
                            Toast.makeText(this, "Jadvalni yaratish uchun oldingi barcha maydonlarni to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполните все предыдущие поля чтобы сгенерировать таблицу", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information to generate timetable", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                timetable.addView(row)

                // generating subject|classroom row
                val subjectClassroomRow = TableRow(this)
                subjectClassroomRow.setPadding(0, 0, 0, 0)
                for (grade in 0 until numberOfGrades) {
                    try {
                        for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                            try {
                                val layout = LinearLayout(this)
//                            layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                                val element = TextView(this)
                                element.width = 400
                                if (language == "UZ") {
                                    element.text = "Element"
                                } else if (language == "RU") {
                                    element.text = "Предмет"
                                } else {
                                    element.text = "Subject"
                                }
                                element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element.textSize = 14F
                                element.setTextColor(Color.BLACK)

                                val element2 = TextView(this)
                                element2.width = 200
                                if (language == "UZ") {
                                    element2.text = "Kabinet"
                                } else if (language == "RU") {
                                    element2.text = "Кабинет"
                                } else {
                                    element2.text = "Classroom"
                                }
                                element2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                element2.textSize = 13F
                                element2.setTextColor(Color.BLACK)

                                layout.addView(element)
                                layout.addView(element2)

                                subjectClassroomRow.addView(layout)

                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
//                    e.printStackTrace()

                        if (language == "UZ") {
                            Toast.makeText(this, "Jadvalni yaratish uchun oldingi barcha maydonlarni to'ldiring", Toast.LENGTH_LONG).show()
                        } else if (language == "RU") {
                            Toast.makeText(this, "Заполните все предыдущие поля чтобы сгенерировать таблицу", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Fill in all the information to generate timetable", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                timetable.addView(subjectClassroomRow)


                // generating timetable
                classroomViewsList.removeAll(classroomViewsList)
                subjectViewsList.removeAll(subjectViewsList)
                for (week_day in 0 until daysInWeek) { // this is for y
                    timetableList.add(mutableListOf())
                    classroomViewsList.add(mutableListOf())
                    subjectViewsList.add(mutableListOf())
                    for (lesson in 0 until maxNumberOfLessons) { // this is also for y
                        timetableList[week_day].add(mutableListOf())
                        classroomViewsList[week_day].add(mutableListOf())
                        subjectViewsList[week_day].add(mutableListOf())
                        val timetableRow = TableRow(this)
                        timetableRow.setPadding(0, 0, 0, 0)
                        timetableRow.setBackgroundColor(Color.parseColor("#EEEEEE"))
                        for (grade in 0 until numberOfGrades) { // this is for x
                            timetableList[week_day][lesson].add(mutableListOf())
                            classroomViewsList[week_day][lesson].add(mutableListOf())
                            subjectViewsList[week_day][lesson].add(mutableListOf())
                            try {
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) { // this is also for x
                                    val layout = LinearLayout(this)

                                    val element = EditText(this)
                                    element.width = 400
                                    element.setText("$week_day, $lesson, $grade, $grade_letter")
                                    element.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                    element.setBackgroundResource(android.R.color.transparent)
                                    element.setPadding(0, 0, 0, 0)
                                    val filterArray = arrayOfNulls<InputFilter>(1)
                                    filterArray[0] = LengthFilter(13)
                                    element.filters = filterArray
                                    element.textSize = 13F
                                    element.setTextColor(Color.BLACK)

                                    val element2 = EditText(this)
                                    element2.width = 200
                                    element2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                                    element2.setBackgroundResource(android.R.color.transparent)
                                    element2.background = Color.WHITE.toDrawable()
                                    element2.setPadding(0, 0, 0, 0)
                                    val filterArray2 = arrayOfNulls<InputFilter>(1)
                                    filterArray2[0] = LengthFilter(4)
                                    element2.filters = filterArray2
                                    element2.setTextColor(Color.BLACK)

                                    layout.addView(element)
                                    layout.addView(element2)

                                    timetableList[week_day][lesson][grade].add(element)
                                    classroomViewsList[week_day][lesson][grade].add(element2)
                                    subjectViewsList[week_day][lesson][grade].add(element)
                                    timetableRow.addView(layout)
                                }
                            } catch (e: IndexOutOfBoundsException) {
//                            e.printStackTrace()
                            }
                        }

                        timetable.addView(timetableRow)
                        // this is for padding between week days
                        if (lesson == maxNumberOfLessons - 1) {
                            val l = LinearLayout(this)
                            val t = TextView(this)
                            t.textSize = 14F
                            t.setTextColor(Color.BLACK)
                            l.addView(t)
                            timetable.addView(l)
                        }
                    }
                }

                timetableLayout.addView(yInfoLayout)
                timetableLayout.addView(timetable)

                // putting information in the timetable
                for (week_day in 0 until daysInWeek) { // this is for y
                    for (lesson in 0 until maxNumberOfLessons) { // this is also for y
                        for (grade in 0 until numberOfGrades) { // this is for x
                            for (grade_letter in 0 until numberOfEveryGrade[grade]) { // this is also for x
                                try {
                                    if (myInt(finalSubjectsTimesViews[grade][lesson].text.toString()) > week_day) {
                                        timetableList[week_day][lesson][grade][grade_letter].setText(
                                            finalSubjectsNamesViews[grade][lesson].text.toString()
                                        )
                                    } else {
                                        timetableList[week_day][lesson][grade][grade_letter].setText("")
                                    }
                                } catch (e: IndexOutOfBoundsException) {
                                    timetableList[week_day][lesson][grade][grade_letter].setText("")
                                }
                            }
                        }
                    }
                }
            }

            // button to select next class
            classText.text = "${selectedClassGrade + 1}${letters[selectedClassLetter]}"
            nextClassButton.setOnClickListener {
                if (selectedClassLetter + 1 < numberOfEveryGrade[selectedClassGrade]) {
                    selectedClassLetter += 1
                } else {
                    selectedClassLetter = 0
                    if (selectedClassGrade < 10) {
                        selectedClassGrade += 1
                    }
                }

                classText.text = "${selectedClassGrade + 1}${letters[selectedClassLetter]}"
            }
            // button to select prev class
            prevClassButton.setOnClickListener {
                if (selectedClassLetter > 0) {
                    selectedClassLetter -= 1
                } else {
                    if (selectedClassGrade > 0) {
                        selectedClassGrade -= 1
                    }
                }

                classText.text = "${selectedClassGrade + 1}${letters[selectedClassLetter]}"
            }
            // let user take screenshot of the timetable
            takeScreenshotButton.setOnClickListener {
                classroomsList.removeAll(classroomsList)
                subjectsList.removeAll(subjectsList)

                for (week_day in 0 until daysInWeek) {
                    classroomsList.add(mutableListOf())
                    for (lesson in 0 until maxNumberOfLessons) {
                        classroomsList[week_day].add(mutableListOf())
                        for (grade in 0 until numberOfGrades) {
                            classroomsList[week_day][lesson].add(mutableListOf())
                            for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                classroomsList[week_day][lesson][grade].add(classroomViewsList[week_day][lesson][grade][grade_letter].text.toString())
                            }
                        }
                    }
                }
                for (week_day in 0 until daysInWeek) {
                    subjectsList.add(mutableListOf())
                    for (lesson in 0 until maxNumberOfLessons) {
                        subjectsList[week_day].add(mutableListOf())
                        for (grade in 0 until numberOfGrades) {
                            subjectsList[week_day][lesson].add(mutableListOf())
                            for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                subjectsList[week_day][lesson][grade].add(subjectViewsList[week_day][lesson][grade][grade_letter].text.toString())
                            }
                        }
                    }
                }

                val intent = Intent(this, MyCanvasActivity::class.java)
                startActivity(intent)
            }

            // SAVING DATA
            saveTimetableButton.setOnClickListener {
                saveTimetable = true

                var timetableName = "Timetable $timetablesCount"
                if (timetableNameEditText.text.toString() != "") {
                    timetableName = timetableNameEditText.text.toString()
                }
                if (timetableNameEditText.text.toString() in timetableNames) {
                    saveTimetable = false
                    Toast.makeText(this, "This name already exists", Toast.LENGTH_SHORT).show()
                }

                if (saveTimetable) {
                    // when I get here, I need to save all the information

                    // resetting lists
                    numberOfEveryGrade.removeAll(numberOfEveryGrade)
                    // adding number of every grade from grades edit texts to numberOfEveryGrade list
                    try {
                        for (i in gradesEditTexts) {
                            if (i.text.toString() != "") {
                                numberOfEveryGrade.add(parseInt(i.text.toString()))
                            } else {
                                numberOfEveryGrade.add(1)
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }

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

                    timetablesCount += 1
                    // getting current time in readable format
                    val sdf = getDateInstance()
                    val timetableDate = sdf.format(Date())
                    timetableNames.add(timetableName)
                    timetableDates.add(timetableDate)
                    savedSubjectNames.add(mutableListOf())
                    savedSubjectTimes.add(mutableListOf())
                    savedNumberOfEveryGrade.add(mutableListOf())
                    savedLessonsScheduleStartTimes.add(mutableListOf())
                    savedLessonsScheduleEndTimes.add(mutableListOf())
                    savedLessonsScheduleBrakeTimes.add(mutableListOf())
                    savedIsTimetableGenerated.add(true)
                    savedSubjectsList.add(mutableListOf())
                    savedClassroomsList.add(mutableListOf())

                    // saving subject names
                    for (grade in 0 until numberOfGrades) {
                        savedSubjectNames[timetablesCount - 1].add(mutableListOf())
                        for (subject in 0 until maxNumberOfSubjects) {
                            try {
                                savedSubjectNames[timetablesCount - 1][grade].add(finalSubjectsNamesViews[grade][subject].text.toString())
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                    }

                    // saving subject times
                    for (grade in 0 until numberOfGrades) {
                        savedSubjectTimes[timetablesCount - 1].add(mutableListOf())
                        for (subject in 0 until maxNumberOfSubjects) {
                            try {
                                savedSubjectTimes[timetablesCount - 1][grade].add(finalSubjectsTimesViews[grade][subject].text.toString())
                            } catch (e: IndexOutOfBoundsException) {
                            }
                        }
                    }

                    // saving list of number of every grade
                    for (i in 0 until gradesEditTexts.size) {
                        if (gradesEditTexts[i].text.toString() != "") {
                            savedNumberOfEveryGrade[timetablesCount - 1].add(gradesEditTexts[i].text.toString().toInt())
                        } else {
                            savedNumberOfEveryGrade[timetablesCount - 1].add(1)
                        }
                    }

                    // saving lessons schedule
                    for (i in lessonsScheduleStartTimes) {
                        savedLessonsScheduleStartTimes[timetablesCount - 1].add(i.text.toString())
                    }
                    for (i in lessonsScheduleEndTimes) {
                        savedLessonsScheduleEndTimes[timetablesCount - 1].add(i.text.toString())
                    }
                    for (i in lessonsScheduleBrakeTimes) {
                        savedLessonsScheduleBrakeTimes[timetablesCount - 1].add(i.text.toString())
                    }

                    // updating lists of timetable views information
                    classroomsList.removeAll(classroomsList)
                    subjectsList.removeAll(subjectsList)
                    for (week_day in 0 until daysInWeek) {
                        classroomsList.add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            classroomsList[week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                classroomsList[week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    try {
                                        classroomsList[week_day][lesson][grade].add(classroomViewsList[week_day][lesson][grade][grade_letter].text.toString())
                                    } catch (e: IndexOutOfBoundsException) {
                                        // if I get here, then timetable is not generated
                                        // so I need to put blank Strings into classroomsList
                                        classroomsList[week_day][lesson][grade].add("")
                                    }
                                }
                            }
                        }
                    }
                    for (week_day in 0 until daysInWeek) {
                        subjectsList.add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            subjectsList[week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                subjectsList[week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    try {
                                        subjectsList[week_day][lesson][grade].add(subjectViewsList[week_day][lesson][grade][grade_letter].text.toString())
                                    } catch (e: IndexOutOfBoundsException) {
                                        // if I get here, then timetable is not generated
                                        // so I need to put blank Strings into subjectsList
                                        subjectsList[week_day][lesson][grade].add("")
                                    }
                                }
                            }
                        }
                    }
                    // saving information in all views in current timetable
                    for (week_day in 0 until daysInWeek) {
                        savedSubjectsList[timetablesCount - 1].add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            savedSubjectsList[timetablesCount - 1][week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                savedSubjectsList[timetablesCount - 1][week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    savedSubjectsList[timetablesCount - 1][week_day][lesson][grade].add(subjectsList[week_day][lesson][grade][grade_letter])
                                }
                            }
                        }
                    }
                    for (week_day in 0 until daysInWeek) {
                        savedClassroomsList[timetablesCount - 1].add(mutableListOf())
                        for (lesson in 0 until maxNumberOfLessons) {
                            savedClassroomsList[timetablesCount - 1][week_day].add(mutableListOf())
                            for (grade in 0 until numberOfGrades) {
                                savedClassroomsList[timetablesCount - 1][week_day][lesson].add(mutableListOf())
                                for (grade_letter in 0 until numberOfEveryGrade[grade]) {
                                    savedClassroomsList[timetablesCount - 1][week_day][lesson][grade].add(classroomsList[week_day][lesson][grade][grade_letter])
                                }
                            }
                        }
                    }

                    // returning to main page
                    // closing this page (activity) and returning to MainActivity.kt
                    finish()
                }
            }

            // changing language
            if (language == "RU") {
                val createTimetableTitle = findViewById<TextView>(R.id.CreateTimetableTitle)
                createTimetableTitle.text = "Создать таблицу"
                val subjectsSectionText = findViewById<TextView>(R.id.SubjectsSectionText)
                subjectsSectionText.text = "Предметы"
                gradesButton.text = "Класс 1"
                for (addSubjectButton in addSubjectButtons) {
                    addSubjectButton.text = "Добавить предмет"
                }
                for (removeSubjectButton in removeSubjectButtons) {
                    removeSubjectButton.text = "Удалить предмет"
                }
                enterSubjectsButton.text = "Ввести предметы"
                val numberOfGradesSectionText = findViewById<TextView>(R.id.NumberOfGradesSectionText)
                numberOfGradesSectionText.text = "Число классов"
                for (gradesEditText in gradesEditTexts) {
                    gradesEditText.hint = "класс ${gradesEditTexts.indexOf(gradesEditText) + 1}"
                }
                val teachersSectionText = findViewById<TextView>(R.id.TeachersSectionText)
                teachersSectionText.text = "Учителя"
                val callScheduleSectionText = findViewById<TextView>(R.id.CallScheduleSectionText)
                callScheduleSectionText.text = "Расписание звонков"
                val startOfLessonText = findViewById<TextView>(R.id.StartOfLessonText)
                startOfLessonText.text = "Начало урока"
                val endOfLessonText = findViewById<TextView>(R.id.EndOfLessonText)
                endOfLessonText.text = "Конец урока"
                val brakeTimeText = findViewById<TextView>(R.id.BrakeTimeText)
                brakeTimeText.text = "Перемена"
                val viewTimetableSectionText = findViewById<TextView>(R.id.ViewTimetableSectionText)
                viewTimetableSectionText.text = "Таблица расписания уроков"
                generateTimetableButton.text = "Сгенерировать расписание уроков"
                prevClassButton.text = "Предыдущий"
                nextClassButton.text = "Следующий"
                takeScreenshotButton.text = "Сохранить в галерею"
                val saveTimetableText = findViewById<TextView>(R.id.SaveTimetableText)
                saveTimetableText.text = "Сохранение таблицы"
                timetableNameEditText.hint = "Название таблицы"
                saveTimetableButton.text = "Сохранить таблицу"
            }
            if (language == "UZ") {
                val createTimetableTitle = findViewById<TextView>(R.id.CreateTimetableTitle)
                createTimetableTitle.text = "Jadval yaratish"
                val subjectsSectionText = findViewById<TextView>(R.id.SubjectsSectionText)
                subjectsSectionText.text = "Elementlar"
                gradesButton.text = "1-sinf"
                for (addSubjectButton in addSubjectButtons) {
                    addSubjectButton.text = "Element qo'shish"
                }
                for (removeSubjectButton in removeSubjectButtons) {
                    removeSubjectButton.text = "Elementni o'chirish"
                }
                enterSubjectsButton.text = "Elementlarni kiriting"
                val numberOfGradesSectionText = findViewById<TextView>(R.id.NumberOfGradesSectionText)
                numberOfGradesSectionText.text = "Sinflar soni"
                for (gradesEditText in gradesEditTexts) {
                    gradesEditText.hint = "sinf ${gradesEditTexts.indexOf(gradesEditText) + 1}"
                }
                val teachersSectionText = findViewById<TextView>(R.id.TeachersSectionText)
                teachersSectionText.text = "O'qituvchilar"
                val callScheduleSectionText = findViewById<TextView>(R.id.CallScheduleSectionText)
                callScheduleSectionText.text = "Qo'ng'iroqlar jadvali"
                val startOfLessonText = findViewById<TextView>(R.id.StartOfLessonText)
                startOfLessonText.text = "Dars boshlanishi"
                val endOfLessonText = findViewById<TextView>(R.id.EndOfLessonText)
                endOfLessonText.text = "Darsning oxiri"
                val brakeTimeText = findViewById<TextView>(R.id.BrakeTimeText)
                brakeTimeText.text = "Maktabda o'zgarish"
                val viewTimetableSectionText = findViewById<TextView>(R.id.ViewTimetableSectionText)
                viewTimetableSectionText.text = "Dars jadvali"
                generateTimetableButton.text = "Dars jadvalini tuzing"
                prevClassButton.text = "Oldingi"
                nextClassButton.text = "Keyingi"
                takeScreenshotButton.text = "Galereyaga saqlang"
                val saveTimetableText = findViewById<TextView>(R.id.SaveTimetableText)
                saveTimetableText.text = "Jadvalni saqlash"
                timetableNameEditText.hint = "Jadval nomi"
                saveTimetableButton.text = "Jadvalni saqlash"
            }
        }
    }

    private fun roundNumber(floatNumber: Float): Int {
        val tempString = floatNumber.toString()
        val commaDigitIndex = tempString.indexOf(".") + 1
        if (parseInt(tempString[commaDigitIndex].toString()) > 4) {
            return (tempString.slice(IntRange(0, commaDigitIndex - 2)).toInt() + 1)
        } else {
            return tempString.slice(IntRange(0, commaDigitIndex - 2)).toInt()
        }
    }

    private fun myInt(str: String): Int {
        try {
            return str.toInt()
        } catch (e: NumberFormatException) {
        }
        return 1
    }
}
