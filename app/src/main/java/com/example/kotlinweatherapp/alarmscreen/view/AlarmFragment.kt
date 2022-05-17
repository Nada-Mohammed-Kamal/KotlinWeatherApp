package com.example.kotlinweatherapp.alarmscreen.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.alarmscreen.AlarmCommunicator
import com.example.kotlinweatherapp.alarmscreen.viewmodel.AlarmFragViewModel
import com.example.kotlinweatherapp.alarmscreen.viewmodel.AlarmViewModelFactory
import com.example.kotlinweatherapp.homeScreen.view.DailyWeatherAdapter
import com.example.kotlinweatherapp.homeScreen.view.HourlyWeatherAdapter
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.repo.Repo
import com.example.kotlinweatherapp.models.retrofit.weatherRetrofitClient
import com.example.kotlinweatherapp.models.room.ConcreateLocalSource
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.example.kotlinweatherapp.workmanager.WeatherWorkerClass
import com.example.kotlinweatherapp.workmanager.WorkerUtilsClass
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AlarmFragment : Fragment() , AlarmCommunicator{
    lateinit var alarmsRecyclerView : RecyclerView
    lateinit var alarmAdapter : AlarmAdapter
    lateinit var addAlarmBtn : FloatingActionButton
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var myView : View

    private lateinit var viewModel: AlarmFragViewModel
    lateinit var alarmFactory : AlarmViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmFactory = AlarmViewModelFactory(
            Repo.getInstance(
                weatherRetrofitClient.getInstance() ,
                ConcreateLocalSource(requireContext()),  requireContext()) , requireContext())
        viewModel = ViewModelProvider(this, alarmFactory).get(AlarmFragViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAlarms().observe(viewLifecycleOwner , Observer {alarmsList ->
            Log.e("NADATAG", "IN OBSERVEEEEE: innnnnnnnnnnnnnnnnnnnnnnn", )
            if(alarmsList != null){
                alarmAdapter.alarms = alarmsList
                alarmAdapter.notifyDataSetChanged()
            }
        })
        //alarmAdapter.notifyDataSetChanged()
        Log.e("NADATAG", "onResume: innnnnnnnnnnnnnnnnnnnnnnn", )

        if(!(NetworkChangeReceiver.isThereInternetConnection)){
            Log.e("snackbaaaaaaaaar", "snackbaaaaaaaaar:${NetworkChangeReceiver.isThereInternetConnection} ", )
            showNoNetSnackbar()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view

        addAlarmBtn = view.findViewById(R.id.btnAddAlarmId)
        alarmsRecyclerView = view.findViewById(R.id.recyclerViewAlarmsId)
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        //view model
        alarmFactory = AlarmViewModelFactory(
            Repo.getInstance(
                weatherRetrofitClient.getInstance() ,
                ConcreateLocalSource(requireContext()),  requireContext()) , requireContext())
        viewModel = ViewModelProvider(this, alarmFactory).get(AlarmFragViewModel::class.java)

        alarmAdapter = AlarmAdapter(listOf<Alarm>()) {
           viewDialog(it)
        }
        alarmsRecyclerView.layoutManager = linearLayoutManager
        alarmsRecyclerView.adapter = alarmAdapter





        // TODO: fadel al delete wal add buttons bal methods bta3ethom


        addAlarmBtn.setOnClickListener{
            if (checkFirstTime()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkDrawOverlayPermission()
                    setNotFirstTime()
                } else {
                    navigateToCreateAlarmDialog()
                }
            } else {
                navigateToCreateAlarmDialog()
            }
        }
    }

    private fun navigateToCreateAlarmDialog() {
        var i = Intent(context, AlarmScrActivity::class.java)
        startActivity(i)
    }

    private fun setNotFirstTime() {
        SharedPrefsHelper.setIsFirstTimeAddAlarm(requireContext() ,false)
    }

    private fun checkFirstTime(): Boolean {
        return   SharedPrefsHelper.getIsFirstTimeForAddAlarm(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkDrawOverlayPermission() {
        // Check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(requireContext())) {
            // if not construct intent to request permission
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Alert")
                .setMessage("please give the app the ability to draw over other apps")
                .setPositiveButton("Okay") { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireActivity().packageName)
                    )
                    // request permission via start activity for result
                    startActivityForResult(intent, 1)
                    //It will call onActivityResult Function After you press Yes/No and go Back after giving permission
                    dialog.dismiss()

                }.setNegativeButton(
                    "No"
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlarmFragment()
    }

//    override fun deleteAlarm(alarmObj: Alarm) {
////        alarmFactory = AlarmViewModelFactory(
////            Repo.getInstance(
////                weatherRetrofitClient.getInstance() ,
////                ConcreateLocalSource(activity?: requireContext()),  requireActivity()) , requireActivity())
////        viewModel = ViewModelProvider(this, alarmFactory).get(AlarmFragViewModel::class.java)
//        viewModel.deleteAlarm(alarmObj)
//    }

     fun viewDialog(alarmObj : Alarm) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                //TODO :- Delete selected alarm from database
                //delete
                viewModel.deleteAlarm(alarmObj)


                ////TODO(Important) :- Delete selected alarm from work manager (make sure that this is the right alarm id)
                WorkerUtilsClass.deleteRequestFromWorkManagerByReq(alarmObj.id)



                alarmAdapter.notifyDataSetChanged()
                //al sa7 an al toast de tab2a fal view model msh hena
                Toast.makeText(context , "deleted Successfully" , Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                Toast.makeText(context , "Not deleted" , Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }


    private fun showNoNetSnackbar() {
        val snack = Snackbar.make(myView, "Please check your internet Connection!", Snackbar.LENGTH_LONG) // replace root view with your view Id
        snack.setAction("Settings") {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        snack.show()
    }




}