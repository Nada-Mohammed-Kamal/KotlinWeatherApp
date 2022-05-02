package com.example.kotlinweatherapp.alarmscreen.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.model.Weather
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlarmFragment : Fragment(){
    lateinit var alarmsRecyclerView : RecyclerView
    lateinit var alarmAdapter : AlarmAdapter
    lateinit var addAlarmBtn : FloatingActionButton
    lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        addAlarmBtn = view.findViewById(R.id.btnAddAlarmId)
        alarmsRecyclerView = view.findViewById(R.id.recyclerViewAlarmsId)
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        alarmAdapter = AlarmAdapter(Weather("" , "" , "" , "" , "" , ""
        ,"" , "Nasr City") , requireContext())

        alarmsRecyclerView.layoutManager = linearLayoutManager
        alarmsRecyclerView.adapter = alarmAdapter

        addAlarmBtn.setOnClickListener{
            var i = Intent(context, AlarmScrActivity::class.java)
            startActivity(i)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlarmFragment()
    }


}