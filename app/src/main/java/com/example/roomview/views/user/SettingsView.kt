package com.example.soiree.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomview.ui.widgets.LoadingCircle
import com.example.roomview.ui.widgets.UserProfileCard
import com.example.roomview.viewmodels.user.SettingsViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    onSignOut: () -> Unit,
    onEdit: () -> Unit,
    onSwitch: () -> Unit,
    paddingValues: PaddingValues,
) {
    val viewModel: SettingsViewModel = viewModel()
    var isLoadingState = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var currentUser = viewModel.currentUser

    DisposableEffect(Unit) {
        val job = scope.launch {
            viewModel.getUsers()
            if (currentUser == null) viewModel.getCurrentUser()
            isLoadingState.value = false
        }
        onDispose {
            job.cancel()
            viewModel.clearList()
        }
    }

    if (isLoadingState.value) {
        LoadingCircle()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                TopAppBar(title = { Text("Manage Profile") })
                ElevatedCard {
                    if (currentUser != null) {
                        UserProfileCard(user = currentUser, modifier = Modifier.clickable {
                            onEdit()
                        })
                    }
                }
                ElevatedCard(modifier = Modifier.padding(top = 16.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEdit()
                        }
                        .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Edit Profile")
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
                    }
                }

                if (currentUser != null && currentUser.userType == 0) {
                    ElevatedCard(modifier = Modifier.padding(top = 16.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                       onSwitch()
                            },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Switch to Agent Account")
                            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
                        }
                    }
                }
            }


            Button(
                onClick = {
                    viewModel.signOut()
                    onSignOut()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Sign Out")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out")
            }
        }
    }
}