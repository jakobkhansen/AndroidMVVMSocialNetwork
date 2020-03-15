package com.in2000.mvvmexample.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.in2000.mvvmexample.R
import com.in2000.mvvmexample.models.PostAdapter
import com.in2000.mvvmexample.new_post.NewPostActivity
import com.in2000.mvvmexample.repositories.FireStoreRepository
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity(), PostAdapter.PostClickListener {

    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViewModel()
        initUser()
        initPosts()
        checkPostButton()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.updatePosts()
    }


    fun initViewModel() {
        try {
            val userId = intent.getStringExtra("userId") ?: throw Exception()
            profileViewModel = ProfileViewModel(userId)
        } catch (e : java.lang.Exception) {
            finish()
        }
    }

    fun initUser() {
        profileViewModel.userLiveData.observe(this, Observer {
            profile_name.text = it.userDisplayName
            Glide.with(profile_image).load(it.userImageUrl).into(profile_image)
        })
    }

    fun checkPostButton() {
        profileViewModel.isAuthenticatedAsProfile.observe(this, Observer {
            if (it) {
                new_post_button.show()
                new_post_button.setOnClickListener {
                    val intent = Intent(this, NewPostActivity::class.java)
                    startActivity(intent)
                }
            } else {
                new_post_button.hide()
                new_post_button.setOnClickListener(null)
            }
        })
    }

    fun initPosts() {

        profileViewModel.postsLiveData.observe(this, Observer {
            val postAdapter = PostAdapter(it, this)
            val postLayoutManager = LinearLayoutManager(this)

            profile_posts.apply {
                setHasFixedSize(true)
                adapter = postAdapter
                layoutManager = postLayoutManager
            }
        })
    }

    override fun onPostClick(position: Int) {
        return
    }
}
