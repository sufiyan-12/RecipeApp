package com.example.recipeapp.models

data class RecipeModel(
    val id: String,
    val name: String,
    val category: String,
    val instructions: String,
    val thumbnail: String,
    val youtube: String,
    var favorite: Int,
    val ingredients: ArrayList<Ingredients>
    ) : java.io.Serializable
