package com.rabiteach.rento.ui.screens.manager.components

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate, LocalDate) -> Unit
) {
    val context = LocalContext.current
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Date Range") },
        text = {
            Column {
                DateInput(label = "Start Date") { selected ->
                    startDate = selected
                }
                Spacer(Modifier.height(8.dp))
                DateInput(label = "End Date") { selected ->
                    endDate = selected
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (startDate != null && endDate != null) {
                        onDateSelected(startDate!!, endDate!!)
                    }
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateInput(label: String, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    var dateText by remember { mutableStateOf(label) }

    Button(onClick = {
        val picker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = LocalDate.of(year, month + 1, dayOfMonth)
                dateText = date.toString()
                onDateSelected(date)
            },
            today.year, today.monthValue - 1, today.dayOfMonth
        )
        picker.show()
    }) {
        Text(dateText)
    }
}

