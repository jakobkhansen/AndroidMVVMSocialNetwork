package com.in2000.mvvmexample.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.in2000.mvvmexample.models.User
import com.in2000.mvvmexample.repositories.FireStoreRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignInRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    var signedIn = firebaseAuth.currentUser != null
    var fireStoreRepository = FireStoreRepository()

    fun firebaseSignInWithAuthCredential(account : AuthCredential) : MutableLiveData<FirebaseUser> {
        val fireBaseUserLiveData = MutableLiveData<FirebaseUser>()
        firebaseAuth.signInWithCredential(account).addOnCompleteListener {
            if (it.isSuccessful) {
                val firebaseUser = firebaseAuth.currentUser
                val user = User(firebaseUser?.uid, firebaseUser?.displayName, firebaseUser?.photoUrl.toString())
                registerUserIfNotExists(user)

                fireBaseUserLiveData.value = firebaseUser
                signedIn = true
            } else {
                Log.d("SignInRepository", it.exception?.message ?: "")
            }
        }
        return fireBaseUserLiveData
    }

    fun registerUserIfNotExists(user : User) {
        fireStoreRepository.addUser(user)
    }
}