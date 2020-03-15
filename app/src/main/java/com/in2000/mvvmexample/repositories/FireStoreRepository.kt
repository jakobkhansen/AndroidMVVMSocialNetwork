package com.in2000.mvvmexample.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.in2000.mvvmexample.models.Post
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.in2000.mvvmexample.models.User


class FireStoreRepository {
    var fireStore = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    var user = auth.currentUser

    init {
        val  settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        fireStore.firestoreSettings = settings
    }

    fun savePost(post : Post) {
        val uid = user?.uid ?: ""
        fireStore.collection("users").document(uid).collection("posts").add(post)
    }

    suspend fun fetchAllPosts() : List<Post> {
        val posts = mutableListOf<Post>()
        val docRef = fireStore.collectionGroup("posts")
        Log.d("Before query", "Before")
        val result = docRef.orderBy("date", Query.Direction.DESCENDING).get().await()
        Log.d("After query", "After")
        for (document in result) {
            val post =
            posts.add(document.toObject(Post::class.java))
        }

        return posts
    }


    suspend fun fetchPostsById(uid : String) : List<Post> {
        val posts = mutableListOf<Post>()
        val docRef = fireStore.collection("users").document(uid).collection("posts")
        val result = docRef.orderBy("date", Query.Direction.DESCENDING).get().await()
        for (document in result.documents) {
            val post = document.toObject(Post::class.java) ?: Post()
            posts.add(post)
        }

        return posts
    }

    suspend fun fetchUserById(uid : String) : User {
        val docRef = fireStore.collection("users").document(uid)
        val result = docRef.get().await()

        return result.toObject(User::class.java) ?: User()
    }

    fun addUser(user : User) {
        val uid = user.userId ?: ""
        val docRef = fireStore.collection("users").document(uid)
        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result!!
                if (!document.exists()) {
                    docRef.set(user)
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}