package com.apiguave.tinderclone.ui.loading

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apiguave.tinderclone.domain.FirebaseRepository
import com.apiguave.tinderclone.domain.PreferencesRepository
import com.apiguave.tinderclone.ui.base.AuthState
import com.apiguave.tinderclone.ui.base.AuthStateViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class LoadingViewModel(context: Context): AuthStateViewModel(context, AuthState.LOADING) {

    init {
        tryAuthStateCheck()
    }

    fun tryAuthStateCheck(){
        viewModelScope.launch {
            checkAuthState()
        }
    }
}