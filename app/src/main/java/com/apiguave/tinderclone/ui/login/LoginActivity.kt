package com.apiguave.tinderclone.ui.login

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.apiguave.tinderclone.R
import com.apiguave.tinderclone.databinding.ActivityLoginBinding
import com.apiguave.tinderclone.ui.HomeActivity
import com.apiguave.tinderclone.ui.PendingInformationActivity
import com.apiguave.tinderclone.ui.base.AuthState
import com.apiguave.tinderclone.ui.base.AuthStateViewModelFactory
import com.apiguave.tinderclone.ui.extension.openActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var googleSignInClient: GoogleSignInClient
    private val intentHandler = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        viewModel.handleGoogleSignInActivityResult(it)
    }

    private val viewModel: LoginViewModel by lazy {
        val factory  = AuthStateViewModelFactory(this)
        ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    private val scaleAnimator: ObjectAnimator by lazy {
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            binding.loadingView,
            PropertyValuesHolder.ofFloat("scaleX", 1.25f),
            PropertyValuesHolder.ofFloat("scaleY", 1.25f)
        )
        scaleDown.duration = 1000
        scaleDown.repeatMode = ValueAnimator.REVERSE
        scaleDown.repeatCount = ValueAnimator.INFINITE
        scaleDown
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.signInButton.setOnClickListener {
            signInWithGoogle()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.authState.collect {
                if(it != AuthState.LOADING)
                    showLoading(false)
                when(it){
                    AuthState.LOADING -> showLoading(true)
                    AuthState.LOGGED_OUT -> {}
                    AuthState.PENDING_INFORMATION -> openActivity(PendingInformationActivity::class.java)
                    AuthState.LOGGED_IN -> openActivity(HomeActivity::class.java)
                    AuthState.ERROR -> showError()
                }
            }
        }
    }

    private fun showLoading(show: Boolean){
        animateLogo(show)
        binding.signInButton.isVisible = !show
    }

    private fun showError() {
        Snackbar.make(binding.root,R.string.loading_error, Snackbar.LENGTH_LONG).show()
    }

    private fun signInWithGoogle(){
        val intent = googleSignInClient.signInIntent
        intentHandler.launch(intent)
    }

    private fun animateLogo(animate: Boolean){
        if(animate)
            scaleAnimator.start()
        else {
            scaleAnimator.end()
            scaleAnimator.currentPlayTime = 0
        }
    }

}