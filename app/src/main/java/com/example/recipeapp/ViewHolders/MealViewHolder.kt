package com.example.recipeapp.ViewHolders

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R

class MealViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val titleImageIV: ImageView = view.findViewById(R.id.meal_image)
    val titleTextTV: TextView = view.findViewById(R.id.meal_title_text)
    val ingredientsTV: TextView = view.findViewById(R.id.meal_ingredients)
    val deleteBtn: ImageButton = view.findViewById(R.id.meal_delete_btn)
}