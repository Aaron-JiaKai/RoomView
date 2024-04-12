@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.roomview.views.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.roomview.R
import com.example.roomview.ui.widgets.CustomTextField
import com.example.roomview.ui.widgets.LoadingCircle
import com.example.roomview.viewmodels.user.EditProfileViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileView(
    onBack: () -> Unit
) {
    val viewModel: EditProfileViewModel = viewModel()
    val isLoadingState = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val currentUser = viewModel.currentUser

    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri.value = uri
    }

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
                title = { Text("Edit Profile") },
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
                Text(
                    "${currentUser?.firstName} ${currentUser?.lastName}",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
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
                            painterResource(id = R.drawable.placeholder_user)
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .size(200.dp)
                            .clip(CircleShape)
                            .clickable {
                                launcher.launch("image/*")
                            }
                    )

                    // First Name
                    CustomTextField(
                        value = firstName,
                        onValueChange = { newValue ->
                            firstName.value = newValue;
                        },
                        label = "New first name",
                        isPassword = false,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    // First Name
                    CustomTextField(
                        value = lastName,
                        onValueChange = { newValue ->
                            lastName.value = newValue;
                        },
                        label = "New last name",
                        isPassword = false,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                Button(onClick = {
                    scope.launch {
                        viewModel.updateProfile(
                            imageUri = imageUri.value,
                            firstName = firstName.value,
                            lastName = lastName.value
                        )
                        onBack()
                    }
                }) {
                    Text("Save Changes")
                }
            }
        }

    }
}