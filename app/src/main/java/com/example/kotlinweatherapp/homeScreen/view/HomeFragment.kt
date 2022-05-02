package com.example.kotlinweatherapp.homeScreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.model.Weather

class HomeFragment : Fragment() {
    lateinit var dailyRecyclerView : RecyclerView
    lateinit var hourlyRecyclerView : RecyclerView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyRecyclerView = view.findViewById(R.id.recyclerViewDailyId)
        hourlyRecyclerView = view.findViewById(R.id.recyclerViewHourlyId)

        var hourlyLayoutManager = LinearLayoutManager(context)
        hourlyLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyRecyclerView.layoutManager = hourlyLayoutManager

        var hourlyAdapter = HourlyWeatherAdapter(Weather("03:00" , "40C" , "https://pin.it/6Zxukdi" , "04:30 Thurs" , "12C" , "30C" , "https://pin.it/6Zxukdi" , "") ,
            activity?.applicationContext!!)

        hourlyRecyclerView.adapter = hourlyAdapter


        var dailyLayoutManager = LinearLayoutManager(context)
        dailyLayoutManager.orientation = LinearLayoutManager.VERTICAL
        dailyRecyclerView.layoutManager = dailyLayoutManager

        var dailyAdapter = DailyWeatherAdapter(Weather("03:00" , "40C" , "https://pin.it/6Zxukdi" , "04:30 Thurs" , "12C" , "30C" , "https://pin.it/6Zxukdi" , "") ,
            activity?.applicationContext!!)

        dailyRecyclerView.adapter = dailyAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}