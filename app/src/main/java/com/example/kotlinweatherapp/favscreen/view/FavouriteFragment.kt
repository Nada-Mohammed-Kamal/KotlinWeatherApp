package com.example.kotlinweatherapp.favscreen.view

import android.Manifest.permission.*
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.map.view.MapsActivity
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.favscreen.FavouritesCommunicator
import com.example.kotlinweatherapp.favscreen.viewmodel.FavFragViewModel
import com.example.kotlinweatherapp.favscreen.viewmodel.FavViewModelFactory
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*


class FavouriteFragment : Fragment() {

    private lateinit var viewModel: FavFragViewModel
    lateinit var favViewModelFactory : FavViewModelFactory


    //maps
    //map checking
    private val TAG = "MAP TAG"
    private val ERROR_DIALOG_REQUEST : Int= 9001

    //recyclerViewFavScreenId
    lateinit var favRecyclerView : RecyclerView
    lateinit var favAdapter : FavAdapter
    lateinit var communicator: FavouritesCommunicator
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var btnAddFav : FloatingActionButton
    lateinit var myView : View
    lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    val PERMISSION_ALL = 1
    var mLocationPermission = false
    val PERMISSIONS = arrayOf(
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    )
    val location_permission_request_code : Int = 1234
    //var location : Location = Location("30.016893,31.377033")
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

    }

    override fun onResume() {
        super.onResume()

        Toast.makeText(requireContext() , "Swipe to delete From Favourite" , Toast.LENGTH_SHORT).show()

        favAdapter.notifyDataSetChanged()
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


        viewModel.getFavourites().observe(viewLifecycleOwner , { favsList ->
            Log.e("NADATAG", "IN OBSERVEEEEE: innnnnnnnnnnnnnnnnnnnnnnn")
            if(favsList != null){
                favAdapter.fav = favsList
                favAdapter.notifyDataSetChanged()
            }
        })
        favAdapter.notifyDataSetChanged()
        Log.e("NADATAG", "onResume: innnnnnnnnnnnnnnnnnnnnnnn")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view

        favRecyclerView = view.findViewById(R.id.recyclerViewFavScreenId)

        btnAddFav = view.findViewById(R.id.btnAddFavId)

        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL


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

            val intent = Intent(requireContext() , MapsActivity::class.java)
            intent.putExtra("fromScreen" , "fav")
            startActivity(intent)
            //getLocationPermission()
            //TODO: araga3 de (working) bs a replace al static location da bal hayarg3 mal map

//            val favouriteObject = FavouriteObject(
//                30.061695,
//                31.458577,
//                getAddressAndDateForLocation(30.061695, 31.458577)
//            )
//            viewModel.addFavourite(favouriteObject)
//            Toast.makeText(requireContext() , "Added to Favourite Places successfully" , Toast.LENGTH_SHORT).show()

            /*TODO: almafrood ageeb al map aw al gps bs wba3d ma adeeb al lat wal log a create fav obj wa a3mlo save fal room wa a
                fal on click listener bta3 al adapter bta3 al fav a call get get obj over network wab3ato fal intent ll home wa3rad
                mmkn a3ml screen t3rd 7aget al fav bs
             */

        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(favRecyclerView)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            location_permission_request_code -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e(TAG, "onRequestPermissionsResult: acceptedddddddddddd", )
                    // Permission is granted.
                    mLocationPermission = true

                    //at2aked mn de
                    //getLocationPermission()
                    //for map checking
                    if (isServiceOkay()){
                        //initialize the map
                        init()
                    }

                } else {
                    Log.e(TAG, "onRequestPermissionsResult: denidedddddddddd", )
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission
                    Toast.makeText(requireContext() , "Please enable the location in order to be able to add a favourite place" , Toast.LENGTH_LONG).show()
                }
            } else ->{
            Toast.makeText(requireContext() , "Please enable the location in order to be able to add a favourite place" , Toast.LENGTH_LONG).show()
        }
        }
    }


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
                var locName = "${myAddress[0].adminArea} , ${myAddress[0].countryName}"
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
                //TODO :- Delete selected fav from database
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

    //MAPS
    fun isServiceOkay() : Boolean{
        Log.e(TAG, "isServiceOkay: Checking google services version" )
        var available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext())
        if(available == ConnectionResult.SUCCESS){
            //every thing is fine and i can make a map request
            Log.e(TAG, "isServiceOkay: Okay :)" )

            return true
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occurred but can be fixed
            Log.e(TAG, "isServiceOkay: Problem can be fixed :|" )

            var dialog : Dialog? = GoogleApiAvailability.getInstance().getErrorDialog(this , available , ERROR_DIALOG_REQUEST)
            dialog?.show()
        } else {
            //i cant make map requests
            Log.e(TAG, "isServiceOkay: problem cant be fixed :(" )

        }
        return false
    }

    private fun init(){
            var i = Intent(requireContext() , MapsActivity::class.java)
            startActivity(i)
    }

}
