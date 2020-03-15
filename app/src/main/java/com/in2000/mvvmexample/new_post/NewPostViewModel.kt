package com.in2000.mvvmexample.new_post

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.in2000.mvvmexample.models.Post
import com.in2000.mvvmexample.repositories.FireStoreRepository

class NewPostViewModel : ViewModel() {

    private val fireStoreRepository = FireStoreRepository()

    fun createAndSavePost(content : String) {
        val post = createPostObj(content)
        addPost(post)
    }

    private fun createPostObj(content : String) : Post {
        val post = Post(Timestamp.now(), content, fireStoreRepository.user?.displayName, fireStoreRepository.user?.uid, fireStoreRepository.user?.photoUrl.toString())
        return post
    }

    private fun addPost(post : Post) {
        fireStoreRepository.savePost(post)
    }
}