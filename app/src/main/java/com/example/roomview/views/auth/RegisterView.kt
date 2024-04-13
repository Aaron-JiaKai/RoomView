package com.example.roomview.views.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.roomview.ui.widgets.WarningDialog
import com.example.roomview.util.Constants.Companion.AGENT_REF_NO
import com.example.roomview.viewmodels.auth.RegisterViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    onRegister: () -> Unit, onBack: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    val agentReferralNo = remember { mutableStateOf("") }

    val imageUri = remember { mutableStateOf<Uri?>(null) }

    var showError by remember { mutableStateOf(false) }

    val options = mutableListOf<String>("User", "Agent")
    var selectedIndex by remember { mutableIntStateOf(0) }


    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri.value = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(title = { Text("Registration") }, navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
            }
        })

        Text(
            "Let's get started with your account details",
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            modifier = Modifier.padding(vertical = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp),
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
                    .wrapContentWidth()
                    .size(128.dp)
                    .aspectRatio(1F)
                    .padding(4.dp)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
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
                        Text(text = option)
                    }
                }
            }

            when (selectedIndex) {
                1 -> {
                    CustomTextField(
                        value = agentReferralNo,
                        onValueChange = { newValue ->
                            agentReferralNo.value = newValue
                        },
                        label = "Referral Number",
                        isPassword = false,
                        leadingIcon = null,
                        trailingIcon = null
                    )
                    Text(
                        text = "For prototype purposes, referral number is 1234",
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )
                }
            }

            // Full Name
            CustomTextField(
                value = firstName,
                onValueChange = { newValue ->
                    firstName.value = newValue
                },
                label = "First Name",
                isPassword = false,
                leadingIcon = null,
                trailingIcon = null
            )

            // Full Name
            CustomTextField(
                value = lastName,
                onValueChange = { newValue ->
                    lastName.value = newValue
                },
                label = "Last Name",
                isPassword = false,
                leadingIcon = null,
                trailingIcon = null
            )

            // Email
            CustomTextField(
                value = email,
                onValueChange = { newValue ->
                    email.value = newValue
                },
                label = "Email Address",
                isPassword = false,
                leadingIcon = null,
                trailingIcon = null
            )

            // Password
            CustomTextField(
                value = password,
                onValueChange = { newValue ->
                    password.value = newValue
                },
                label = "Password",
                isPassword = true,
                leadingIcon = null,
                trailingIcon = null
            )

            // Confirm Password
            CustomTextField(
                value = confirmPassword,
                onValueChange = { newValue ->
                    confirmPassword.value = newValue
                },
                label = "Re-enter your Password",
                isPassword = true,
                leadingIcon = null,
                trailingIcon = null
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.End),
                onClick = {

                    if (selectedIndex == 1 && agentReferralNo.value != AGENT_REF_NO) {
                        showError = true
                        viewModel.setMessage("Agent Referral Number is invalid!")
                        return@Button
                    }

                    if (password.value != confirmPassword.value) {
                        showError = true
                        viewModel.setMessage("Passwords do not match")
                        return@Button
                    }

                    scope.launch {

                        val data = viewModel.registerUser(
                            firstName = firstName.value.trim(),
                            lastName = lastName.value.trim(),
                            email = email.value.trim(),
                            password = password.value.trim(),
                            imageUri = imageUri.value,
                            selectedIndex
                        )

                        if (data != null) {
                            onRegister()
                        } else {
                            showError = true
                        }
                    }
                }
            ) {
                Text("Register")
            }
        }
        //Login Error Dialog
        if (showError) {
            WarningDialog(
                alertTitle = "Registration Failed",
                alertBody = viewModel.message.value /*"An error has occurred. Make sure that all fields are filled correctly and try again"*/,
                onDismissFun = { showError = false },
                buttonFun = { showError = false }
            )
        }

    }
}
