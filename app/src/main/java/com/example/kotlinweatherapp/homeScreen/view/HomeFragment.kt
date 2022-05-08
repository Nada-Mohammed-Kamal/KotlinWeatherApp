package com.example.kotlinweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.homeScreen.viewmodel.HomeFragViewModel
import com.example.kotlinweatherapp.homeScreen.viewmodel.HomeViewModelFactory
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    lateinit var dailyRecyclerView : RecyclerView
    lateinit var hourlyRecyclerView : RecyclerView
    private lateinit var viewModel: HomeFragViewModel
    lateinit var homeFactory : HomeViewModelFactory

    lateinit var placeName : TextView
    lateinit var temp : TextView
    lateinit var descImg : ImageView
    lateinit var icon : ImageView
    lateinit var date : TextView
    lateinit var hummidity : TextView
    lateinit var pressure : TextView
    lateinit var wind : TextView
    lateinit var cloud : TextView
    lateinit var ultraViolet : TextView
    lateinit var visibility : TextView
    lateinit var desc : TextView

    //labels
    lateinit var hummid : TextView
    lateinit var pressu : TextView
    lateinit var wnd : TextView
    lateinit var clod : TextView
    lateinit var uv : TextView
    lateinit var visblty : TextView



    //var weatherObj : WeatherResponse? = null
    lateinit var myView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view

        if(!(NetworkChangeReceiver.isThereInternetConnection)){
            Log.e("snackbaaaaaaaaar", "snackbaaaaaaaaar:${NetworkChangeReceiver.isThereInternetConnection} ", )
            showNoNetSnackbar()
        }


        //view model
        homeFactory = HomeViewModelFactory(
            Repo.getInstance(weatherRetrofitClient.getInstance() ,ConcreateLocalSource(requireContext()),  requireContext()) , requireContext())
        viewModel = ViewModelProvider(this, homeFactory).get(HomeFragViewModel::class.java)


        //labels
        uv = view.findViewById(R.id.idUltraViolet)
        clod = view.findViewById(R.id.idCloud)
        hummid = view.findViewById(R.id.Idhummid)
        wnd = view.findViewById(R.id.idWind)
        pressu = view.findViewById(R.id.idPressure)
        visblty = view.findViewById(R.id.idVisability)

        if(SharedPrefsHelper.getLang(requireContext()) == "ar"){
            uv.text = "الاشعه فوق البنفسجيه"
            clod.text = "السحب"
            hummid.text = "الرطوبه"
            wnd.text = "الرياح"
            pressu.text = "الضغط"
            visblty.text = "الرؤيه"
        }

        //recycler views
        dailyRecyclerView = view.findViewById(R.id.recyclerViewDailyId)
        hourlyRecyclerView = view.findViewById(R.id.recyclerViewHourlyId)

        var hourlyLayoutManager = LinearLayoutManager(context)
        hourlyLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyRecyclerView.layoutManager = hourlyLayoutManager


        var dailyLayoutManager = LinearLayoutManager(context)
        dailyLayoutManager.orientation = LinearLayoutManager.VERTICAL
        dailyRecyclerView.layoutManager = dailyLayoutManager

        //
        placeName = view.findViewById(R.id.homePlaceId)
        temp = view.findViewById(R.id.homeTempratureId)
        descImg = view.findViewById(R.id.descImgId)
        desc = view.findViewById(R.id.homeDescId)
        //icon = view.findViewById(R.id.imageView)
        date = view.findViewById(R.id.homeDateId)
        hummidity = view.findViewById(R.id.homeHummidityId)
        pressure = view.findViewById(R.id.homePressureId)
        wind = view.findViewById(R.id.homeWindId)
        cloud = view.findViewById(R.id.homeCloudId)
        ultraViolet = view.findViewById(R.id.homeultraVioletId)
        visibility = view.findViewById(R.id.homeVisabitiyId)


        viewModel.getWeatherObj().observe(viewLifecycleOwner , { weatherObj ->

            if(weatherObj != null && weatherObj.isNotEmpty()){
                var hourlyAdapter = HourlyWeatherAdapter(weatherObj[0], requireContext())
                hourlyRecyclerView.adapter = hourlyAdapter
                var dailyAdapter = DailyWeatherAdapter(weatherObj[0], requireContext())
                dailyRecyclerView.adapter = dailyAdapter

                getAddressAndDateForLocation()
                if(SharedPrefsHelper.getTempUnit(requireContext()) == "metric"){
                    if(SharedPrefsHelper.getLang(requireContext()) == "ar"){
                        temp.text = "${weatherObj[0].current.temp}" + "س"
                        wind.text = "${weatherObj[0].current.windSpeed}" + "متر/ثانيه"
                    } else if(SharedPrefsHelper.getLang(requireContext()) == "en"){
                        temp.text = "${weatherObj[0].current.temp}" + "C"
                        wind.text = "${weatherObj[0].current.windSpeed} Meter/s"
                    }
                }else if(SharedPrefsHelper.getTempUnit(requireContext()) == "imperial")
                {
                    if(SharedPrefsHelper.getLang(requireContext()) == "ar"){
                        temp.text = "${weatherObj[0].current.temp}" + "ف"
                        wind.text = "${weatherObj[0].current.windSpeed} " + " ميل/ساعه"

                    } else if(SharedPrefsHelper.getLang(requireContext()) == "en"){
                        temp.text = "${weatherObj[0].current.temp}" + "F"
                        wind.text = "${weatherObj[0].current.windSpeed}" + "Mile/h"
                    }

                } else if (SharedPrefsHelper.getTempUnit(requireContext()) == "standard"){
                    if(SharedPrefsHelper.getLang(requireContext()) == "ar"){
                        temp.text = "${weatherObj[0].current.temp} ك "
                        wind.text = "${weatherObj[0].current.windSpeed}" + " متر/ثانيه"
                    } else if(SharedPrefsHelper.getLang(requireContext()) == "en"){
                        temp.text = "${weatherObj[0].current.temp}" + "K"
                        wind.text = "${weatherObj[0].current.windSpeed} " + "Mile/H"
                    }

                }


                Glide.with(descImg).load("http://openweathermap.org/img/w/"+ weatherObj?.get(0)?.current?.weather[0].icon+".png").into(descImg)

                //icon =  TODO: a3ml enum bal swr bta3t al 3agal de al ana ha7otaha
                date.text = timeStampToDate(weatherObj[0].current.dt)
                hummidity.text = weatherObj[0].current.humidity.toString()
                pressure.text = weatherObj[0].current.pressure.toString()
                cloud.text = weatherObj[0].current.clouds.toString()
                ultraViolet.text = weatherObj[0].current.uvi.toString()
                visibility.text = weatherObj[0].current.visibility.toString()
                desc.text = weatherObj[0].current.weather[0].description

            }else{
                var hourlyAdapter = HourlyWeatherAdapter(null , activity?.applicationContext!!)
                hourlyRecyclerView.adapter = hourlyAdapter
                var dailyAdapter = DailyWeatherAdapter(null , activity?.applicationContext!!)
                dailyRecyclerView.adapter = dailyAdapter
                Log.e("NADAAAAA", "onViewCreated: No obj foundddddd", )
            }
        })

    }

    override fun onResume() {
        super.onResume()
        if(!(NetworkChangeReceiver.isThereInternetConnection)){
            Log.e("snackbaaaaaaaaar", "snackbaaaaaaaaar:${NetworkChangeReceiver.isThereInternetConnection} ", )
            showNoNetSnackbar()
        }

        viewModel.getWeatherObj().observe(viewLifecycleOwner , { weatherObj ->

            if(weatherObj != null && weatherObj.isNotEmpty()){
                var hourlyAdapter = HourlyWeatherAdapter(weatherObj[0], requireContext())
                hourlyRecyclerView.adapter = hourlyAdapter
                var dailyAdapter = DailyWeatherAdapter(weatherObj[0], requireContext())
                dailyRecyclerView.adapter = dailyAdapter

                getAddressAndDateForLocation()
                if(SharedPrefsHelper.getTempUnit(requireContext()) == "metric"){
                    if(SharedPrefsHelper.getLang(requireContext()) == "ar"){
                        temp.text = "${weatherObj[0].current.temp}" + "س"
                        wind.text = "${weatherObj[0].current.windSpeed}" + "متر/ثانيه"
                    } else if(SharedPrefsHelper.getLang(requireContext()) == "en"){
                        temp.text = "${weatherObj[0].current.temp}" + "C"
                        wind.text = "${weatherObj[0].current.windSpeed} Meter/s"
                    }
                }else if(SharedPrefsHelper.getTempUnit(requireContext()) == "imperial")
                {
                    if(SharedPrefsHelper.getLang(requireContext()) == "ar"){
                        temp.text = "${weatherObj[0].current.temp}" + "ف"
                        wind.text = "${weatherObj[0].current.windSpeed} " + " ميل/ساعه"

                    } else if(SharedPrefsHelper.getLang(requireContext()) == "en"){
                        temp.text = "${weatherObj[0].current.temp}" + "F"
                        wind.text = "${weatherObj[0].current.windSpeed}" + "Mile/h"
                    }

                } else if (SharedPrefsHelper.getTempUnit(requireContext()) == "standard"){
                    if(SharedPrefsHelper.getLang(requireContext()) == "ar"){
                        temp.text = "${weatherObj[0].current.temp} ك "
                        wind.text = "${weatherObj[0].current.windSpeed}" + " متر/ثانيه"
                    } else if(SharedPrefsHelper.getLang(requireContext()) == "en"){
                        temp.text = "${weatherObj[0].current.temp}" + "K"
                        wind.text = "${weatherObj[0].current.windSpeed} " + "Mile/H"
                    }

                }


                Glide.with(descImg).load("http://openweathermap.org/img/w/"+ weatherObj?.get(0)?.current?.weather[0].icon+".png").into(descImg)

                //icon =  TODO: a3ml enum bal swr bta3t al 3agal de al ana ha7otaha
                date.text = timeStampToDate(weatherObj[0].current.dt)
                hummidity.text = weatherObj[0].current.humidity.toString()
                pressure.text = weatherObj[0].current.pressure.toString()
                cloud.text = weatherObj[0].current.clouds.toString()
                ultraViolet.text = weatherObj[0].current.uvi.toString()
                visibility.text = weatherObj[0].current.visibility.toString()
                desc.text = weatherObj[0].current.weather[0].description

            }else{
                var hourlyAdapter = HourlyWeatherAdapter(null , activity?.applicationContext!!)
                hourlyRecyclerView.adapter = hourlyAdapter
                var dailyAdapter = DailyWeatherAdapter(null , activity?.applicationContext!!)
                dailyRecyclerView.adapter = dailyAdapter
                Log.e("NADAAAAA", "onViewCreated: No obj foundddddd", )
            }
        })

    }

    suspend fun getget(){

        val instance = Repo.getInstance(
            weatherRetrofitClient.getInstance(),
            ConcreateLocalSource(requireContext()),
            requireContext()
        )
        val weatherObjOverNetwork = instance.remoteSource.getWeatherObjOverNetwork(requireContext())
        Log.e("NadaTAG", "getget: ${weatherObjOverNetwork}")
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private fun showNoNetSnackbar() {
        val snack = Snackbar.make(myView, "Please check your internet Connection!", Snackbar.LENGTH_LONG) // replace root view with your view Id
        snack.setAction("Settings") {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        snack.show()
    }

    fun timeStampToDate (dt : Long) : String{
        var date : Date = Date(dt * 1000)
        var dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy")
        return dateFormat.format(date)
    }

    fun getAddressAndDateForLocation(){
        var addressGeocoder : Geocoder = Geocoder(context, Locale.getDefault())
        try {
            var myAddress : List<Address> = addressGeocoder.getFromLocation(SharedPrefsHelper.getLatitude(requireContext()).toDouble(), SharedPrefsHelper.getLongitude(requireContext()).toDouble(), 2)
            if(myAddress.isNotEmpty()){
                placeName.text = "${myAddress[0].subAdminArea}, ${myAddress[0].adminArea}"
                Log.i("NADAAAAAAA", "getAddressForLocation: ${myAddress[0].subAdminArea} ${myAddress[0].adminArea}")
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
    }
}