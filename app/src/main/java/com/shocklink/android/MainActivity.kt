package com.shocklink.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shocklink.android.screen.LoginPage
import com.shocklink.android.screen.OwnShockerPage
import com.shocklink.android.screen.ShockerPage
import com.shocklink.android.ui.theme.ShockLinkAndroidTheme
import com.shocklink.android.util.TokenManager
import com.shocklink.android.viewmodels.LoginViewModel
import com.shocklink.android.viewmodels.ShockerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShockLinkAndroidTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val hasToken = TokenManager.getToken(application) != null
                NavHost(navController = navController, startDestination = if(!hasToken) "Login" else "Shocker" ) {
                    composable(Routes.Login.route) { LoginPage(navController = navController, viewModel = LoginViewModel(application = application))}
                    composable(Routes.Shocker.route) { ShockerPage(navController = navController, ShockerViewModel(application)) }
                }
            }
        }
    }
}
