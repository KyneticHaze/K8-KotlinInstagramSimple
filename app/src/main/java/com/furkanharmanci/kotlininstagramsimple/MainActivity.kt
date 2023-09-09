package com.furkanharmanci.kotlininstagramsimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.furkanharmanci.kotlininstagramsimple.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        
        // Initialize
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            recreate()
        }
    }

    fun signInButton(view: View) {

    }
    fun signUpButton(view: View) {

    }
}