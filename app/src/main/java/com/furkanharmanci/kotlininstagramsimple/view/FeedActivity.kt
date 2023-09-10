package com.furkanharmanci.kotlininstagramsimple.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkanharmanci.kotlininstagramsimple.R
import com.furkanharmanci.kotlininstagramsimple.adapter.PostAdapter
import com.furkanharmanci.kotlininstagramsimple.databinding.ActivityFeedBinding
import com.furkanharmanci.kotlininstagramsimple.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.collections.ArrayList

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firestoreDatabase : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // firebase initialize
        auth = FirebaseAuth.getInstance()
        firestoreDatabase = FirebaseFirestore.getInstance()

        // arrayList initialize
        postArrayList = ArrayList()

        getData()

        binding.recycler.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postArrayList)
        binding.recycler.adapter = postAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        firestoreDatabase.collection("Posts")
            .orderBy("date", Query.Direction.DESCENDING) // son eklenen en başta çıkıyor.
            .addSnapshotListener { value, error ->
                when (error) {
                    null -> {
                        if (value != null) {
                            val documents = value.documents

                            postArrayList.clear() // döngü ile tek tek yazdırılmadan önce listeyi temizleme

                            if (!value.isEmpty) {
                                for (document in documents) {
                                    val comment = document["comment"] as String
                                    val userEmail = document["useremail"] as String
                                    val downloadUrl = document["downloadUrl"] as String

                                    val post = Post(userEmail, comment, downloadUrl)
                                    postArrayList.add(post)
                                }

                                postAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(this@FeedActivity, error.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflaterInit = menuInflater
        menuInflaterInit.inflate(R.menu.insta_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addPost) {
            val intent = Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.signOut) {
            auth.signOut()
            val intent = Intent(this@FeedActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}