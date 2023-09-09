package com.furkanharmanci.kotlininstagramsimple

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.furkanharmanci.kotlininstagramsimple.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher : ActivityResultLauncher<String>
    private var selectedPicUri : Uri? = null
    private var imageProvider = Manifest.permission.READ_MEDIA_IMAGES


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()
    }

    fun selectImage(view: View) {

            if (ContextCompat.checkSelfPermission(this,imageProvider) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, imageProvider)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give Permission") {
                            //request permission
                            permissionResultLauncher.launch(imageProvider)
                        }.show()
                } else {
                    // rationale görmek istemezsek yine request permission
                    permissionResultLauncher.launch(imageProvider)
                }
            } else {
                // intent to gallery directly
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentToResult = result.data
                if (intentToResult != null) {
                    selectedPicUri = intentToResult.data
                    selectedPicUri?.let {
                        /// bitmap'e çevirmeye gerek yok. Firebase uri şeklinde alıp upload edebiliyor.
                        binding.imagePicker.setImageURI(it)
                    }
                }
            }
        }
        permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {result ->
            if (result) {
                /// permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                /// permission denied
                Toast.makeText(this@UploadActivity, "Permission Needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadButton(view: View) {

    }
}