package com.example.kotlinweatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kotlinweatherapp.alarmscreen.view.AlarmFragment
import com.example.kotlinweatherapp.favscreen.view.FavouriteFragment
import com.example.kotlinweatherapp.homeScreen.view.HomeFragment
import com.example.kotlinweatherapp.settingsscreen.view.SettingsFragment

class TabPageAdapter (activity : FragmentActivity , private val tabCount : Int) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> HomeFragment.newInstance()
            1 -> FavouriteFragment.newInstance()
            2 -> AlarmFragment.newInstance()
            3 -> SettingsFragment.newInstance()

            else -> {HomeFragment.newInstance()}
        }
    }
}