package com.shocklink.android.screen
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shocklink.android.R
import com.shocklink.android.Routes
import com.shocklink.android.ui.theme.ShockLinkAndroidTheme
import com.shocklink.android.viewmodels.LoginStatus
import com.shocklink.android.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavHostController, viewModel: LoginViewModel){
    var context = LocalContext.current
    val offset = Offset(0.0f, 4.0f)
    val loginStatus by viewModel.loginStatus.collectAsState()
    val loginGoingOn = remember { mutableStateOf(false) }
    loginStatus?.let { status ->
        when (status) {
            is LoginStatus.Success -> {
                navController.navigate(Routes.Shocker.route)
                viewModel.resetLogin()
                loginGoingOn.value = false
            }

            is LoginStatus.Failure -> {

                loginGoingOn.value = false
            }

            is LoginStatus.Default -> {}
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background69bda1a83),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight)
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }


            Text(
                text = "Login",
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
                color = Color.White)

            //Email Field
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                label = { Text(text = "Email", color = Color.White) },
                value = username.value,
                onValueChange = { username.value = it },
                enabled = !loginGoingOn.value,
                leadingIcon = {Icon(Icons.Outlined.Person, "Email", tint = Color.White)},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = Color.Gray)
                )
            //Password Field
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                label = { Text(text = "Password", color = Color.White) },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                enabled = !loginGoingOn.value,
                onValueChange = { password.value = it },
                leadingIcon = {Icon(Icons.Default.Lock, "Password", tint = Color.White)},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = Color.Gray))
            Spacer(modifier = Modifier.height(20.dp))

            //Login Button
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                OutlinedButton(
                    onClick = {
                        loginGoingOn.value = true
                        viewModel.login(username.value.text, password.value.text)
                        },
                    shape = RoundedCornerShape(50.dp),
                    enabled = !loginGoingOn.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    border = BorderStroke(2.dp, Color.Gray)
                ) {
                    Text(text = "Login", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            ClickableText(
                text = AnnotatedString("Forgot password?"),
                onClick = { openLink(context, "https://shocklink.net/#/account/password/reset") },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black, offset = offset, blurRadius = 1f
                    )
                ),
            )
        }
        ClickableText(
            text = AnnotatedString("Sign up here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { openLink(context, "https://shocklink.net/#/account/signup")},
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Color.White
            )
        )
    }
}
fun openLink(context: Context, url: String) {

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShockLinkAndroidTheme {
        //LoginPage(navController = rememberNavController(), LoginViewModel())
    }
}