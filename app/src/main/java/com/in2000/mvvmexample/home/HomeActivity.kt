package com.in2000.mvvmexample.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.in2000.mvvmexample.R
import com.in2000.mvvmexample.auth.SignInActivity
import com.in2000.mvvmexample.models.PostAdapter
import com.in2000.mvvmexample.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), PostAdapter.PostClickListener {

    private val homeViewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.updatePosts()
    }

    private fun setupUI() {
        observeRecyclerView()
        initButtons()
    }

    private fun observeRecyclerView() {
        homeViewModel.postLiveData.observe(this, Observer {
            progress.visibility = View.VISIBLE
            val postAdapter = PostAdapter(it, this)
            val postLayoutManager = LinearLayoutManager(this)

            home_recycler.apply {
                setHasFixedSize(true)
                adapter = postAdapter
                layoutManager = postLayoutManager
            }

            progress.visibility = View.INVISIBLE
        })
    }

    private fun initButtons() {
        sign_out.setOnClickListener {
            homeViewModel.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        my_profile.setOnClickListener {
            val uid = homeViewModel.fireStoreRepository.user?.uid
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userId", uid)
            startActivity(intent)
        }
    }


    override fun onPostClick(position: Int) {
        val viewHolder = home_recycler.findViewHolderForLayoutPosition(position) as PostAdapter.PostHolder
        val userId = viewHolder.userId
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}
