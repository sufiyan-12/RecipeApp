package com.example.recipeapp.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapters.MealsAdapter
import com.example.recipeapp.adapters.OnDeleteButtonClick
import com.example.recipeapp.db.RecipeAppDB
import com.example.recipeapp.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MealsFragment : Fragment(), OnDeleteButtonClick {
    private val mealsList = ArrayList<RecipeModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MealsAdapter
    private lateinit var dbHelper: RecipeAppDB
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meals, container, false)

        pref = activity?.getSharedPreferences("user",Context.MODE_PRIVATE)!!
        dbHelper = RecipeAppDB(requireContext())
        recyclerView = view.findViewById(R.id.meals_recyclerview)
        adapter = MealsAdapter(this)
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        GlobalScope.launch(Dispatchers.Main) {
            if (mealsList.isEmpty()) {
                val id = pref.getString("user_id", "")!!
                val list = dbHelper.getAllMeals(id)
                mealsList.clear()
                mealsList.addAll(list)
            }
            adapter.updateList(mealsList)
        }
        super.onResume()
    }
    override fun onDeleteMeal(item: RecipeModel) {
        val id = pref.getString("user_id", "").toString()
        if(dbHelper.deleteMealById(id, item.id.toInt()) && dbHelper.deleteIngredientsOfMeals(id, item.id.toString())){
            Toast.makeText(requireContext(), "${item.name} deleted!", Toast.LENGTH_SHORT).show()
        }
    }

}