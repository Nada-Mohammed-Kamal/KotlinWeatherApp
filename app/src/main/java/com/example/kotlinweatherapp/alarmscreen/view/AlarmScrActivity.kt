package com.example.kotlinweatherapp.alarmscreen.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.alarmscreen.viewmodel.AlarmFragViewModel
import com.example.kotlinweatherapp.alarmscreen.viewmodel.AlarmViewModelFactory
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.example.kotlinweatherapp.workmanager.WorkerUtilsClass
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*

class AlarmScrActivity : AppCompatActivity() ,AdapterView.OnItemSelectedListener {
    //calendar(date)
    private val myCalendar = Calendar.getInstance()
    private var dateSelected: Date? = null
    var startDate : EditText? = null
    var endDate : EditText? = null
    lateinit var addBtn : Button
    lateinit var reasonOfTheAlarm: Spinner
    lateinit var title : EditText
    lateinit var alarmOrNotificationSwitch: Switch
    lateinit var reason : TextView
    //Time
    var alarmTime : EditText? = null
    lateinit var timeCalendar : Calendar
    var currentHour : Int = 0
    var currentMinute : Int = 0
    lateinit var timePickerDialogListener: TimePickerDialog.OnTimeSetListener
    lateinit var timeBtn : Button
    var isAlarm : Boolean = false

    lateinit var homeDateId : TextView
    lateinit var homePlaceId : TextView
    lateinit var homeWindId : TextView

    //viewModel
    private lateinit var viewModel: AlarmFragViewModel
    lateinit var alarmFactory : AlarmViewModelFactory

    //alarm obj to be added
    lateinit var alarmObj : Alarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_scr)
        //title and reason of the alarm
        title = findViewById(R.id.alarmTitleId)
        reasonOfTheAlarm = findViewById(R.id.spinnerAlarmTimeId)
        alarmOrNotificationSwitch = findViewById(R.id.alarmOrNotificationSwitchId)



        //addBtn
        addBtn = findViewById(R.id.btnAddAlarmToRecyclerViewFromAlarmDetailsScreenId)

        homeDateId = findViewById(R.id.homeDateId)
        homePlaceId = findViewById(R.id.homePlaceId)
        homeWindId = findViewById(R.id.homeWindId)
        reason = findViewById(R.id.alarmIDID)
        //setDateAndTimeViews
        setDateAndTimePickersView()
        //view time picker dialog
        viewTimePicker()

        if(SharedPrefsHelper.getLang(this) == "ar"){
            homeDateId.text = "التاريخ الاول"
            homePlaceId.text = "التاريخ الاخير"
            homeWindId.text = "ميعاد التنبيه"
            reason.text = "سبب التنبيه"
            alarmOrNotificationSwitch.text = "ديالوج"

            addBtn.text = "أضف"
        }


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
            isAlarm = alarmOrNotificationSwitch.isChecked
            //the alarm obj to be added
            alarmObj = Alarm(title?.text.toString(), startDate?.text.toString() , endDate?.text.toString() , alarmTime?.text.toString()
                , alarmOrNotificationSwitch.isChecked , reasonOfTheAlarm.selectedItem.toString() , LatLng( SharedPrefsHelper.getLatitude(this).toDouble() , SharedPrefsHelper.getLongitude(this).toDouble() ))


            viewModel.addAlarm(alarmObj)

            WorkerUtilsClass.addRequestsToWorkManager(alarmObj)


            Toast.makeText(this , "alarm added successfully" , Toast.LENGTH_SHORT).show()
            finish()
        }


        //view model
        alarmFactory = AlarmViewModelFactory(
            Repo.getInstance(
                weatherRetrofitClient.getInstance() ,
                ConcreateLocalSource(this),  this) , this)
        viewModel = ViewModelProvider(this, alarmFactory).get(AlarmFragViewModel::class.java)

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