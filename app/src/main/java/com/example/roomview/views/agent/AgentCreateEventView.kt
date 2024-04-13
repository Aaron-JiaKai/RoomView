package com.example.roomview.views.agent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.roomview.R
import com.example.roomview.ui.widgets.CustomTextField
import com.example.roomview.ui.widgets.HouseType
import com.example.roomview.ui.widgets.LoadingCircle
import com.example.roomview.viewmodels.agent.AgentCreateEventViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentCreateEventView(
    onSuccessCreate: () -> Unit,
    paddingValues: PaddingValues
) {

    val viewModel: AgentCreateEventViewModel = viewModel()
    val context = LocalContext.current

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri.value = uri
    }

    val eventTitle = remember { mutableStateOf("") }
    val eventDescription = remember { mutableStateOf("") }
    val pickedDate = remember { mutableStateOf("") }
    val pickedTime = remember { mutableStateOf("") }
    val eventLocation = remember { mutableStateOf("") }
    val eventAddress = remember { mutableStateOf<Address?>(null) }

    val isMenuOpen = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val isLoadingState = remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    format.timeZone = TimeZone.getDefault()

    val options = mutableListOf(HouseType.TERRACE, HouseType.SEMI_D, HouseType.CONDO)
    var selectedIndex by remember { mutableIntStateOf(0) }

    var currentUser = viewModel.currentUser

    DisposableEffect(Unit) {
        val job = scope.launch {
            if (currentUser == null) {
                viewModel.getCurrentUser()
            }
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
                title = { Text("List a property") },
                actions = {
                    Button(onClick = {
                        scope.launch {

                            val eventDate = "${pickedDate.value}T${pickedTime.value}"


                            if (eventAddress.value == null && eventLocation.value != "") {
                                val geocoder = Geocoder(context)

                                val results =
                                    eventLocation.value.let { geocoder.getFromLocationName(it, 1) }
                                if (!results.isNullOrEmpty()) {
                                    val addressArray: List<Address> = results

                                    for (address in addressArray) {
                                        eventLocation.value = address.getAddressLine(0)
                                        eventAddress.value = address
                                    }
                                }
                            }
                            if (eventAddress.value != null) {
                                if (viewModel.createEvent(
                                    eventTitle.value,
                                    eventDescription.value,
                                    selectedIndex,
                                    imageUri.value,
                                    eventDate,
                                    eventLocation.value,
                                    eventAddress.value!!.latitude,
                                    eventAddress.value!!.longitude,
                                    context
                                ) != null) {
                                    onSuccessCreate();
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid address is entered",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }) {
                        Text("Post")
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                Image(
                    painter =
                    if (imageUri.value != null) {
                        rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .crossfade(false)
                                .data(imageUri.value)
                                .build(),
                            filterQuality = FilterQuality.High
                        )
                    } else {
                        painterResource(id = R.drawable.placeholder)
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
                if (imageUri.value == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Select an image",
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Black
                        )
                    }

                }
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                    ) {
                        Text(text = "House Type")
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
                                    Text(text = option.description)
                                }
                            }
                        }
                    }
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                    ) {
                        Text("About Property")

                        CustomTextField(
                            value = eventTitle,
                            onValueChange = { newValue ->
                                eventTitle.value = newValue;
                            },
                            label = "Title",
                            isPassword = false,
                            leadingIcon = null,
                            trailingIcon = null,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        CustomTextField(
                            value = eventDescription,
                            onValueChange = { newValue ->
                                eventDescription.value = newValue;
                            },
                            label = "Description",
                            isPassword = false,
                            isSingleLine = false,
                            maxLines = 3,
                            leadingIcon = null,
                            trailingIcon = null,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }


                val calendar = Calendar.getInstance()

                // Fetching current year, month and day
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                        pickedDate.value = "$selectedYear-${String.format("%02d", selectedMonth + 1) }-${String.format("%02d", selectedDayOfMonth)}"
                    }, year, month, dayOfMonth
                )
                datePicker.datePicker.minDate = calendar.timeInMillis

                // Fetching current hour, and minute
                val hour = calendar[Calendar.HOUR_OF_DAY]
                val minute = calendar[Calendar.MINUTE]

                val timePicker = TimePickerDialog(
                    context,
                    { _, selectedHour: Int, selectedMinute: Int ->
                        pickedTime.value = "${String.format("%02d", selectedHour)}:${String.format("%02d", selectedMinute)}:00"
                    }, hour, minute, false
                )

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                    ) {
                        Text("Showroom Details")

                        OutlinedButton(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .fillMaxWidth(),
                            onClick = { datePicker.show() }
                        ) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(text = if (pickedDate.value != "") pickedDate.value else "Select Showroom Date")
                        }

                        OutlinedButton(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .fillMaxWidth(),
                            onClick = { timePicker.show() }
                        ) {
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(text = if (pickedTime.value != "") pickedTime.value else "Select Showroom Time")
                        }
                    }
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {

                    Column(
                        Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("Showroom Venue")

                            CustomTextField(
                                value = eventLocation,
                                onValueChange = { newValue ->
                                    eventLocation.value = newValue;
                                },
                                label = "Location",
                                isPassword = false,
                                maxLines = 5,
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Please ensure the address is correct",
                                fontSize = MaterialTheme.typography.labelSmall.fontSize
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {

                            Button(
                                onClick = {
                                    if (eventLocation.value == "") return@Button

                                    val geocoder = Geocoder(context)

                                    val results =
                                        eventLocation.value.let {
                                            geocoder.getFromLocationName(
                                                it,
                                                1
                                            )
                                        }
                                    if (!results.isNullOrEmpty()) {
                                        val addressArray: List<Address> = results

                                        for (address in addressArray) {
                                            eventLocation.value = address.getAddressLine(0)
                                            eventAddress.value = address
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No address is found for this location",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }) {
                                Text(text = "Check Location")
                            }
                        }
                    }
                }
            }
        }
    }
}
