package com.shocklink.android.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shocklink.android.api.ApiClient
import com.shocklink.android.api.models.LoginRequest
import com.shocklink.android.api.models.LoginResponse
import com.shocklink.android.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Headers

class LoginViewModel(private val application: Application): ViewModel() {
    private val _loginStatus = MutableStateFlow<LoginStatus?>(null)
    val loginStatus: StateFlow<LoginStatus?> = _loginStatus


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val loginResult = performLoginAsync(email, password)

            _loginStatus.value = loginResult
        }
    }

    fun resetLogin() {
        _loginStatus.value = LoginStatus.Default
    }

    private suspend fun performLoginAsync(email: String, password: String): LoginStatus {
        return try {
            val response = ApiClient.getLoginApiService(application).login(LoginRequest(email, password))
            val cookie: String? = response.headers()["Set-Cookie"]
            if (!cookie.isNullOrEmpty()) {
                val regex = "openShockSession=([^;]*)".toRegex()
                val matchResult = regex.find(cookie)
                val openShockSessionValue = matchResult?.groups?.get(1)?.value
                if(openShockSessionValue != null) {
                    TokenManager.saveToken(context = application, token = openShockSessionValue)
                    return LoginStatus.Success
                }
            }
            LoginStatus.Failure(
                response.body()?.message ?: "Login Failed (No Message supplied)"
            )
        }catch(e: Exception) {
            Log.e("LoginViewModel", e.message ?: "Login failed for unknown reason")
            LoginStatus.Failure("Login Failed")
        }
    }
}

sealed class LoginStatus {
    object Success : LoginStatus()

    object Default : LoginStatus()
    data class Failure(val errorMessage: String) : LoginStatus()
}