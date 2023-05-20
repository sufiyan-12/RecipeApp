package com.example.recipeapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.ViewHolders.RecipeViewHolder
import com.example.recipeapp.models.RecipeModel
import com.squareup.picasso.Picasso

class SearchAdapter(
    private val listener: MealItemClick
    ): RecyclerView.Adapter<RecipeViewHolder>() {

    private val list = ArrayList<RecipeModel>()
    private var isFavorite = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        val viewHolder = RecipeViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            listener.onMealItemClick(list[viewHolder.adapterPosition])
        }

        viewHolder.addMealBtn.setOnClickListener {
            listener.onAddMealClick(list[viewHolder.adapterPosition])
        }
        viewHolder.favoriteIB.setOnClickListener {
            listener.onFavoriteClick(list[viewHolder.adapterPosition])
            if(isFavorite){
                list[viewHolder.adapterPosition].favorite = 0
            }else{
                list[viewHolder.adapterPosition].favorite = 1
            }
            notifyItemChanged(viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val cur = list[position]
        Picasso.get().load(cur.thumbnail).into(holder.recipeImageIV)
        holder.recipeNameTV.text = cur.name
        if(cur.favorite == 1){
            holder.favoriteIB.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF0000"))
        }
        holder.recipeCategoryTV.text = cur.category


    }

    fun updateList(newList: ArrayList<RecipeModel>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}

interface MealItemClick{
    fun onMealItemClick(item: RecipeModel)
    fun onFavoriteClick(item: RecipeModel)
    fun onAddMealClick(item: RecipeModel)
}