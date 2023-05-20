package com.example.recipeapp.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.recipeapp.LoginActivity
import com.example.recipeapp.R
import com.example.recipeapp.db.RecipeAppDB

class AccountFragment : Fragment() {
    private lateinit var dbHelper: RecipeAppDB
    private lateinit var pref: SharedPreferences
    private lateinit var userNameTV: TextView
    private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        dbHelper = RecipeAppDB(requireContext())
        pref = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)!!
        userNameTV = view.findViewById(R.id.userNameTV)
        userNameTV.text = pref.getString("user_email", "").toString()
        logoutBtn = view.findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            val editor = pref.edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
        return view
    }
}