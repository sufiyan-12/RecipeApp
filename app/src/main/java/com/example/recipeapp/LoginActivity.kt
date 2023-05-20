package com.example.recipeapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.recipeapp.screens.RegisterFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pref = getSharedPreferences("user", Context.MODE_PRIVATE)

        if(pref.getString("user_email", "")!!.isEmpty()){
            supportFragmentManager.beginTransaction().add(R.id.loginHost, RegisterFragment()).commit()
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}