package com.example.roomview.views.user

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.roomview.R
import com.example.roomview.ui.widgets.HouseType
import com.example.roomview.ui.widgets.HouseTypeCard
import com.example.roomview.ui.widgets.LoadingCircle
import com.example.roomview.ui.widgets.ParticipantCard
import com.example.roomview.viewmodels.user.EventDetailsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsView(
    eventId: String,
    onBack: () -> Unit,
    navController: NavController
) {
    val viewModel: EventDetailsViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isLoadingState = remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    val event = viewModel.event
    val eventMemberList = viewModel.joinedUserList
    val joinStatus = viewModel.joinStatus

    val currentUser = viewModel.currentUser
    val currentAgent = viewModel.currentAgent

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())


    DisposableEffect(Unit) {
        val job = scope.launch {

            if (event == null) {
                viewModel.getJoinedUsers(eventId.toInt())
                viewModel.getAgent(eventId.toInt())
            }

            isLoadingState.value = false
        }

        onDispose {
            job.cancel()
        }
    }
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                if (isLoadingState.value) {
                    LoadingCircle()
                } else {
                    if (event != null) {
                        val dateTime = inputFormat.parse(event.eventDate)

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(
                                        shape = AbsoluteRoundedCornerShape(
                                            bottomLeft = 10.dp,
                                            bottomRight = 10.dp
                                        )
                                    )
                            ) {
                                if (event.imageUrl != "") {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(event.imageUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(
                                                RoundedCornerShape(
                                                    bottomStart = 16.dp,
                                                    bottomEnd = 16.dp
                                                )
                                            )
                                    )
                                } else {
                                    Image(
                                        painterResource(id = R.drawable.placeholder),
                                        contentDescription = "Placeholder",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(
                                                RoundedCornerShape(
                                                    bottomStart = 16.dp,
                                                    bottomEnd = 16.dp
                                                )
                                            )
                                    )
                                }

                                IconButton(onClick = { onBack() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    )
                                }


                            }
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 16.dp)
                                    .fillMaxWidth()
                            ) {

                                Row {
                                    ElevatedCard(
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    ) {
                                        IconButton(onClick = { onBack() }) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface,
                                            )
                                        }
                                    }

                                    ElevatedCard(
                                        modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
                                    ) {
                                        Column {
                                            when (event.houseType) {
                                                0 -> HouseTypeCard(houseType = HouseType.TERRACE)
                                                1 -> HouseTypeCard(houseType = HouseType.SEMI_D)
                                                2 -> HouseTypeCard(houseType = HouseType.CONDO)
                                            }
                                        }
                                    }
                                }

                                ElevatedCard(
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(all = 12.dp)
                                            .fillMaxWidth(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            event.title,
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            textAlign = TextAlign.Start,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            if (event.agentId.toInt() != (currentUser?.id ?: 0)) {
                                                // Disable when event in eventMember
                                                if (!joinStatus.value) {
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                val response =
                                                                    viewModel.joinEvent(eventId.toInt())

                                                                if (response != null && response.isSuccessful) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Joined successfully",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }

                                                                onBack()
                                                            }
                                                        }
                                                    ) {
                                                        Icon(Icons.AutoMirrored.Filled.Login, null)
                                                        Text("Join Event")
                                                    }
                                                } else {
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                val response =
                                                                    viewModel.leaveEvent(eventId.toInt())

                                                                if (response != null && response.isSuccessful) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Left successfully",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }

                                                                onBack()
                                                            }
                                                        },
                                                        colors = ButtonDefaults.buttonColors(
                                                            MaterialTheme.colorScheme.error
                                                        )
                                                    ) {
                                                        Icon(Icons.Default.Delete, null)
                                                        Text("Leave Event")
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }

                                ElevatedCard(
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(all = 12.dp)
                                    ) {
                                        Text(
                                            "Description",
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            textAlign = TextAlign.Start,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        Text(
                                            event.description,
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        )
                                    }
                                }

                                ElevatedCard(
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(all = 12.dp)
                                    ) {

                                        Text(
                                            "Event Details",
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            textAlign = TextAlign.Start,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        val dateFormatter =
                                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        val timeFormatter =
                                            SimpleDateFormat("HH:mm", Locale.getDefault())

                                        Text(
                                            "Date: ${dateTime?.let { dateFormatter.format(it) }}",
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start,
                                        )

                                        Text(
                                            "Time: ${dateTime?.let { timeFormatter.format(it) }}",
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start,
                                        )

                                        Text(
                                            "Location: ${event.eventLocation}",
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start,
                                        )

                                        val mapLocation = LatLng(event.eventLat, event.eventLong)

                                        val locationState = MarkerState(position = mapLocation)
                                        val cameraPositionState = rememberCameraPositionState {
                                            position =
                                                CameraPosition.fromLatLngZoom(mapLocation, 15f)
                                        }

                                        GoogleMap(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(300.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .padding(vertical = 8.dp),
                                            cameraPositionState = cameraPositionState,
                                            properties = MapProperties(
                                                mapType = MapType.HYBRID, isTrafficEnabled = true
                                            )
                                        ) {
                                            Marker(
                                                state = locationState,
                                                title = event.title,
                                            )
                                        }
                                    }
                                }

                                ElevatedCard(
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(all = 12.dp)
                                    ) {
                                        Text(
                                            "Agent in Charge",
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            textAlign = TextAlign.Start,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        currentAgent.value?.let { ParticipantCard(it) }
                                    }
                                }

                                ElevatedCard(
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(all = 12.dp)
                                    ) {
                                        Text(
                                            "Participants",
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            textAlign = TextAlign.Start,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        if (eventMemberList.isNotEmpty()) {
                                            eventMemberList.forEach { user ->
                                                ParticipantCard(user)
                                            }
                                        } else {
                                            Text(
                                                "No participants at this time",
                                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                                color = MaterialTheme.colorScheme.tertiary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
