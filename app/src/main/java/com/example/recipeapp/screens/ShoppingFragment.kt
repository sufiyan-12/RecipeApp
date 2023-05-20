package com.example.recipeapp.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapters.OnRemoveClick
import com.example.recipeapp.adapters.ShopAdapter
import com.example.recipeapp.db.RecipeAppDB
import com.example.recipeapp.models.Ingredients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShoppingFragment : Fragment(), OnRemoveClick {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter
    private lateinit var dbHelper: RecipeAppDB
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_shopping, container, false)
        initVariables(view)
        return view
    }

    override fun onResume() {
        val id = pref.getString("user_id", "").toString()
        val list = dbHelper.getShoppingList(id)
        adapter.updateList(list)
        super.onResume()
    }

    private fun initVariables(view: View) {
        recyclerView = view.findViewById(R.id.shopping_recyclerview)
        adapter = ShopAdapter(this)
        recyclerView.adapter = adapter
        dbHelper = RecipeAppDB(requireContext())
        pref = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)!!
    }

    override fun onRemoveBtnClick(item: Ingredients) {
        val id = pref.getString("user_id", "").toString()
        GlobalScope.launch(Dispatchers.Main){
            if(dbHelper.deleteIngredientById(id, item)){
                Toast.makeText(requireContext(), "${item.name} is deleted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}