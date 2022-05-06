package com.example.kotlinweatherapp.favscreen.view

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.favscreen.FavouritesCommunicator
import com.example.kotlinweatherapp.favscreen.viewmodel.FavFragViewModel
import com.example.kotlinweatherapp.favscreen.viewmodel.FavViewModelFactory
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*


class FavouriteFragment : Fragment() {

    private lateinit var viewModel: FavFragViewModel
    lateinit var FavFactory : FavViewModelFactory

    //recyclerViewFavScreenId
    lateinit var favRecyclerView : RecyclerView
    lateinit var favAdapter : FavAdapter
    lateinit var communicator: FavouritesCommunicator
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var btnAddFav : FloatingActionButton
    lateinit var myView : View
    lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    val PERMISSION_ALL = 1
    val PERMISSIONS = arrayOf(
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    )
    //var location : Location = Location("30.016893,31.377033")
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var FavViewModel: FavFragViewModel
    lateinit var favViewModelFactory : FavViewModelFactory

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

//
//        if (!hasPermissions(requireContext(), *PERMISSIONS)) {
//            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)
//        }

    }

    override fun onResume() {
        super.onResume()
        if(!(NetworkChangeReceiver.isThereInternetConnection)){
            Log.e(
                "snackbaaaaaaaaar",
                "snackbaaaaaaaaar:${NetworkChangeReceiver.isThereInternetConnection} "
            )
            showNoNetSnackbar()
        }


        favViewModelFactory = FavViewModelFactory(
            Repo.getInstance(
                weatherRetrofitClient.getInstance() ,
                ConcreateLocalSource(requireContext()),  requireContext()) , requireContext())
        viewModel = ViewModelProvider(this, favViewModelFactory).get(FavFragViewModel::class.java)


        viewModel.getFavourites().observe(viewLifecycleOwner , androidx.lifecycle.Observer { favsList ->
            Log.e("NADATAG", "IN OBSERVEEEEE: innnnnnnnnnnnnnnnnnnnnnnn")
            if(favsList != null){
                favAdapter.fav = favsList
                favAdapter.notifyDataSetChanged()
            }
        })
        //alarmAdapter.notifyDataSetChanged()
        Log.e("NADATAG", "onResume: innnnnnnnnnnnnnnnnnnnnnnn")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }



    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view

        favRecyclerView = view.findViewById(R.id.recyclerViewFavScreenId)

        btnAddFav = view.findViewById(R.id.btnAddFavId)

        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL





//        //view model
//        favViewModelFactory = FavViewModelFactory(
//            Repo.getInstance(
//                weatherRetrofitClient.getInstance() ,
//                ConcreateLocalSource(requireContext()),  requireContext()) , requireContext())
//        FavViewModel = ViewModelProvider(this, favViewModelFactory).get(FavFragViewModel::class.java)
//        val favourites = FavViewModel.getFavourites()
//        //put it back
//        favourites?.observe(viewLifecycleOwner , androidx.lifecycle.Observer { favs ->
//            if(favs != null){
//                favAdapter = FavAdapter( favs, requireContext())
//                favRecyclerView.layoutManager = linearLayoutManager
//                favRecyclerView.adapter = favAdapter
//            }else{
//                favAdapter = FavAdapter( favs , requireContext())
//                favRecyclerView.layoutManager = linearLayoutManager
//                favRecyclerView.adapter = favAdapter
//                Log.e("FromAlarmFragment", "onViewCreated: :List of favourites is null", )
//            }
//        })

        //view model
        favViewModelFactory = FavViewModelFactory(
            Repo.getInstance(
                weatherRetrofitClient.getInstance() ,
                ConcreateLocalSource(requireContext()),  requireContext()) , requireContext())
        viewModel = ViewModelProvider(this, favViewModelFactory).get(FavFragViewModel::class.java)


        favAdapter = FavAdapter(listOf<FavouriteObject>() , requireContext())
        favRecyclerView.layoutManager = linearLayoutManager
        favRecyclerView.adapter = favAdapter

        communicator = favAdapter



        btnAddFav.setOnClickListener{

            val favouriteObject = FavouriteObject(
                30.061695,
                31.458577,
                getAddressAndDateForLocation(30.061695, 31.458577)
            )
            viewModel.addFavourite(favouriteObject)
            Toast.makeText(requireContext() , "Added to Favourite Places successfully" , Toast.LENGTH_SHORT).show()

            //TODO: araga3 de bs azabatha
        //check permission
//            if (!hasPermissions(requireContext(), *PERMISSIONS)) {
//                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL);
//            } else {
//
//
//                //getLastKnownLocation()
//
//
//
//
//
//            //open map 3al current location
////                val lm = getSystemService(Context.APP_OPS_SERVICE) as ?
////                val location: Location? = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
////                val longitude: Double = location!!.longitude
////                val latitude: Double = location.latitude
////                val geocoder: Geocoder
////                val address: List<Address>
////                geocoder = Geocoder(requireContext(), Locale.getDefault())
////
////                address = geocoder.getFromLocation(latitude, longitude, 1)
////                Handler().postDelayed(Runnable {
////                    val uri =
////                        "http://maps.google.com/maps?saddr=$latitude,$longitude"// + "&daddr=" + destinationLatitude.toString() + "," + destinationLongitude
////                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
////                    startActivity(intent)
////                }, 1000)
//            }


//            val geocoder: Geocoder
//            val address: List<Address>
//            geocoder = Geocoder(this, Locale.getDefault())

//            address = geocoder.getFromLocation(latitude, longitude, 1)
//            Handler().postDelayed(Runnable {
//                val uri =
//                    "http://maps.google.com/maps?saddr=" + sourceLatitude.toString() + "," + sourceLongitude.toString() + "&daddr=" + destinationLatitude.toString() + "," + destinationLongitude
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//                startActivity(intent)
//            }, 1000)


            /*TODO: almafrood ageeb al map aw al gps bs wba3d ma adeeb al lat wal log a create fav obj wa a3mlo save fal room wa a
                fal on click listener bta3 al adapter bta3 al fav a call get get obj over network wab3ato fal intent ll home wa3rad
                mmkn a3ml screen t3rd 7aget al fav bs
             */

        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(favRecyclerView)
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    val longitude: Double = location!!.longitude
                    val latitude: Double = location.latitude
                    val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val address: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
                    Handler().postDelayed(Runnable {
                        //"http://maps.google.com/maps?saddr="
                        val uri =
                            "geo:$latitude,$longitude?z=11"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latitude,$longitude"))
                        val chooser = Intent.createChooser(intent , "Launch Maps")
                        startActivity(chooser)
                    }, 500)
                }

            }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
//            PERMISSION_REQUEST_CODE -> {
//                // If request is cancelled, the result arrays are empty.
//                if ((grantResults.isNotEmpty() &&
//                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // Permission is granted. Continue the action or workflow
//                    // in your app.
//                } else {
//                    // Explain to the user that the feature is unavailable because
//                    // the features requires a permission that the user has denied.
//                    // At the same time, respect the user's decision. Don't link to
//                    // system settings in an effort to convince the user to change
//                    // their decision.
//                }
//                return
//            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
//            else -> {
//                // Ignore all other requests.
//            }

        }
    }

//    fun onMapReady(googleMap: GoogleMap) {
//        val currentLocation = LatLng(location.getLatitude(), location.getLongitude())
//        googleMap.addMarker(
//            MarkerOptions().position(currentLocation)
//                .title("Current Location")
//        )
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
//    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

//    fun getCurrentLocation(){
//        val lm = getSystemService(LOCATION_SERVICE) as LocationManager?
//        val location: Location? = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        val longitude: Double = location!!.longitude
//        val latitude: Double = location.latitude
//    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }


    private fun showNoNetSnackbar() {
        val snack = Snackbar.make(myView, "Please check your internet Connection!", Snackbar.LENGTH_LONG) // replace root view with your view Id
        snack.setAction("Settings") {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        snack.show()
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

    var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            Toast.makeText(requireContext(), "on Move", Toast.LENGTH_SHORT).show()
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            Toast.makeText(requireContext(), "on Swiped ", Toast.LENGTH_SHORT).show()
            //Remove swiped item from list and notify the RecyclerView
            val position = viewHolder.adapterPosition
            viewDialog(communicator.getItemAtIndexToDelete(position))
        }
    }

    //TODO Anadeha fal swipe
    fun viewDialog(favObj : FavouriteObject) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                //TODO :- Delete selected alarm from database
                //delete
                viewModel.deleteFavourite(favObj)
                favAdapter.notifyDataSetChanged()
                //al sa7 an al toast de tab2a fal view model msh hena
                Toast.makeText(context , "deleted Successfully" , Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                Toast.makeText(context , "Not deleted" , Toast.LENGTH_SHORT).show()
                favAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

}
