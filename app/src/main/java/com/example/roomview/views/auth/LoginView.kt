@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.roomview.views.auth

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.roomview.R
import com.example.roomview.navgraph.AuthScreen
import com.example.roomview.navgraph.Graph
import com.example.roomview.ui.widgets.CustomTextField
import com.example.roomview.ui.widgets.WarningDialog
import com.example.roomview.viewmodels.auth.LoginViewModel
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun LoginContent(
    navController: NavController,
    onSignUpClick: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var showLoginError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val options = mutableListOf("User", "Agent")
    var selectedIndex by remember { mutableIntStateOf(0) }


    DisposableEffect(Unit) {
        val job = scope.launch {
            viewModel.getAllUsers()

        }

        onDispose {
            job.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .height(150.dp)
            )
            Text("Room View", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Column(
                modifier = Modifier
                    .padding(horizontal = 48.dp, vertical = 4.dp)
            ) {

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            )
                        ) {
                            Text(text = option)
                        }
                    }
                }

                CustomTextField(
                    value = email,
                    onValueChange = { newValue ->
                        email.value = newValue;
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Mail,
                            contentDescription = "Email",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailingIcon = null,
                    label = "email",
                )

                CustomTextField(
                    value = password,
                    onValueChange = { newValue ->
                        password.value = newValue;
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Key,
                            contentDescription = "Password",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailingIcon = null,
                    label = "password",
                    isPassword = true
                )


            }

            Button(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    scope.launch {
                        val data = viewModel.login(
                            email.value.trim(),
                            password.value.trim(),
                            selectedIndex
                        )

                        if (data != null) { // Navigate to Home page
                            when (selectedIndex) {
                                0 -> {
                                    navController.navigate(Graph.HOME) {
                                        popUpTo(AuthScreen.LOGIN.route) {
                                            inclusive = true
                                        }
                                    }
                                    Log.d("TESTING", "${navController.currentBackStack.value}")

                                }

                                1 -> {
                                    navController.navigate(Graph.AGENT) {
                                        popUpTo(AuthScreen.LOGIN.route) {
                                            inclusive = true
                                        }
                                    }
                                    Log.d("TESTING", "${navController.currentBackStack.value}")
                                }
                            }
                        } else {
                            showLoginError = true
                        }
                    }

                }) {
                Text("Login")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "No account? Register Here",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clickable {
                        onSignUpClick()
                    }
            )
        }

        if (showLoginError) { //Login Error Dialog
            WarningDialog(
                alertTitle = "Authentication Error",
                alertBody = viewModel.message.value,
                onDismissFun = { showLoginError = false },
                buttonFun = { showLoginError = false })
        }


    }
}