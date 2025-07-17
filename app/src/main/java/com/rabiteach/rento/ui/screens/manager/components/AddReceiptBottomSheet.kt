package com.rabiteach.rento.ui.screens.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.model.Tenant

@Composable
fun AddReceiptBottomSheet(
    tenant: Tenant,
    onDismiss: () -> Unit,
    onSubmit: (amountPaid: Int, monthsPaid: Int) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Add Receipt for ${tenant.name}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount Paid") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = months,
            onValueChange = { months = it },
            label = { Text("Months Paid For") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    val amt = amount.toIntOrNull() ?: 0
                    val mnth = months.toIntOrNull() ?: 0
                    if (amt > 0 && mnth > 0) {
                        onSubmit(amt, mnth)
                        onDismiss()
                    }
                }
            ) {
                Text("Submit")
            }
        }
    }
}
