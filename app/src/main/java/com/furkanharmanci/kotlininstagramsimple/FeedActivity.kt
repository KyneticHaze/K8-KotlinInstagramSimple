package com.furkanharmanci.kotlininstagramsimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.furkanharmanci.kotlininstagramsimple.databinding.ActivityFeedBinding
import com.furkanharmanci.kotlininstagramsimple.databinding.ActivityMainBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}