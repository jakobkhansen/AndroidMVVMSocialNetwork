package com.in2000.mvvmexample.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.in2000.mvvmexample.models.Post
import com.in2000.mvvmexample.models.User
import com.in2000.mvvmexample.repositories.FireStoreRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileViewModel(userId : String) : ViewModel() {
    val fireStoreRepository = FireStoreRepository()
    val userLiveData = MutableLiveData<User>()
    val postsLiveData = MutableLiveData<List<Post>>()
    val isAuthenticatedAsProfile = MutableLiveData<Boolean>()

    val uid = userId

    init {
        initUserInfo()
        updatePosts()
        authenticatedAsProfile()
    }

    fun initUserInfo() {
        GlobalScope.launch {
            val user = fireStoreRepository.fetchUserById(uid)
            userLiveData.postValue(user)
        }
    }

    fun updatePosts() {
        GlobalScope.launch {
            val posts = fireStoreRepository.fetchPostsById(uid)
            postsLiveData.postValue(posts)
        }
    }

    fun authenticatedAsProfile() {
        isAuthenticatedAsProfile.value = fireStoreRepository.user?.uid == uid
    }

}