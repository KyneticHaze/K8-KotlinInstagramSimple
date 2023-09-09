package com.furkanharmanci.kotlininstagramsimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.furkanharmanci.kotlininstagramsimple.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    fun selectImage(view: View) {

    }

    fun uploadButton(view: View) {

    }
}