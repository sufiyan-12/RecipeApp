package com.example.recipeapp.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.recipeapp.R
import com.example.recipeapp.adapters.MealItemClick
import com.example.recipeapp.adapters.SearchAdapter
import com.example.recipeapp.db.RecipeAppDB
import com.example.recipeapp.models.Ingredients
import com.example.recipeapp.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SearchFragment : Fragment(), MealItemClick, SearchView.OnQueryTextListener {

    private val recipesList = ArrayList<RecipeModel>()
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var dbHelper: RecipeAppDB
    private lateinit var pref: SharedPreferences
    private lateinit var pbar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initVariables(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchView.setOnQueryTextListener(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if(recipesList.isEmpty()){
            GlobalScope.launch(Dispatchers.Main){
                getData()
            }
        }
    }

    private fun initVariables(view: View) {
        pbar = view.findViewById(R.id.progressBar)
        dbHelper = RecipeAppDB(requireContext())
        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.search_recyclerview)
        adapter = SearchAdapter(this)
        recyclerView.adapter = adapter
        pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()
        val email = pref.getString("user_email", "").toString().trim()
        val password = pref.getString("user_password", "").toString().trim()
        editor.putString("user_id", dbHelper.getUserId(email, password))
        editor.apply()
    }

    private fun getData() {
        val url: String = "https://www.themealdb.com/api/json/v1/1/search.php?s"
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response->

//                recipesList.clear()
                recyclerView.visibility = View.VISIBLE
                pbar.visibility = View.GONE

                val ingredientList = ArrayList<Ingredients>()

                try{
                    val meals: JSONArray = response.getJSONArray("meals")
                    for(i in 0 until meals.length()){
                        val meal: JSONObject = meals.getJSONObject(i)
                        val id = meal.getString("idMeal")
                        val name = meal.getString("strMeal")
                        val category = meal.getString("strCategory")
                        val instructions = meal.getString("strInstructions")
                        val thumbnail = meal.getString("strMealThumb")
                        val youtube = meal.getString("strYoutube")
                        for (j in 1..20){
                            val ingredient = meal.getString("strIngredient$j")
                            val quantity = meal.getString("strMeasure$j")
                            if(ingredient.isNotEmpty() && quantity.isNotEmpty()){
                                ingredientList.add(Ingredients(ingredient, quantity))
                            }
                        }
                        recipesList.add(RecipeModel(id, name, category, instructions, thumbnail, youtube, 0,ingredientList))
                    }
                    adapter.updateList(recipesList)
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            },{
                recyclerView.visibility = View.GONE
                pbar.visibility = View.VISIBLE
                Toast.makeText(requireContext(),"Try after sometime!!", Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(jsonObjectRequest)
    }

    override fun onMealItemClick(item: RecipeModel) {
        val bundle = Bundle()
        bundle.putString("id", item.id)
        bundle.putString("name", item.name)
        bundle.putString("category", item.category)
        bundle.putString("instructions", item.instructions)
        bundle.putString("thumbnail", item.thumbnail)
        bundle.putString("youtube", item.youtube)
        bundle.putInt("favorite", item.favorite)
        bundle.putSerializable("ingredients", item.ingredients)
        val displayFragment = DisplayFragment()
        displayFragment.arguments = bundle
        val ft = requireActivity().supportFragmentManager.beginTransaction()
        ft.remove(SearchFragment())
        ft.add(R.id.mainHost, displayFragment)
        ft.addToBackStack("display")
        ft.commit()
    }

    override fun onFavoriteClick(item: RecipeModel) {
        val id = pref.getString("user_id", "").toString()

        GlobalScope.launch(Dispatchers.IO) {
            dbHelper.updateFavoriteById(id, item.id.toInt(), item.favorite == 0)
        }
        if(item.favorite == 0){
            Toast.makeText(requireContext(), "Marked Favorite!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddMealClick(item: RecipeModel) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = pref.getString("user_id", "").toString()
            dbHelper.addMeal(item, id)
            for(i in item.ingredients){
                dbHelper.addIngredient(Ingredients(i.name, i.quantity),id, item.id)
            }
        }
        Toast.makeText(requireContext(), "Meal added to your Meal Plan!", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if(recipesList.isEmpty()){
            Toast.makeText(requireContext(), "No data available right now!!", Toast.LENGTH_SHORT).show()
            return false
        }else{
            val newList = ArrayList<RecipeModel>()
            for(item in recipesList){

                if(item.name.toLowerCase().contains(query.trim().toLowerCase()) ||
                    item.category.toLowerCase().contains(query.trim().toLowerCase()) ||
                        item.instructions.toLowerCase().contains(query.trim().toLowerCase())){
                    newList.add(item)
                }

                for(ingredient in item.ingredients){
                    if (ingredient.name.toLowerCase().contains(query.trim().toLowerCase()) && !newList.contains(item)){
                        newList.add(item)
                    }
                }
            }
            adapter.updateList(newList)
            return true
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}