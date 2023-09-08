package com.shocklink.android.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.reflect.TypeToken
import com.microsoft.signalr.Action1
import com.microsoft.signalr.HttpRequestException
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionState
import com.shocklink.android.Routes
import com.shocklink.android.api.ApiClient
import com.shocklink.android.api.UserHubClient
import com.shocklink.android.api.models.Control
import com.shocklink.android.api.models.ControlType
import com.shocklink.android.api.models.DeviceOnlineState
import com.shocklink.android.api.models.ShockerResponse
import com.shocklink.android.api.models.ShockerSharedResponse
import com.shocklink.android.util.TokenManager
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class ShockerViewModel(private val context: Context, private val navController: NavController) : ViewModel() {

    private val _tabIndex: MutableLiveData<Int> = MutableLiveData(0)
    val tabIndex: LiveData<Int>
    val tabs: List<String>


    fun updateTabIndex(i: Int) {
        _tabIndex.value = i
    }

    private val _shockerApiResponse: MutableLiveData<ShockerResponse>
    private val _shockerSharedApiResponse: MutableLiveData<ShockerSharedResponse>
    private val _onlineDevices: MutableLiveData<MutableList<DeviceOnlineState>>
    private val hubConnection: HubConnection
    val ownShockerApiResponse: LiveData<ShockerResponse> get() = _shockerApiResponse

    val onlineDevices: LiveData<MutableList<DeviceOnlineState>> get() =_onlineDevices
    val shockerSharedApiResponse: LiveData<ShockerSharedResponse> get() = _shockerSharedApiResponse

    fun fetchOwnShockerApiResponse() {
        viewModelScope.launch {
            try {
                val response = ApiClient.getShockerApiService(context).ownShocker()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    _shockerApiResponse.value = apiResponse
                } else {
                    // Handle error case
                }
            } catch (e: Exception) {
                Log.e("ShockerViewModel", e.toString())
            }
        }
    }

    fun fetchShockerSharedApiResponse() {
        viewModelScope.launch {
            try {
                val response = ApiClient.getShockerApiService(context).sharedShocker()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    _shockerSharedApiResponse.value = apiResponse
                } else {
                    // Handle error case
                }
            } catch (e: Exception) {
                Log.e("ShockerViewModel", e.toString())
            }
        }
    }

    fun sendCommand(id: String, type: ControlType, duration: UInt, intensity: Byte){
        hubConnection.send("ControlV2",
            listOf(Control(id, type.controlType, intensity, duration)), null)
    }

    init {
        this.tabIndex = _tabIndex
        this.tabs = listOf("Own", "Shared")
        this._shockerApiResponse = MutableLiveData<ShockerResponse>()
        this._shockerSharedApiResponse = MutableLiveData<ShockerSharedResponse>()
        this._onlineDevices = MutableLiveData<MutableList<DeviceOnlineState>>()
        this.hubConnection = UserHubClient.create(context = context)

        val objectType: Type = object : TypeToken<MutableList<DeviceOnlineState>>() {}.type
        hubConnection.on(
            "DeviceStatus",
            Action1 { devices: MutableList<DeviceOnlineState> ->
                _onlineDevices.postValue(devices)
            },
            objectType
        )
        try {
            hubConnection.start().blockingAwait()
        }
        catch(exception: HttpRequestException) {
            if(exception.statusCode == 401){
                TokenManager.clearToken(context)
                navController.navigate(Routes.Login.route)
                Toast.makeText(context, "Your session expired, logging you out.", Toast.LENGTH_SHORT).show()
            }
        }
        if(hubConnection.connectionState != HubConnectionState.CONNECTED) {
            //Check for internet and everything
            TokenManager.clearToken(context)
            navController.navigate(Routes.Login.route)
        }
    }
}