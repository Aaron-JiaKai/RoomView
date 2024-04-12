@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.roomview.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun WarningDialog(
    alertTitle: String,
    alertBody: String,
    onDismissFun: () -> Unit,
    buttonFun: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissFun,
        confirmButton = {
            Button(
                modifier = Modifier.padding(4.dp),
                onClick = buttonFun) {
                Text("OK")
            }
        },
        title = { Text(alertTitle) },
        text = { Text(alertBody) },
    )
}
