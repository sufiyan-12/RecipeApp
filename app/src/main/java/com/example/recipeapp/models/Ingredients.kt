package com.example.recipeapp.models

import java.io.Serializable

data class Ingredients(
    val name: String,
    val quantity: String
): Serializable
