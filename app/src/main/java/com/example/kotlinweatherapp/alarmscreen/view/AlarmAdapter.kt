package com.example.kotlinweatherapp.alarmscreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.R


class AlarmAdapter(var weather: Weather?, var context: Context): RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    var myView : View? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var layout = itemView
        var alarmPlaceTextView = itemView.findViewById<TextView>(R.id.txtViewAlarmPlaceId)
        var deleteAlarmBtn = itemView.findViewById<ImageButton>(R.id.btnDeleteAlarmId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(context)
        var v = inflater.inflate(R.layout.alarm_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.alarmPlaceTextView.text = weather?.alarmPlace
        holder.deleteAlarmBtn.setOnClickListener{
            //atala3 confirmation dialog
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->

                    //TODO :- Delete selected alarm from database

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
        //Glide.with(holder.iconImageView).load(sports?.dailyIcon).into(holder.iconImageView)
    }

    override fun getItemCount() = 4//3dd al alarms al 3ndy ya fal db ya fal obj
}