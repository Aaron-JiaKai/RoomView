package com.example.roomview.views.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.roomview.navgraph.Routes
import com.example.roomview.ui.widgets.EventCardContent
import com.example.roomview.ui.widgets.LoadingCircle
import com.example.roomview.viewmodels.agent.AgentEventsViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentEventsView(
    navController: NavController
) {
    val viewModel: AgentEventsViewModel = viewModel()
    val otherEventList = viewModel.otherEventList

    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false   ) }
    var isSearching by remember { mutableStateOf(false   ) }

    val scope = rememberCoroutineScope()


    val isLoadingState = remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val job = scope.launch {
            viewModel.getJoinedEvents()
            isLoadingState.value = false
        }

        onDispose {
            job.cancel()
            otherEventList.clear()
        }
    }

    if (isLoadingState.value) {
        LoadingCircle()
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Welcome Back!") },
            )

            if (otherEventList.size > 0) {
                LazyColumn(modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)) {
                    itemsIndexed(otherEventList) { _, event ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    navController.navigate(Routes.EventDetails.route + "/${event.id}")
                                }
                        ) {
                            EventCardContent(event = event)
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "No events are currently available.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

    }
}


