package com.example.kotlinweatherapp.splashscreen

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.example.kotlinweatherapp.CustomDiolog.GPSOrMapActivity
import com.example.kotlinweatherapp.MainActivity
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper

class SplashAnimation : AppCompatActivity() {


    //animation
    lateinit var animation : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_annimation)



        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        animation = findViewById(R.id.animationView)

        animation.animate().translationY(1500.0F).setDuration(1000).startDelay = 4000

        Handler().postDelayed(kotlinx.coroutines.Runnable {
            kotlin.run {
                //for GPS or Map dialog
                if(SharedPrefsHelper.getIsFirstTime(this)){
                    startActivity(Intent(this , GPSOrMapActivity::class.java))
                }else{
                    startActivity(Intent(this , MainActivity::class.java))
                }
            }
        }, 5000)

        initConnectionListener()


    }

    private fun initConnectionListener() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(NetworkChangeReceiver(), intentFilter)
    }



}