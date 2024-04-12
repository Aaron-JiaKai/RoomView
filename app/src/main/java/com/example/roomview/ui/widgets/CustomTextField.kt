package com.example.roomview.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomTextField(
    value: MutableState<String>,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isSingleLine: Boolean = false,
    maxLines: Int = 1,
    leadingIcon: @Composable() (() -> Unit)? = {},
    trailingIcon: @Composable() (() -> Unit)? = {},
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChange(it)
        },
        label = {
            Text(label)
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = isSingleLine,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        enabled = !readOnly,
        modifier = modifier
            .fillMaxWidth()
    )
}