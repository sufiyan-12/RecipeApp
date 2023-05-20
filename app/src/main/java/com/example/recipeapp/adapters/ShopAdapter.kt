package com.example.recipeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.ViewHolders.ShoppingViewHolder
import com.example.recipeapp.models.Ingredients
import com.example.recipeapp.models.RecipeModel

class ShopAdapter(
    private val listener: OnRemoveClick) :
    RecyclerView.Adapter<ShoppingViewHolder>(){

    private val list = ArrayList<Ingredients>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        val viewHolder = ShoppingViewHolder(view)

        viewHolder.shoppingAddBtn.setOnClickListener {
            listener.onRemoveBtnClick(list[viewHolder.adapterPosition])
            list.remove(list[viewHolder.adapterPosition])
            notifyItemRemoved(viewHolder.adapterPosition)
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val cur = list[position]
        holder.ingredientTV.text = cur.name
        holder.quantityTV.text = cur.quantity
        holder.shoppingAddBtn.setBackgroundResource(R.drawable.ic_remove)
    }

    fun updateList(newList: ArrayList<Ingredients>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}

interface OnRemoveClick{
    fun onRemoveBtnClick(item: Ingredients)
}