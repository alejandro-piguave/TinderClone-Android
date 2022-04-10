package com.apiguave.tinderclone.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apiguave.tinderclone.ui.loading.LoadingViewModel
import com.apiguave.tinderclone.ui.login.LoginViewModel

class AuthStateViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadingViewModel::class.java)) {
            return LoadingViewModel(context) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}