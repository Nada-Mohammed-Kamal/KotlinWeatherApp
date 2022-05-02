package com.example.kotlinweatherapp.settingsscreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton

import android.widget.RadioGroup
import android.widget.Toast
import com.example.kotlinweatherapp.R

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
        addListenerOnButton();
    }

    private fun addListenerOnButton() {
        //radio groups
        langRadioGroup = myView?.findViewById(R.id.radioGroupLangId)
        locationRadioGroup = myView?.findViewById(R.id.radioGroupLocId)
        windRadioGroup = myView?.findViewById(R.id.radioGroupWindId)
        tempRadioGroup = myView?.findViewById(R.id.radioGroupTempId)

//        //radio buttons
//        arabicRadioButton = myView?.findViewById(R.id.radioButtonEnglishId)
//        englishRadioButton = myView?.findViewById(R.id.radioButtonArabicId)
//        milePerHourRadioButton = myView?.findViewById(R.id.radioButtonMilePerHourId)
//        meterPerSecRadioButton = myView?.findViewById(R.id.radioButtonMeterPerSecId)
//        mapRadioButton = myView?.findViewById(R.id.radioButtonMapId)
//        gpsRadioButton = myView?.findViewById(R.id.radioButtonGPSId)
//        celsiusRadioButton = myView?.findViewById(R.id.radioButtonCelisiusId)
//        fahrenheitRadioButton = myView?.findViewById(R.id.radioButtonFehrenhitId)
//        kelvinRadioButton = myView?.findViewById(R.id.radioButtonKelvinId)


        //button
        btnApply = myView?.findViewById(R.id.btnApplySettingsScreenId);
        langRadioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonEnglishId -> {Toast.makeText(context , "english" , Toast.LENGTH_SHORT).show()}
                R.id.radioButtonArabicId -> {Toast.makeText(context , "arabic" , Toast.LENGTH_SHORT).show()}
            }
        })
        locationRadioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonMapId -> {Toast.makeText(context , "map" , Toast.LENGTH_SHORT).show()}
                R.id.radioButtonGPSId -> {Toast.makeText(context , "gps" , Toast.LENGTH_SHORT).show()}
            }
        })
        windRadioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonMilePerHourId -> {Toast.makeText(context , "milePerHour" , Toast.LENGTH_SHORT).show()}
                R.id.radioButtonMeterPerSecId -> {Toast.makeText(context , "meterPerSec" , Toast.LENGTH_SHORT).show()}
            }
        })
        tempRadioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonCelisiusId -> {Toast.makeText(context , "Celsius" , Toast.LENGTH_SHORT).show()}
                R.id.radioButtonFehrenhitId -> {Toast.makeText(context , "Fahrenheit" , Toast.LENGTH_SHORT).show()}
                R.id.radioButtonKelvinId -> {Toast.makeText(context , "Kelvin" , Toast.LENGTH_SHORT).show()}
            }
        })

        btnApply?.setOnClickListener{
            //change the app settings
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
}