package com.example.recipeapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.ViewHolders.MealViewHolder
import com.example.recipeapp.ViewHolders.RecipeViewHolder
import com.example.recipeapp.models.RecipeModel
import com.squareup.picasso.Picasso

class MealsAdapter(private val listener: OnDeleteButtonClick): RecyclerView.Adapter<MealViewHolder>() {
    private val list = ArrayList<RecipeModel>()

    fun updateList(newList: ArrayList<RecipeModel>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_item, parent, false)
        val viewHolder = MealViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val curr = list[position]
        Log.d("myTag", curr.name)
        Picasso.get().load(curr.thumbnail).into(holder.titleImageIV)
        holder.titleTextTV.text = curr.name
        val ingredientsList = curr.ingredients
        var allIngredients = ""
        for(i in ingredientsList.indices){
            if(i == 0){
                allIngredients += ingredientsList[i].name
            }else{
                if(!allIngredients.contains(ingredientsList[i].name) && i<6)
                    allIngredients += ", ${ingredientsList[i].name}"
            }
        }
        holder.ingredientsTV.text = allIngredients


        holder.deleteBtn.setOnClickListener {
            listener.onDeleteMeal(curr)
            list.remove(curr)
            notifyItemRemoved(position)
        }
    }

}

interface OnDeleteButtonClick{
    fun onDeleteMeal(item: RecipeModel)
}

