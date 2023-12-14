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
import com.shocklink.android.api.models.Hub.Control
import com.shocklink.android.api.models.Hub.ControlType
import com.shocklink.android.api.models.Device
import com.shocklink.android.api.models.Hub.DeviceOnlineState
import com.shocklink.android.api.models.MessageDataListResponse
import com.shocklink.android.api.models.MessageDataResponse
import com.shocklink.android.api.models.Request.PauseRequest
import com.shocklink.android.api.models.User
import com.shocklink.android.util.TokenManager
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.reflect.Type

class ShockerViewModel(private val context: Context, private val navController: NavController) : ViewModel() {

    private val _tabIndex: MutableLiveData<Int> = MutableLiveData(0)
    val tabIndex: LiveData<Int>
    val tabs: List<String>


    fun updateTabIndex(i: Int) {
        _tabIndex.value = i
    }

    private val _shockerApiResponse: MutableLiveData<List<Device>>
    private val _shockerSharedApiResponse: MutableLiveData<List<User>>
    private val _onlineDevices: MutableLiveData<MutableList<DeviceOnlineState>>
    private val hubConnection: HubConnection
    val ownShockerApiResponse: MutableLiveData<List<Device>> get() = _shockerApiResponse

    val onlineDevices: MutableLiveData<MutableList<DeviceOnlineState>> get() =_onlineDevices
    val shockerSharedApiResponse: MutableLiveData<List<User>> get() = _shockerSharedApiResponse

    fun fetchOwnShockerApiResponse() {
        viewModelScope.launch {
            try {
                val response = ApiClient.getShockerApiService(context).ownShocker()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    _shockerApiResponse.value = apiResponse?.data!!
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
                    _shockerSharedApiResponse.value = apiResponse?.data
                } else {
                    // Handle error case
                }
            } catch (e: Exception) {
                Log.e("ShockerViewModel", e.toString())
            }
        }
    }

    fun pauseShocker(id: String, pause: Boolean) {
        viewModelScope.launch {
            try {
                val response = ApiClient.getShockerApiService(context).pauseShocker(shockerId = id, PauseRequest(pause))
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val currentDevices = _shockerApiResponse.value

                    val updatedDevices = currentDevices?.map { device ->
                        val updatedShockers = device.shockers.map { shocker ->
                            if (shocker.id == id) shocker.copy(isPaused = apiResponse?.data ?: !pause) else shocker
                        }
                        device.copy(shockers = updatedShockers)
                    }

                    _shockerApiResponse.postValue(updatedDevices)

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
        this._shockerApiResponse = MutableLiveData<List<Device>>()
        this._shockerSharedApiResponse = MutableLiveData<List<User>>()
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