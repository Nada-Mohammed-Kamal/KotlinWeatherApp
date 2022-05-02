package com.example.kotlinweatherapp.model

data class Weather (
    var hour : String,
    var temp : String,
    var icon : String,
    var dateTime : String,
    var lowTemp : String,
    var heightTemp : String,
    var dailyIcon: String,

    var alarmPlace : String
        )