package com.example.roomview.views.user

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.roomview.MainActivity
import com.example.roomview.model.Event
import com.example.roomview.navgraph.Routes
import com.example.roomview.ui.widgets.EventCardContent
import com.example.roomview.ui.widgets.PermissionView
import com.example.roomview.viewmodels.user.DiscoverViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, FlowPreview::class)
@Composable
fun DiscoverView(
    navController: NavController,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fineLocationState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    val viewModel: DiscoverViewModel = viewModel()
    val eventsList = viewModel.eventsList
    var eventSelected by remember { mutableStateOf<Event?>(null) }


    DisposableEffect(Unit) {
        val job = scope.launch {
            if (eventsList.isEmpty()) {
                viewModel.getValidEvents()
            }
        }

        onDispose {
            job.cancel()
        }
    }

    if (fineLocationState.status.isGranted) {

        val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

        var lastKnownLocation by remember { mutableStateOf<Location?>(null) }

        var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(context as MainActivity) { task ->
            if (task.isSuccessful) {

                lastKnownLocation = task.result
                deviceLatLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
            }
        }

        val cameraState = rememberCameraPositionState()

        LaunchedEffect(key1 = deviceLatLng) {
            cameraState.centerOnLocation(deviceLatLng)
        }

        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                GoogleMap(
                    modifier = Modifier.height(
                        LocalConfiguration.current.screenHeightDp.dp -
                                paddingValues.calculateTopPadding() -
                                paddingValues.calculateBottomPadding() -
                                150.dp
                    ),
                    cameraPositionState = cameraState,
                    properties = MapProperties(
                        mapType = MapType.NORMAL,
                        isMyLocationEnabled = true,
                    )
                ) {
                    for (event in eventsList) {
                        Marker(
                            state = MarkerState(position = LatLng(event.eventLat, event.eventLong)),
                            title = event.title,
                            snippet = "Date & Time: ${event.eventDate}",
                            onClick = {
                                eventSelected = event
                                return@Marker true
                            },
                        )
                    }
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 4.dp)
                        .height(150.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (eventSelected != null) {
                            Column(
                                modifier = Modifier.clickable {
                                    navController.navigate(Routes.EventDetails.route + "/${eventSelected!!.id}")
                                }
                            )
                            {
                                EventCardContent(event = eventSelected!!)
                            }
                        } else {
                            Text("Tap on a Marker to show details")
                        }
                    }
                }
            }
        }
    } else {
        PermissionView()
    }
}

private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        15f
    ),
    durationMs = 1500
)