package com.example.kotlinweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlinweatherapp.alarmscreen.view.AlarmFragment
import com.example.kotlinweatherapp.favscreen.view.FavouriteFragment
import com.example.kotlinweatherapp.homeScreen.view.HomeFragment
import com.example.kotlinweatherapp.settingsscreen.view.SettingsFragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTabBar()
//        val bubbleNavLinearView = findViewById<BubbleNavigationLinearView>(R.id.bubbleNavigationBarid)
//        setFragment(HomeFragment.newInstance())
//
//        bubbleNavLinearView.setNavigationChangeListener{view , position ->
//            when(position){
//                0-> setFragment(HomeFragment.newInstance())
//                1-> setFragment(FavouriteFragment.newInstance())
//                2-> setFragment(AlarmFragment.newInstance())
//                3-> setFragment(SettingsFragment.newInstance())
//            }
//        }
    }

//    fun setFragment(fragment : Fragment){
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer , fragment , "HomeActivity")
//            .commit()
//    }

    fun setUpTabBar(){
        val adapter = TabPageAdapter(this , tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}