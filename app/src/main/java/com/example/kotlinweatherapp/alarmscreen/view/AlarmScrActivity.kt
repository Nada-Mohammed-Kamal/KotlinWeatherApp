package com.example.kotlinweatherapp.alarmscreen.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import com.example.kotlinweatherapp.R
import java.text.SimpleDateFormat
import java.util.*


class AlarmScrActivity : AppCompatActivity() ,AdapterView.OnItemSelectedListener {
    //calendar(date)
    private val myCalendar = Calendar.getInstance()
    private var dateSelected: Date? = null
    var startDate : EditText? = null
    var endDate : EditText? = null
    lateinit var addBtn : Button

    //Time
    var alarmTime : EditText? = null
    lateinit var timeCalendar : Calendar
    var currentHour : Int = 0
    var currentMinute : Int = 0
    lateinit var timePickerDialogListener: TimePickerDialog.OnTimeSetListener
    lateinit var timeBtn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_scr)

        //addBtn
        addBtn = findViewById(R.id.btnAddAlarmToRecyclerViewFromAlarmDetailsScreenId)
        //setDateAndTimeViews
        setDateAndTimePickersView()
        //view time picker dialog
        viewTimePicker()

        //alarmTime
        alarmTime?.setOnClickListener{
            TimePickerDialog(
                this,
                12,
                timePickerDialogListener,
                0,0,
                false
            ).show()
        }

        viewCal()

        //cal
        //startDate
        startDate?.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(
                    Calendar.DAY_OF_MONTH
                )
            ).show()
        }

        //endDate
        endDate?.setOnClickListener {
            DatePickerDialog(
                this,
                date1,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(
                    Calendar.DAY_OF_MONTH
                )
            ).show()
        }

        addBtn.setOnClickListener{
            //add to db and set an alarm manager finally return to the previous screen
            //asta5dem de law finish bazet
//            var i = Intent(context, AlarmScrActivity::class.java)
//            startActivity(i)
            finish()
        }


    }

    private fun viewCal() {
        val spinner = findViewById<View>(R.id.spinnerAlarmTimeId) as Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.planets_array, android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    private fun viewTimePicker() {
        timePickerDialogListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> // logic to properly handle
                // the picked timings by user
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }

                alarmTime?.setText(formattedTime)
            }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        println(p0?.getItemAtPosition(p2))
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


    //cal
    var date: OnDateSetListener? =
        OnDateSetListener { datePicker, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            dateSelected = Date(year, month, day)
            updateLabel()
        }

    var date1: OnDateSetListener? =
        OnDateSetListener { datePicker, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            dateSelected = Date(year, month, day)
            updateLabel1()
        }



    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        startDate?.setText(dateFormat.format(myCalendar.time))
    }

    private fun updateLabel1() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        endDate?.setText(dateFormat.format(myCalendar.time))
    }

    private fun setDateAndTimePickersView(){
        startDate = findViewById(R.id.startDateid)
        endDate = findViewById(R.id.endDateid)

        alarmTime = findViewById(R.id.ediTtxtAlarmTimeid)
        //time
        timeCalendar = Calendar.getInstance()
        currentHour = timeCalendar.get(Calendar.HOUR_OF_DAY)
        currentMinute = timeCalendar.get(Calendar.MINUTE)
    }





}