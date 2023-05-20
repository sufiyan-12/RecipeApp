package com.example.recipeapp.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapters.DisplayAdapter
import com.example.recipeapp.adapters.OnBtnClick
import com.example.recipeapp.db.RecipeAppDB
import com.example.recipeapp.models.Ingredients
import com.squareup.picasso.Picasso

class DisplayFragment : Fragment(), OnBtnClick {

    private lateinit var displayIV: ImageView
    private lateinit var recipeNameTV: TextView
    private lateinit var instructionsTV: TextView
    private lateinit var displayRecyclerView: RecyclerView
    private lateinit var adapter: DisplayAdapter
    private lateinit var dbHelper: RecipeAppDB
    private lateinit var pref: SharedPreferences
    private val ingredientList = ArrayList<Ingredients>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_diplay, container, false)

        initVariables(view)

        val id = arguments?.getString("id")
        val name = arguments?.getString("name")
        val category = arguments?.getString("category")
        val instructions = arguments?.getString("instructions")
        val thumbnail = arguments?.getString("thumbnail")
        val youtube = arguments?.getString("youtube")
        val favorite = arguments?.getInt("favorite")
        val ingredients = arguments?.getSerializable("ingredients") as ArrayList<Ingredients>
        Picasso.get().load(thumbnail).into(displayIV)
        recipeNameTV.text = name
        instructionsTV.text = instructions

        if (ingredients != null)
            ingredientList.addAll(ingredients)
        return view
    }

    private fun initVariables(view: View) {
        displayIV = view.findViewById(R.id.displayImage)
        recipeNameTV = view.findViewById(R.id.displayRecipeName)
        instructionsTV = view.findViewById(R.id.displayInstructions)
        displayRecyclerView = view.findViewById(R.id.displayRecyclerView)
        adapter = DisplayAdapter(this, ingredientList)
        displayRecyclerView.adapter = adapter
    }

    override fun onAddClick(item: Ingredients) {

    }
}