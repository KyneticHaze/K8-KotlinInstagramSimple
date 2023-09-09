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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher : ActivityResultLauncher<String>
    private var selectedPicUri : Uri? = null
    private var imageProvider = Manifest.permission.READ_MEDIA_IMAGES
    private lateinit var auth : FirebaseAuth
    private lateinit var firestoreDatabase : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // firebase modules initialize
        auth = FirebaseAuth.getInstance()
        firestoreDatabase = FirebaseFirestore.getInstance()
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
        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference
        val uuid = UUID.randomUUID() // rastgele benzersiz id
        val imageName = "$uuid.jpg"
        val imageReference = reference.child("images").child(imageName) // reference.child("images/image.jpg") de olur.

        if (selectedPicUri != null) {
            imageReference.putFile(selectedPicUri!!)
                .addOnSuccessListener {
                    // download url -> firestore
                    val uploadPictureReference = storage.reference.child("images").child(imageName)
                    uploadPictureReference.downloadUrl
                        .addOnSuccessListener {
                            val downloadUrl = it.toString() // download url alındı

                            if (auth.currentUser != null) {
                                val postMap = hashMapOf<String, Any>()
                                postMap["downloadUrl"] = downloadUrl
                                postMap["useremail"] = auth.currentUser!!.email!!
                                postMap["comment"] = binding.comment.text.toString()
                                postMap["date"] = Timestamp.now()

                                firestoreDatabase.collection("Posts")
                                    .add(postMap)
                                    .addOnSuccessListener {
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                                    }
                            }
                        }

                }
                .addOnFailureListener {
                    Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }
}