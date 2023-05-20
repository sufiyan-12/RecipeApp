package com.example.recipeapp.ViewHolders

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R

class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val recipeImageIV: ImageView = itemView.findViewById(R.id.recipe_item_image)
    val recipeNameTV: TextView = itemView.findViewById(R.id.recipe_item_name)
    val recipeCategoryTV: TextView = itemView.findViewById(R.id.recipe_item_category)
    val favoriteIB: ImageButton = itemView.findViewById(R.id.recipe_item_favorite_IB)
    val addMealBtn: ImageButton = itemView.findViewById(R.id.recipe_add_meal_btn)
    val bottomLinearLayout: LinearLayout = itemView.findViewById(R.id.recipe_item_bottom_layout)
}