package com.example.roomview.ui.widgets

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionView() {

    val context = LocalContext.current
    val fineLocationState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseLocationState = rememberPermissionState(permission = Manifest.permission.ACCESS_COARSE_LOCATION)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Button(onClick = {

            if (!fineLocationState.status.isGranted || !coarseLocationState.status.isGranted) {
                fineLocationState.launchPermissionRequest()
            }

        }) {
            Text(text = "Launch Location Permission")
        }

    }
}