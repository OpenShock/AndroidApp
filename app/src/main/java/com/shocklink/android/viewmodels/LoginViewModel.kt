package com.shocklink.android.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shocklink.android.api.ApiClient
import com.shocklink.android.api.models.LoginRequest
import com.shocklink.android.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val application: Application): ViewModel() {
    private val _loginStatus = MutableStateFlow<LoginStatus?>(null)
    val loginStatus: StateFlow<LoginStatus?> = _loginStatus


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val loginResult = performLoginAsync(email, password)

            _loginStatus.value = loginResult
        }
    }

    private suspend fun performLoginAsync(email: String, password: String): LoginStatus {
        return try {
            val response = ApiClient.getLoginApiService(application).login(LoginRequest(email, password))

            // Check the login response
            if (response.data.shockLinkSession.isNotEmpty()) {
                TokenManager.saveToken(context = application, token = response.data.shockLinkSession)
                LoginStatus.Success
            } else {
                LoginStatus.Failure(response.message)
            }
        } catch (e: Exception) {
            LoginStatus.Failure("Login failed")
        }
    }
}

sealed class LoginStatus {
    object Success : LoginStatus()
    data class Failure(val errorMessage: String) : LoginStatus()
}