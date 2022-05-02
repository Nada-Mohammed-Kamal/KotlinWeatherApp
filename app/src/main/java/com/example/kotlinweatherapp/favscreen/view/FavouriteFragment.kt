package com.example.kotlinweatherapp.favscreen.view

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

class FavouriteFragment : Fragment() {
    //recyclerViewFavScreenId
    lateinit var favRecyclerView : RecyclerView
    lateinit var favAdapter : FavAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var btnAddFav : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        favRecyclerView = view.findViewById(R.id.recyclerViewFavScreenId)

        btnAddFav = view.findViewById(R.id.btnAddFavId)

        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        favAdapter = FavAdapter(
            Weather("" , "" , "" , "" , "" , ""
            ,"" , "Nasr City") , requireContext())

        favRecyclerView.layoutManager = linearLayoutManager
        favRecyclerView.adapter = favAdapter

        btnAddFav.setOnClickListener{
            //open map
        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }
}