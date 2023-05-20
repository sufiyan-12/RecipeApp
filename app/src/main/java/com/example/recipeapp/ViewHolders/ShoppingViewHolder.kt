package com.example.recipeapp.ViewHolders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.recipeapp.R

class ShoppingViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val quantityTV: TextView = view.findViewById(R.id.shoppingItemQuantity)
    val ingredientTV: TextView = view.findViewById(R.id.shoppingItemName)
    val shoppingAddBtn: ImageButton = view.findViewById(R.id.shoppingItemBtn)
}