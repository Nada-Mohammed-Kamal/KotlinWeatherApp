package com.example.kotlinweatherapp.workmanager

import java.util.*

class CalcTimes {
    companion object{
        fun getDaysBetweenDates(startdate: Date?, enddate: Date?): List<Date?> {
            val dates: MutableList<Date?> = ArrayList()
            val calendar: Calendar = GregorianCalendar()
            calendar.time = startdate
            while (calendar.time.before(enddate)) {
                val result = calendar.time
                dates.add(result)
                calendar.add(Calendar.DATE, 1)
            }
            return dates
        }
    }
}