package com.shocklink.android.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shocklink.android.screen.views.ShockerBox
import com.shocklink.android.viewmodels.ShockerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedShockerPage(navController: NavHostController, viewModel: ShockerViewModel, context: Context) {
    val shockerApiResponse by viewModel.shockerSharedApiResponse.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchShockerSharedApiResponse()
    }

    when {
        shockerApiResponse != null -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())){
                for (user in shockerApiResponse!!.data){
                    for (device in user.devices){
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
                                    text = user.name  + " - " + device.name,
                                    color = MaterialTheme.colorScheme.onBackground)
                                for(shocker in device.shockers){
                                    ShockerBox(context = context, shocker)
                                }
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

