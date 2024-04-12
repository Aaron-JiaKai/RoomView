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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.roomview.viewmodels.user.ManageEventViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEventsView(
    navController: NavController
) {
    val viewModel: ManageEventViewModel = viewModel()
    val joinedValidEventList = viewModel.joinedValidEventList
    val joinedExpiredEventList = viewModel.joinedExpiredEventList
    val otherEventList = viewModel.otherEventList

    val scope = rememberCoroutineScope()

    val isLoadingState = remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val job = scope.launch {
            viewModel.getJoinedValidEvents()
            isLoadingState.value = false
        }

        onDispose {
            job.cancel()
            joinedValidEventList.clear()
            joinedExpiredEventList.clear()
            otherEventList.clear()
        }
    }

    val tabTitles = listOf("Upcoming", "History")
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    if (isLoadingState.value) {
        LoadingCircle()
    } else {

        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(title = { Text("Manage Showrooms") })

            TabRow(
                selectedTabIndex = selectedTabIndex.intValue,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex.intValue == index,
                        onClick = { selectedTabIndex.intValue = index },
                        text = { Text(title) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            when (selectedTabIndex.intValue) {
                0 -> {
                    if (joinedValidEventList.size > 0) {
                        LazyColumn(
                            modifier = Modifier.padding(
                                vertical = 8.dp,
                                horizontal = 8.dp
                            )
                        ) {
                            itemsIndexed(joinedValidEventList) { _, event ->
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
                                "No showrooms joined.",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
                1 -> {
                    if (joinedExpiredEventList.size > 0) {
                        LazyColumn(
                            modifier = Modifier.padding(
                                vertical = 8.dp,
                                horizontal = 8.dp
                            )
                        ) {
                            itemsIndexed(joinedExpiredEventList) { _, event ->
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
                                "No showrooms joined.",
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
    }
}