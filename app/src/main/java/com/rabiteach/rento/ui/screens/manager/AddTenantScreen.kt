package com.rabiteach.rento.ui.screens.manager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.viewModels.TenantViewModel

@Composable
fun AddTenantScreen(viewModel: TenantViewModel, onTenantAdded: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var houseNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var passcode by remember { mutableStateOf("") }
    var monthsPaid by remember { mutableStateOf("1") }
    var amountPaid by remember { mutableStateOf("10") }
    var error by remember { mutableStateOf<String?>(null) }


    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
    ) {
        Text("Add New Tenant", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Contact") })
        OutlinedTextField(value = houseNumber, onValueChange = { houseNumber = it }, label = { Text("House Number") })
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
        OutlinedTextField(value = passcode, onValueChange = { passcode = it }, label = { Text("Passcode") })
        OutlinedTextField(
            value = monthsPaid,
            onValueChange = { monthsPaid = it.filter { c -> c.isDigit() } },
            label = { Text("Months Paid For") }
        )
        OutlinedTextField(
            value = amountPaid,
            onValueChange = { amountPaid = it.filter { c -> c.isDigit() } },
            label = { Text("Amount Paid") }
        )

        error?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val paidMonths = monthsPaid.toIntOrNull() ?: 1
            val amountPaid = amountPaid.toIntOrNull() ?: 1
            viewModel.addTenant(
                name = name,
                contact = contact,
                houseNumber = houseNumber,
                location = location,
                passcode = passcode,
                monthsPaidFor = paidMonths,
                onSuccess = {
                    onTenantAdded()
                    name = ""
                    contact = ""
                    houseNumber = ""
                    location = ""
                    passcode = ""
                },
                amountPaid = amountPaid,
                onError = {
                    error = it
                }
            )
        }) {
            Text("Add Tenant")
        }
    }
}
