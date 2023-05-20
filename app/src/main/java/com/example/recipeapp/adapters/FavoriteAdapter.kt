package com.example.recipeapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.ViewHolders.RecipeViewHolder
import com.example.recipeapp.models.RecipeModel
import com.squareup.picasso.Picasso

class FavoriteAdapter(private val listener: OnItemClick) : RecyclerView.Adapter<RecipeViewHolder>() {

    private val list = ArrayList<RecipeModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        val viewHolder = RecipeViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            listener.onFavoriteItemClick(list[viewHolder.adapterPosition])
        }

        viewHolder.favoriteIB.setOnClickListener {
            listener.onUnFavorite(list[viewHolder.adapterPosition])
            list.remove(list[viewHolder.adapterPosition])
            notifyItemRemoved(viewHolder.adapterPosition)
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
        holder.addMealBtn.visibility = View.GONE
    }

    fun updateList(newList: ArrayList<RecipeModel>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
interface OnItemClick{
    fun onFavoriteItemClick(item: RecipeModel)
    fun onUnFavorite(item: RecipeModel)
}