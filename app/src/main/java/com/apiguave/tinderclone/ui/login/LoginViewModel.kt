package com.apiguave.tinderclone.ui.login

import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.apiguave.tinderclone.ui.base.AuthState
import com.apiguave.tinderclone.ui.base.AuthStateViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(context: Context): AuthStateViewModel(context, AuthState.LOGGED_OUT) {

    fun handleGoogleSignInActivityResult(activityResult: ActivityResult){
        viewModelScope.launch {
            _authState.emit(AuthState.LOADING)
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            e.printStackTrace()
            viewModelScope.launch {
                _authState.emit(AuthState.ERROR)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        checkAuthState()
                    }
                } else {
                    viewModelScope.launch {
                        _authState.emit(AuthState.ERROR)
                    }
                }
            }
    }
}