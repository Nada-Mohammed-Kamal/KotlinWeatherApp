package com.example.kotlinweatherapp.map.view
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.MainActivity
import com.example.kotlinweatherapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.kotlinweatherapp.databinding.ActivityMapsBinding
import com.example.kotlinweatherapp.map.viewmodel.MapViewModel
import com.example.kotlinweatherapp.map.viewmodel.MapViewModelFactory
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.example.kotlinweatherapp.settingsscreen.SettingsAndMapCommunicator
import com.example.kotlinweatherapp.settingsscreen.viewmodel.SettingsFragViewModel
import com.example.kotlinweatherapp.settingsscreen.viewmodel.SettingsViewModelFactory
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import java.io.IOException
import java.util.*

class MapsActivity() : AppCompatActivity(), OnMapReadyCallback{
    //viewModel
    private lateinit var viewModel: MapViewModel
    lateinit var mapFactory : MapViewModelFactory

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var saveBtn : Button

    private lateinit var currentLocation : LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveBtn = findViewById(R.id.saveLocInMapBtnId)


        saveBtn.visibility = View.INVISIBLE


        saveBtn.setOnClickListener {
            val screen = intent.getStringExtra("fromScreen")
            when (screen){
                "fav" -> {
                    //TODO:get instance of view model and call
//                    var latlng = "${SharedPrefsHelper.getLatitude(this)},${SharedPrefsHelper.getLongitude(this)}"
//                    SharedPrefsHelper.setPreviousLatLng(this , latlng)
                    val favouriteObject = FavouriteObject(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        getAddressAndDateForLocation(
                            currentLocation.latitude,
                            currentLocation.longitude
                        )
                    )
                    viewModel.addFavourite(favouriteObject)
                    finish()
                }
                "settings" -> {
                    var latlng = "${SharedPrefsHelper.getLatitude(this)},${SharedPrefsHelper.getLongitude(this)}"
                    SharedPrefsHelper.setPreviousLatLng(this , latlng)
                    SharedPrefsHelper.setLatitude(this , currentLocation.latitude.toString())
                    SharedPrefsHelper.setLongitude(this , currentLocation.longitude.toString())
                    finish()
                }
                "gpsOrMap" -> {
                    var latlng = "${SharedPrefsHelper.getLatitude(this)},${SharedPrefsHelper.getLongitude(this)}"
                    SharedPrefsHelper.setPreviousLatLng(this , latlng)
                    SharedPrefsHelper.setLatitude(this , currentLocation.latitude.toString())
                    SharedPrefsHelper.setLongitude(this , currentLocation.longitude.toString())
                    val i = Intent(this , MainActivity::class.java)
                    startActivity(i)
                }
            }
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocAndViewThePin()

        mMap.setOnMapClickListener {
            mMap.clear()
            currentLocation = it
            val addressInText =
                getAddressAndDateForLocation(it.latitude, it.longitude)
            Log.e("LocationTAG", "onMapReady: $addressInText")
            mMap.addMarker(MarkerOptions().position(it).title(addressInText))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            saveBtn.visibility = View.VISIBLE
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    private fun getLocAndViewThePin(){
        // Add a marker in Sydney and move the camera
        val currentLoc = LatLng(26.8206 , 30.8025)
        val addressInText =
            getAddressAndDateForLocation(currentLoc.latitude, currentLoc.longitude)
        Log.e("LocationTAG", "onMapReady: $addressInText")

        mMap.addMarker(MarkerOptions().position(currentLoc).title(addressInText))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc))
    }

    //fromScreen --> gpsOrMap , settings , fav

    private fun getAddressAndDateForLocation(lat : Double, long : Double) : String{
        val addressGeocoder = Geocoder(this, Locale.getDefault())
        try {
            val myAddress : List<Address> = addressGeocoder.getFromLocation(lat, long, 2)
            if(myAddress.isNotEmpty()){
                Log.i("NADAAAAAAA", "getAddressForLocation: ${myAddress[0].subAdminArea} ${myAddress[0].adminArea}")
                return "${myAddress[0].subAdminArea}, ${myAddress[0].adminArea}"
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
        return ""
    }


    override fun onResume() {
        super.onResume()
        mapFactory = MapViewModelFactory(
            Repo.getInstance(
                weatherRetrofitClient.getInstance() ,
                ConcreateLocalSource(this),  this) , this)
        viewModel = ViewModelProvider(this, mapFactory).get(MapViewModel::class.java)
    }



}