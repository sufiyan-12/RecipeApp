package com.example.recipeapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.recipeapp.db.RecipeAppDB
import com.example.recipeapp.screens.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener(this)
        supportFragmentManager.beginTransaction().add(R.id.host, SearchFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        when(item.itemId){
            R.id.search -> {
                ft.replace(R.id.host, SearchFragment())
                ft.commit()
            }
            R.id.shopping -> {
                ft.replace(R.id.host, ShoppingFragment())
                ft.commit()
            }
            R.id.meal_planning -> {
                ft.replace(R.id.host, MealsFragment())
                ft.commit()
            }
            R.id.favorite -> {
                ft.replace(R.id.host, FavoriteFragment())
                ft.commit()
            }
            R.id.account -> {
                ft.replace(R.id.host, AccountFragment())
                ft.commit()
            }
        }
        return true
    }
}