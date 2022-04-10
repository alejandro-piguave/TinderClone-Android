package com.apiguave.tinderclone.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.apiguave.tinderclone.domain.FirebaseRepository
import com.apiguave.tinderclone.domain.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect

enum class AuthState{ LOADING, LOGGED_OUT, PENDING_INFORMATION, LOGGED_IN, ERROR}
open class AuthStateViewModel(context: Context, authState: AuthState): ViewModel() {
    private val preferencesRepository = PreferencesRepository(context)
    private val firebaseRepository = FirebaseRepository

    protected val _authState: MutableStateFlow<AuthState> = MutableStateFlow(authState)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    protected suspend fun checkAuthState(){
        val currentUser = firebaseRepository.currentUser
        currentUser?.let {
            preferencesRepository.hasCheckedPendingInfoFlow.collect { hasCheckedPendingInfo ->
                if (hasCheckedPendingInfo){
                    _authState.emit(AuthState.LOGGED_IN)
                } else{
                    val userExists = firebaseRepository.userExists()
                    userExists.getOrNull()?.let {
                        _authState.emit(if(it) AuthState.LOGGED_IN else AuthState.PENDING_INFORMATION)
                    } ?: kotlin.run { _authState.emit(AuthState.ERROR) }
                }
            }
        } ?: run{
            _authState.emit(AuthState.LOGGED_OUT)
        }
    }
}