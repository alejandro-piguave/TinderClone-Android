package com.apiguave.tinderclone.ui.loading

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.apiguave.tinderclone.databinding.ActivityLoadingBinding
import com.apiguave.tinderclone.ui.login.LoginActivity
import com.apiguave.tinderclone.ui.HomeActivity
import com.apiguave.tinderclone.ui.PendingInformationActivity
import com.apiguave.tinderclone.ui.base.AuthState
import com.apiguave.tinderclone.ui.base.AuthStateViewModelFactory
import com.apiguave.tinderclone.ui.extension.openActivity
import kotlinx.coroutines.flow.collect

class LoadingActivity : AppCompatActivity() {

    private val binding: ActivityLoadingBinding by lazy { ActivityLoadingBinding.inflate(layoutInflater)}
    private val viewModel: LoadingViewModel by lazy {
        val factory  = AuthStateViewModelFactory(this)
        ViewModelProvider(this, factory).get(LoadingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        animateLogo()

        lifecycleScope.launchWhenStarted {
            viewModel.authState.collect {
                when(it){
                    AuthState.LOADING -> {}
                    AuthState.LOGGED_OUT -> openActivity(LoginActivity::class.java)
                    AuthState.PENDING_INFORMATION -> openActivity(PendingInformationActivity::class.java)
                    AuthState.LOGGED_IN -> openActivity(HomeActivity::class.java)
                    AuthState.ERROR -> showError()
                }
            }
        }

        binding.tryAgainButton.setOnClickListener {
            viewModel.tryAuthStateCheck()
            hideError()
        }
    }

    private fun showError(){
        binding.errorGroup.isVisible = true
    }

    private fun hideError(){
        binding.errorGroup.isVisible = false
    }
    private fun animateLogo(){
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            binding.loadingView,
            PropertyValuesHolder.ofFloat("scaleX", 1.25f),
            PropertyValuesHolder.ofFloat("scaleY", 1.25f)
        )
        scaleDown.duration = 1000
        scaleDown.repeatMode = ValueAnimator.REVERSE
        scaleDown.repeatCount = ValueAnimator.INFINITE
        scaleDown.start()
    }
}