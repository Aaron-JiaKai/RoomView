package com.example.roomview.views.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomview.ui.widgets.CustomTextField
import com.example.roomview.ui.widgets.LoadingCircle
import com.example.roomview.viewmodels.user.SwitchAgentViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchAgentView(
    onBack: () -> Unit
) {
    val viewModel: SwitchAgentViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val currentUser = viewModel.currentUser
    val agentReferralNo = remember { mutableStateOf("") }

    val isLoadingState = remember { mutableStateOf(true) }

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
        Column {

            TopAppBar(
                title = { Text("Switch to Agent Account") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 48.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please enter an agent referral number.",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    CustomTextField(
                        value = agentReferralNo,
                        onValueChange = { newValue ->
                            agentReferralNo.value = newValue
                        },
                        label = "Referral Number",
                        isPassword = false,
                        leadingIcon = {
                            Icon(Icons.Default.Key, contentDescription = null)
                        },
                        trailingIcon = null
                    )
                    Text(
                        text = "For prototype purposes, referral number is 1234",
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )
                }
                Button(onClick = {
                    scope.launch {
                        viewModel.updateProfile(context, agentReferralNo.value)
                        onBack()
                    }
                }) {
                    Text("Confirm")
                }
            }
        }

    }
}