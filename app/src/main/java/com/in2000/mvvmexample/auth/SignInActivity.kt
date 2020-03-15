package com.in2000.mvvmexample.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.in2000.mvvmexample.R
import com.in2000.mvvmexample.home.HomeActivity
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : AppCompatActivity() {

    private lateinit var signInViewModel: SignInViewModel
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        initAuthButton()
        initSignInClient()
        initViewModel()
        checkSignedIn()
    }

    private fun initAuthButton() {
        google_sign_in_button.setOnClickListener {
            signIn()
        }
    }

    private fun initViewModel() {
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
    }

    private fun initSignInClient() {
        val signInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
    }

    private fun checkSignedIn() {
        if (signInViewModel.checkSignedIn()) {
            goToMainActivity()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java) ?: throw Exception()
                getGoogleAuthCredentials(account)
                signInViewModel.userLiveData.observe(this, Observer {
                    googleSignInClient.signOut()
                    goToMainActivity()
                })

            } catch (e : Exception) {
                Log.d("Google sign-in activity", e.message ?: "")
            }
        }
    }

    private fun getGoogleAuthCredentials(account : GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        signInViewModel.signInWithGoogle(authCredential)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
