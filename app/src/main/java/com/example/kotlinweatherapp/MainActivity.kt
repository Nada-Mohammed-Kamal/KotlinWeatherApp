package com.example.kotlinweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.kotlinweatherapp.alarmscreen.view.AlarmFragment
import com.example.kotlinweatherapp.favscreen.view.FavouriteFragment
import com.example.kotlinweatherapp.homeScreen.view.HomeFragment
import com.example.kotlinweatherapp.settingsscreen.view.SettingsFragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var i = Intent(this , AlarmScrActivity::class.java)
//        startActivity(i)

        val bubbleNavLinearView = findViewById<BubbleNavigationLinearView>(R.id.bubbleNavigationBarid)
        setFragment(HomeFragment.newInstance())

        bubbleNavLinearView.setNavigationChangeListener{view , position ->
            when(position){
                0-> setFragment(HomeFragment.newInstance())
                1-> setFragment(FavouriteFragment.newInstance())
                2-> setFragment(AlarmFragment.newInstance())
                3-> setFragment(SettingsFragment.newInstance())
            }
        }

    }

    fun setFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer , fragment , "HomeActivity")
            .commit()
    }
}