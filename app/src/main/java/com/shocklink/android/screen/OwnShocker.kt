package com.shocklink.android.screen

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shocklink.android.api.models.Hub.ControlType
import com.shocklink.android.api.models.Request.Permissions
import com.shocklink.android.api.models.Shocker
import com.shocklink.android.screen.views.ShareDialog
import com.shocklink.android.screen.views.ShockerBox
import com.shocklink.android.ui.theme.ShockLinkAndroidTheme
import com.shocklink.android.viewmodels.ShockerViewModel

@Composable
fun OwnShockerPage(navController: NavHostController, viewModel: ShockerViewModel, context: Context) {
    val shockerApiResponse by viewModel.ownShockerApiResponse.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.fetchOwnShockerApiResponse()
    }
    var currentShareShocker = remember { mutableStateOf("") }

    when {
        shockerApiResponse != null -> {
            if (showDialog) {
                ShareDialog(
                    onConfirm = {vibrate: Boolean, shocking: Boolean, beep: Boolean, duration: Int?, intensity: Int? ->
                                viewModel.createShareForShocker(currentShareShocker.value, Permissions(vibrate = vibrate, shock = shocking, sound = beep), intensity?: 100, duration?: 30000)
                    },
                    onCancel = {
                        // Handle cancel action
                        showDialog = false
                    }
                )
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                   /*.verticalScroll(rememberScrollState())*/){
                for (element in shockerApiResponse!!){
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ){
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()) {
                            Text(
                                text = element.name,
                                color = MaterialTheme.colorScheme.onBackground)
                            for(shocker in element.shockers){
                                ShockerBox(context = context, shocker, ownShocker = true, onEventClicked =
                                { shockerB: Shocker, controlType: ControlType, duration: UInt, intensity: Byte ->
                                    viewModel.sendCommand(shockerB.id, controlType, duration, intensity)
                                }, onPauseClicked = { id: String, pause: Boolean ->
                                    viewModel.pauseShocker(id, pause)
                                }, onShareClicked = {
                                    currentShareShocker.value = it
                                    showDialog = true
                                })
                            }
                        }
                    }
                }
            }
        }
        shockerApiResponse == null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)) {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.Center)
                    .size(200.dp),)
            }
        }
        else -> {
            // Display error state
        }
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES,showBackground = true)
@Composable
fun Preview() {
    ShockLinkAndroidTheme {
        OwnShockerPage( rememberNavController(), ShockerViewModel(LocalContext.current, rememberNavController()), LocalContext.current)
    }
}