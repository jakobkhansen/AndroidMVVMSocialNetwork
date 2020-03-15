package com.in2000.mvvmexample.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.in2000.mvvmexample.auth.SignInRepository

class SignInViewModel: ViewModel() {
    lateinit var userLiveData : LiveData<FirebaseUser>
    private var signInRepository = SignInRepository()

    fun signInWithGoogle(account : AuthCredential) {
        userLiveData = signInRepository.firebaseSignInWithAuthCredential(account)
    }

    fun checkSignedIn() : Boolean {
        return signInRepository.signedIn
    }
}