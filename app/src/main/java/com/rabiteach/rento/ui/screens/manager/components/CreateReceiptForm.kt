package com.rabiteach.rento.ui.screens.manager.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rabiteach.rento.model.Tenant
import com.rabiteach.rento.viewModels.TenantViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateReceiptForm(
    tenant: Tenant,
    onReceiptCreated: () -> Unit,
    viewModel: TenantViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var amount by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var months by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Create Receipt for ${tenant.name}", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount Paid") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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

        if (loading) {
            CircularProgressIndicator()
        } else {
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {/*onDismissSheet*/}
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (amount.isBlank()) {
                            Toast.makeText(context, "Enter amount", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        loading = true
                        scope.launch {
                            viewModel.createReceiptAndUpdateTenant(
                                tenant = tenant,
                                amountPaid = amount.toInt(),
                                onSuccess = {
                                    loading = false
                                    Toast.makeText(context, "Receipt created", Toast.LENGTH_SHORT)
                                        .show()
                                    onReceiptCreated()
                                },
                                monthsPaid = months.toInt(),
                                onFailure = {
                                    loading = false
                                    Toast.makeText(
                                        context,
                                        "Failed: ${it.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    },
//                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}
