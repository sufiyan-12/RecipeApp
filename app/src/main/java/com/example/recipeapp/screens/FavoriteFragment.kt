package com.example.recipeapp.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapters.FavoriteAdapter
import com.example.recipeapp.adapters.OnItemClick
import com.example.recipeapp.db.RecipeAppDB
import com.example.recipeapp.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(), OnItemClick {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteAdapter
    private lateinit var dbHelper: RecipeAppDB
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerView = view.findViewById(R.id.favorite_recyclerview)
        adapter = FavoriteAdapter(this)
        recyclerView.adapter = adapter
        dbHelper = RecipeAppDB(requireContext())
        pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = pref.edit()
        val email = pref.getString("user_email", "").toString().trim()
        val password = pref.getString("user_password", "").toString().trim()
        editor.putString("user_id", dbHelper.getUserId(email, password))
        editor.apply()
        return view
    }

    override fun onResume() {
        val list = dbHelper.getFavoriteMeals(pref.getString("user_id", "").toString())
        adapter.updateList(list)
        super.onResume()
    }

    override fun onFavoriteItemClick(item: RecipeModel) {
    }

    override fun onUnFavorite(item: RecipeModel) {
        GlobalScope.launch(Dispatchers.IO) {
            dbHelper.updateFavoriteById(pref.getString("user_id","")!!, item.id.toInt(), false)
        }
        Toast.makeText(requireContext(), "Removed from favorites!", Toast.LENGTH_SHORT).show()
    }
}