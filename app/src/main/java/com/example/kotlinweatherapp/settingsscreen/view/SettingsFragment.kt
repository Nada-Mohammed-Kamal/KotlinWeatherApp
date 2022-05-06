package com.example.kotlinweatherapp.settingsscreen.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton

import android.widget.RadioGroup
import android.widget.Toast
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.google.android.material.snackbar.Snackbar
import kotlin.math.log

class SettingsFragment : Fragment() {

    //radio groups
    private var langRadioGroup: RadioGroup? = null
    private var windRadioGroup: RadioGroup? = null
    private var locationRadioGroup: RadioGroup? = null
    private var tempRadioGroup: RadioGroup? = null

    //radio buttons
    private var arabicRadioButton: RadioButton? = null
    private var englishRadioButton: RadioButton? = null
    private var milePerHourRadioButton: RadioButton? = null
    private var meterPerSecRadioButton: RadioButton? = null
    private var mapRadioButton: RadioButton? = null
    private var gpsRadioButton: RadioButton? = null
    private var celsiusRadioButton: RadioButton? = null
    private var fahrenheitRadioButton: RadioButton? = null
    private var kelvinRadioButton: RadioButton? = null

    //selected radio buttons
    private var langRadioButtonSelected: RadioButton? = null
    private var windSelectedRadioButton: RadioButton? = null
    private var locSelectedRadioButton: RadioButton? = null
    private var tempSelectedRadioButton: RadioButton? = null

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
                }
                R.id.radioButtonGPSId -> {
                    Log.e("NEWWWWWWWWWWWWWWW", "addListenerOnButton: gps", )
                    //Toast.makeText(context, "gps", Toast.LENGTH_SHORT).show()
                    location = "gps"
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

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    private fun showNoNetSnackbar() {
        val snack = Snackbar.make(myView!!, "Please check your internet Connection!", Snackbar.LENGTH_LONG) // replace root view with your view Id
        snack.setAction("Settings") {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        snack.show()
    }


}