package com.example.recipeapp.adapters

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.recipeapp.R
import com.example.recipeapp.ViewHolders.ShoppingViewHolder
import com.example.recipeapp.models.Ingredients

class DisplayAdapter(private val listener: OnBtnClick, private val list: ArrayList<Ingredients>) : RecyclerView.Adapter<ShoppingViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        val viewHolder = ShoppingViewHolder(view)
        viewHolder.shoppingAddBtn.setOnClickListener {
            listener.onAddClick(list[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        Log.d("myTag", list.size.toString())
        val curr = list[position]
        holder.quantityTV.text = curr.quantity
        holder.ingredientTV.text = curr.name
    }

}
interface OnBtnClick{
    fun onAddClick(item: Ingredients)
}