package com.example.kotlinweatherapp.sharedprefs

import android.content.Context

class SharedPrefsHelper{

    companion object {

        private var INSTANCE: SharedPrefsHelper? = null

        fun getInstance(context: Context): SharedPrefsHelper? =
            INSTANCE ?: synchronized(this) {
                if(INSTANCE == null){
                    return@synchronized SharedPrefsHelper()
                }else {
                    return@synchronized INSTANCE
                }
            }

        fun setLongitude(context: Context , longitude : String){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("long",longitude)
            editor.apply()
        }


        fun setLatitude(context: Context , latitude : String){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("lat",latitude)
            editor.apply()
        }


        fun setLanguage(context: Context , lang : String){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("lang",lang)
            editor.apply()
        }


        fun setTempUnit(context: Context , tempUnit : String){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("tempUnit",tempUnit)
            editor.apply()
        }

        fun getLongitude(context: Context) : String{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getString("long","31.2885")!!
        }

        fun getTempUnit(context: Context) : String{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getString("tempUnit","metric")!!
        }

        fun getLatitude(context: Context) : String{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getString("lat","29.9604")!!
        }

        fun getLang(context: Context) : String{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getString("lang","en")!!
        }

        fun setIsFav(context: Context , isFav : Boolean){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putBoolean("isFav", isFav)
            editor.apply()
        }

        fun getIsFav(context: Context) : Boolean{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getBoolean("isFav", false)!!
        }

        fun setIsFirstTime(context: Context , isFirstTime : Boolean){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putBoolean("isFirstTime", isFirstTime)
            editor.apply()
        }


        fun getIsFirstTime(context: Context) : Boolean{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getBoolean("isFirstTime",true)
        }

        fun getIsFirstTimeForAddAlarm(context: Context) : Boolean{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getBoolean("isFirstTimeAdd",true)
        }

        fun setIsFirstTimeAddAlarm(context: Context , isFirstTime : Boolean){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putBoolean("isFirstTimeAdd", isFirstTime)
            editor.apply()
        }


        fun setPreviousLatLng(context: Context , latlng : String){
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("prevLatLng",latlng)
            editor.apply()
        }


        fun getPreviousLatLng(context: Context) : String{
            val sharedPreference = context.getSharedPreferences("myAppSHaredPrefs", Context.MODE_PRIVATE)
            return sharedPreference.getString("prevLatLng","")!!
        }


    }
}