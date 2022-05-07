package com.example.kotlinweatherapp.settingsscreen.view

import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.map.view.MapsActivity
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.example.kotlinweatherapp.settingsscreen.SettingsAndMapCommunicator
import com.example.kotlinweatherapp.settingsscreen.viewmodel.SettingsFragViewModel
import com.example.kotlinweatherapp.settingsscreen.viewmodel.SettingsViewModelFactory
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*

class SettingsFragment : Fragment() , SettingsAndMapCommunicator{

    //getCurrentLocation
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //viewModel
    private lateinit var viewModel: SettingsFragViewModel
    lateinit var settingFactory : SettingsViewModelFactory

    //radio groups
    private var langRadioGroup: RadioGroup? = null
    private var windRadioGroup: RadioGroup? = null
    private var locationRadioGroup: RadioGroup? = null
    private var tempRadioGroup: RadioGroup? = null

    var language : String = ""
    var wind : String = ""
    var temprature : String = ""
    var location : String = ""


    //button
    private var btnApply: Button? = null

    //view
    private var myView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())


        myView = view
        language =SharedPrefsHelper.getLang(requireContext())
        temprature = SharedPrefsHelper.getTempUnit(requireContext())
        addListenerOnButton();

    }

    override fun onResume() {
        super.onResume()
        addListenerOnButton()
        if(!(NetworkChangeReceiver.isThereInternetConnection)){
            Log.e("snackbaaaaaaaaar", "snackbaaaaaaaaar:${NetworkChangeReceiver.isThereInternetConnection} ", )
            showNoNetSnackbar()
        }

        settingFactory = SettingsViewModelFactory(
            Repo.getInstance(
                weatherRetrofitClient.getInstance() ,
                ConcreateLocalSource(requireContext()),  requireContext()) , requireContext())
        viewModel = ViewModelProvider(this, settingFactory).get(SettingsFragViewModel::class.java)
    }

    private fun addListenerOnButton() {
        //radio groups
        langRadioGroup = myView?.findViewById(R.id.radioGroupLangId)
        locationRadioGroup = myView?.findViewById(R.id.radioGroupLocId)
        windRadioGroup = myView?.findViewById(R.id.radioGroupWindId)
        tempRadioGroup = myView?.findViewById(R.id.radioGroupTempId)

        //button
        btnApply = myView?.findViewById(R.id.btnApplySettingsScreenId);

        langRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonEnglishId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: english", )
                    //Toast.makeText(context, "english", Toast.LENGTH_SHORT).show()
                    language = "en"
                }
                R.id.radioButtonArabicId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: arabic", )
                    //Toast.makeText(context, "arabic", Toast.LENGTH_SHORT).show()
                    language = "ar"
                }
            }
        }
        locationRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonMapId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: map", )
                    //Toast.makeText(context, "map", Toast.LENGTH_SHORT).show()
                    location = "map"
                    val intent = Intent(requireContext() , MapsActivity::class.java)
                    intent.putExtra("fromScreen" , "settings")
                    startActivity(intent)
                }
                R.id.radioButtonGPSId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: gps", )
                    //Toast.makeText(context, "gps", Toast.LENGTH_SHORT).show()
                    location = "gps"
                    getCurrentLoc()
                }
            }
        }
        windRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonMilePerHourId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: milePerHour", )
//                    Toast.makeText(context, "milePerHour", Toast.LENGTH_SHORT).show()
                    wind = "Mile/hour"
                    temprature = "imperial"
                }
                R.id.radioButtonMeterPerSecId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: meterPerSec", )
//                    Toast.makeText(context, "meterPerSec", Toast.LENGTH_SHORT).show()
                    wind = "Meter/sec"
                    temprature = "metric"
                }
            }
        }
        tempRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonCelisiusId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: celisus", )
//                    Toast.makeText(context, "Celsius", Toast.LENGTH_SHORT).show()
                    temprature = "metric"
                }
                R.id.radioButtonFehrenhitId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: fehrinhit", )
//                    Toast.makeText(context, "Fahrenheit", Toast.LENGTH_SHORT).show()
                    temprature = "imperial"
                }
                R.id.radioButtonKelvinId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: kelvin", )
//                    Toast.makeText(context, "Kelvin", Toast.LENGTH_SHORT).show()
                    temprature = "standard"
                }
            }
        }

        //TODO : on click listener ba2a 3al map wal gps
        btnApply?.setOnClickListener{
            SharedPrefsHelper.setLanguage(requireContext() , language)
            SharedPrefsHelper.setTempUnit(requireContext() , temprature)
            Log.e("SETINGSSCREEEEEN", "appl button: al lang $language wal temp $temprature")
            Toast.makeText(requireContext(), "Changes Applied Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    private fun showNoNetSnackbar() {
        val snack = Snackbar.make(myView!!, "Please check your internet Connection!", Snackbar.LENGTH_LONG) // replace root view with your view Id
        snack.setAction("Settings") {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        snack.show()
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
                        Toast.makeText(requireContext() , "Null location returned" , Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(requireContext() , "location returned successfully lat is : ${location.latitude} and long is : ${location.longitude} " , Toast.LENGTH_LONG).show()
                        SharedPrefsHelper.setLatitude(requireContext() , (location.latitude).toString())
                        SharedPrefsHelper.setLongitude(requireContext() , (location.longitude).toString())
                    }
                }
            }
            else
            {
                //setting open here
                Toast.makeText(requireContext() , "Please turn on Location" , Toast.LENGTH_SHORT).show()
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
        val locationManager : LocationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION
                ,android.Manifest.permission.ACCESS_FINE_LOCATION) , 1234)
    }

    private fun checkPermission(): Boolean {
        if(ActivityCompat.checkSelfPermission(requireContext() ,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext() ,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }


    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

        @JvmStatic
        fun newInstance() = SettingsFragment()
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
                Toast.makeText(requireContext() , "Permission Granted" , Toast.LENGTH_LONG).show()
                getCurrentLoc()
            }
            else{
                Toast.makeText(requireContext() , "Permission Denied , Please Enable The Location in order to get you the Weather" , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun addTOFav(location: LatLng) {
        val addressInTxt = getAddressAndDateForLocation(location.latitude, location.longitude)
        val favouriteObject = FavouriteObject(location.latitude, location.longitude, addressInTxt)
        viewModel.addFavourite(favouriteObject)
        Toast.makeText(requireContext() , "Favourite place added successfully" , Toast.LENGTH_SHORT).show()
    }

    fun getAddressAndDateForLocation(lat : Double , long : Double) : String{
        //GPSLat GPSLong
        var addressGeocoder : Geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            var myAddress : List<Address> = addressGeocoder.getFromLocation(lat , long, 2)
            if(myAddress.isNotEmpty()){
                var locName = "${myAddress[0].subAdminArea}, ${myAddress[0].adminArea}"
                Log.i("ConvertLocInFavFrag", "getAddressForLocation: ${myAddress[0].subAdminArea} ${myAddress[0].adminArea}")
                return locName
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
        return ""
    }
}