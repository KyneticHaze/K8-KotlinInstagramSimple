package com.furkanharmanci.kotlininstagramsimple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.furkanharmanci.kotlininstagramsimple.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser // güncel kullanıcı
        if (currentUser != null) {
            val intent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signInButton(view: View) {
        email = binding.email.text.toString()
        password = binding.password.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this@MainActivity, "Enter email or password!", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val intent = Intent(this@MainActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }
    fun signUpButton(view: View) {
        email = binding.email.text.toString()
        password = binding.password.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this@MainActivity, "Enter email or password!", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    val intent = Intent(this@MainActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}