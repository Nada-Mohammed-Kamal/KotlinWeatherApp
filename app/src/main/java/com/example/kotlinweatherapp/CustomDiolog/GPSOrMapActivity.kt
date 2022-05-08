package com.example.kotlinweatherapp.CustomDiolog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.kotlinweatherapp.MainActivity
import com.example.kotlinweatherapp.map.view.MapsActivity
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GPSOrMapActivity : AppCompatActivity() {
    lateinit var gpsBtn : Button
    lateinit var mapBtn : Button

    var chosenBtnIsMap : Boolean = false

    //getCurrentLocation
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpsor_map)

        //to not open the dialog again
        SharedPrefsHelper.setIsFirstTime(this , false)

        //btns
        gpsBtn = findViewById(R.id.btnUseGPSId)
        mapBtn = findViewById(R.id.btnUseMapId)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        gpsBtn.setOnClickListener {
            //currentLoc
            getCurrentLoc()
        }

        mapBtn.setOnClickListener {
            chosenBtnIsMap = true
            val intent = Intent(this , MapsActivity::class.java)
            //a7ot 7aga 3al intent 3ahsan a3rf hnak ana gaya mn meen fa law mn al GPSorMap Screen send 0 to save in shared prefs
            //wlw mn settins screen kaman
            //laken law mn al favs ma3mlsh save fal shared prefs
            intent.putExtra("fromScreen" , "gpsOrMap")
            startActivity(intent)
        }
    }


    //currentLoc
    private fun getCurrentLoc()
    {
        if(checkPermission())
        {
            if(isLocationEnabeled())
            {
                //find lat and long
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task ->
                    val location : Location? = task.result
                    if(location==null)
                    {
                        Toast.makeText(this , "Null location returned" , Toast.LENGTH_LONG).show()
                    }
                    else
                    {
//                        if(isMap){
//                            //the map doesn't need permission
//                        } else{
                            Toast.makeText(this , "location returned successfully lat is : ${location.latitude} and long is : ${location.longitude} " , Toast.LENGTH_LONG).show()
                            var latlng = "${SharedPrefsHelper.getLatitude(this)},${SharedPrefsHelper.getLongitude(this)}"
                            SharedPrefsHelper.setPreviousLatLng(this , latlng)
                            SharedPrefsHelper.setLatitude(this , (location.latitude).toString())
                            SharedPrefsHelper.setLongitude(this , (location.longitude).toString())
                            val intent = Intent(this , MainActivity::class.java)
                            startActivity(intent)
//                        }
                    }
                }
            }
            else
            {
                //setting open here
                Toast.makeText(this , "Please turn on Location" , Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else
        {
            //request permission here
            requestPermission()
        }
    }

    private fun isLocationEnabeled(): Boolean {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this as Activity,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION
                ,android.Manifest.permission.ACCESS_FINE_LOCATION) , 1234)
    }

    private fun checkPermission(): Boolean {
        if(ActivityCompat.checkSelfPermission(this ,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this ,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }


    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this , "Permission Granted" , Toast.LENGTH_LONG).show()
                getCurrentLoc()
            }
            else{
                Toast.makeText(this , "Permission Denied , Please Enable The Location in order to get you the Weather" , Toast.LENGTH_LONG).show()
            }
        }
    }

}