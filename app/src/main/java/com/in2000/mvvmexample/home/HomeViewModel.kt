package com.in2000.mvvmexample.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.in2000.mvvmexample.models.Post
import com.in2000.mvvmexample.repositories.FireStoreRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var fireStoreRepository = FireStoreRepository()
    var postLiveData = MutableLiveData<List<Post>>()

    init {
        updatePosts()
    }

    fun updatePosts() {
        GlobalScope.launch {
            val posts = fireStoreRepository.fetchAllPosts()
            postLiveData.postValue(posts)
        }
    }

    fun populateList() {
        val list = mutableListOf<Post>()

        list.add(Post(Timestamp.now(), "Test post", "Jakob Hansen"))
        list.add(Post(Timestamp.now(), "Test post", "Jakob Hansen"))
        postLiveData.value = list
    }

    fun createPost(content : String) : Post{
        val post = Post(Timestamp.now(), content, fireStoreRepository.user?.displayName, fireStoreRepository.user?.uid, fireStoreRepository.user?.photoUrl.toString())
        return post
    }

    fun signOut() {
        fireStoreRepository.signOut()
    }
}