package com.example.roomview.views.agent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.roomview.R
import com.example.roomview.ui.widgets.CustomTextField
import com.example.roomview.ui.widgets.LoadingCircle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentCreateEventView(
    onSuccessCreate: () -> Unit,
    paddingValues: PaddingValues
) {
    var imageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri.value = uri
    }
    val placeholderImage = "https://fakeimg.pl/600x400?text=No+Media"

    val eventTitle = remember { mutableStateOf("") }
    val eventDescription = remember { mutableStateOf("") }
    val selectedCommunityId = remember { mutableStateOf("") }
    val selectedCommunityName = remember { mutableStateOf("") }
    var pickedDate = remember { mutableStateOf("") }
    var pickedTime = remember { mutableStateOf("") }
    var location = remember { mutableStateOf("") }

    var isMenuOpen = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    //val viewModel: CreateEventViewModel = viewModel()

    val isLoadingState = remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    format.timeZone = TimeZone.getDefault()


    DisposableEffect(Unit) {
        val job = scope.launch {
            viewModel.getOwnCommunityData()
            isLoadingState.value = false
        }

        onDispose {
            job.cancel()

        }
    }

    if (isLoadingState.value) {
        LoadingCircle()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text("Schedule a new Event") },
                actions = {
                    Button(onClick = {
                        scope.launch {
                            imageUri.value?.let {
                                viewModel.createEvent(
                                    imageUri = it,
                                    title = eventTitle.value,
                                    date = format.parse("${pickedDate.value} ${pickedTime.value}") as Date,
                                    description = eventDescription.value,
                                    community = selectedCommunityId.value,
                                    location = location.value
                                )
                            }
                            if (viewModel.alreadyExist.value) {
                                showCreateError = true
                            }

                            if (viewModel.successAdd.value) {
                                onSuccessCreate()
                            }
                        }

                    }) {
                        Text("Post")
                    }
                }
            )

            Image(
                rememberAsyncImagePainter(
                    model = ImageRequest
                        .Builder(context)
                        .crossfade(false)
                        .data(imageUri.value ?: placeholderImage)
                        .build(),
                    filterQuality = FilterQuality.High
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            Text("Select an image")

            Column(
                modifier = Modifier.padding(horizontal = 48.dp)
            ) {
                CustomTextField(
                    value = eventTitle,
                    onValueChange = { newValue ->
                        eventTitle.value = newValue;
                    },
                    label = "Title",
                    isPassword = false
                )

                CustomTextField(
                    value = eventDescription,
                    onValueChange = { newValue ->
                        eventDescription.value = newValue;
                    },
                    label = "Description",
                    isPassword = false,
                    isSingleLine = false,
                    maxLines = 3
                )

                val pickerContext = LocalContext.current
                val calendar = Calendar.getInstance()

                // Fetching current year, month and day
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

                val datePicker = DatePickerDialog(
                    pickerContext,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                        pickedDate.value = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                    }, year, month, dayOfMonth
                )
                datePicker.datePicker.minDate = calendar.timeInMillis

                // Fetching current hour, and minute
                val hour = calendar[Calendar.HOUR_OF_DAY]
                val minute = calendar[Calendar.MINUTE]

                val timePicker = TimePickerDialog(
                    pickerContext,
                    { _, selectedHour: Int, selectedMinute: Int ->
                        pickedTime.value = "$selectedHour:$selectedMinute"
                    }, hour, minute, false
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            datePicker.show()
                        },
                        modifier = Modifier
                            .weight(1f),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(if (pickedDate.value == "") "Select Date" else pickedDate.value)
                    }
                    OutlinedButton(
                        onClick = {
                            timePicker.show()
                        },
                        modifier = Modifier
                            .weight(1f),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(if (pickedTime.value == "") "Select Time" else pickedTime.value)
                    }
                }

                CustomTextField(
                    value = location,
                    onValueChange = { newValue ->
                        location.value = newValue;
                    },
                    label = "Location",
                    isPassword = false
                )

                CustomTextField(
                    value = selectedCommunityName,
                    onValueChange = { newValue ->
                        selectedCommunityName.value = newValue
                    },
                    readOnly = true,
                    isPassword = false,
                    label = "Community",
                    modifier = Modifier.clickable {
                        isMenuOpen.value = true
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }


        DropdownMenu(
            expanded = isMenuOpen.value,
            onDismissRequest = { isMenuOpen.value = false }
        ) {
            viewModel.ownCommunityList.forEach { community ->
                DropdownMenuItem(onClick = {
                    selectedCommunityId.value = community.id
                    selectedCommunityName.value = community.title
                    isMenuOpen.value = false
                }) {
                    Text(text = community.title)
                }
            }
        }


    }

    if (showCreateError) { //Login Error Dialog
        CustomDialogClose(
            alertTitle = stringResource(R.string.community_exist_error_header),
            alertBody = stringResource(R.string.community_exist_error_desc),
            onDismissFun = { showCreateError = false },
            btnCloseClick = { showCreateError = false })
    }


}
