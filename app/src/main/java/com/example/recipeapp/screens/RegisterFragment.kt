package com.example.recipeapp.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.MainActivity
import com.example.recipeapp.R
import com.example.recipeapp.db.RecipeAppDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {


    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginTV: TextView
    private lateinit var pref: SharedPreferences
    private lateinit var dbHelper: RecipeAppDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        emailEdt = view.findViewById(R.id.idRegisterEmailEdt)
        passwordEdt = view.findViewById(R.id.idRegisterPassEdt)
        registerBtn = view.findViewById(R.id.idRegisterBtn)
        loginTV = view.findViewById(R.id.idGoForLoginTV)

        loginTV.setOnClickListener {
            val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            ft.replace(R.id.loginHost, LoginFragment())
            ft.addToBackStack("register")
            ft.commit()
        }

        pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        dbHelper = RecipeAppDB(activity?.applicationContext!!)

        registerBtn.setOnClickListener {
            if(emailEdt.text.isNotEmpty() && passwordEdt.text.isNotEmpty()) {
                val email = emailEdt.text.toString().trim()
                val password = passwordEdt.text.toString().trim()

                if (!isEmailCorrect(email)) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Please enter correct email like example@gmail.com",
                        Toast.LENGTH_LONG
                    ).show()

                } else if (!isPasswordCorrect(password)) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Password must contain at least 8 characters with at least one uppercase letter, one lowercase letter, and one number",
                        Toast.LENGTH_LONG
                    ).show()
                }else{

                    val editor = pref.edit()
                    editor.putString("user_email", email)
                    editor.putString("user_password", password)
                    editor.apply()


                    if (dbHelper.isUserAvailable(email, password)) {
                        Toast.makeText(
                            activity?.applicationContext,
                            "Account already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        GlobalScope.launch(Dispatchers.IO) {
                            dbHelper.addUser(email, password)
                        }
                    }

                    // starts home activity
                    startActivity(Intent(activity, MainActivity::class.java))

                }
            }else{
                Toast.makeText(activity?.applicationContext, "Please give email and password", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }


    private fun isEmailCorrect(email: String) : Boolean{
        if(email.length < 10) return false
        if(!email.contains('@')) return false
        if(!email.contains(".com")) return false
        return true
    }

    private fun isPasswordCorrect(password: String) : Boolean{
        val specialChars = arrayOf(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+',
            '=', '{', '}', '[', ']', '|', '\\', ':', ';', '<', '>', ',', '.', '?', '/'
        )

        if(password.length < 8) return false
        var containsSpecial = false
        for(i in specialChars){
            if(password.contains(i)){
                containsSpecial = true
                break
            }
        }
        if(!containsSpecial){
            return false
        }
        val numerals = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        var containsNumber = false
        for(i in numerals){
            if(password.contains(i)){
                containsNumber = true
                break
            }
        }

        if(!containsNumber) return false

        return true
    }
}
