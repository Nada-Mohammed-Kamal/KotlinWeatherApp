package com.example.kotlinweatherapp.favscreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.favscreen.FavouritesCommunicator
import com.example.kotlinweatherapp.favscreen.NavigateToHome
import com.example.kotlinweatherapp.homeScreen.view.HomeFragment
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper


class FavAdapter(var fav: List<FavouriteObject>?, var context: Context , var navToHome : NavigateToHome ): RecyclerView.Adapter<FavAdapter.ViewHolder>() ,
    FavouritesCommunicator {
    var myView : View? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var layout = itemView
        var favPlaceTextView = itemView.findViewById<TextView>(R.id.txtViewFavPlaceId)
        var constraintLayoutRow = itemView.findViewById<ConstraintLayout>(R.id.favRowConstraintLayoutId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(context)
        var v = inflater.inflate(R.layout.fav_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //change time zone
        holder.favPlaceTextView.text = fav?.get(position)?.locationName

        holder.constraintLayoutRow.setOnClickListener{
            SharedPrefsHelper.setPreviousLatLng(context , "${SharedPrefsHelper.getLatitude(context)},${SharedPrefsHelper.getLongitude(context)}")
            SharedPrefsHelper.setIsFav(context , true)


            SharedPrefsHelper.setLatitude(context , fav?.get(position)?.latitude.toString())
            SharedPrefsHelper.setLongitude(context , fav?.get(position)?.longitude.toString())

            //SharedPrefsHelper.setIsFav(context , fav?.get(position)?.isFavourite!!)
            navToHome.navigateToHome()
            setFragment(HomeFragment.newInstance())
        }
        //Glide.with(holder.iconImageView).load(sports?.dailyIcon).into(holder.iconImageView)
    }

    override fun getItemCount() : Int{
        return fav?.size!!
    }

    override fun getItemAtIndexToDelete(index: Int): FavouriteObject {
        return fav?.get(index)!!
    }

    fun setFragment(fragment : Fragment){
        //fragment.activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer , fragment , "HomeActivity")?.commit()
    }
}