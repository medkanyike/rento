package com.rabiteach.rento.ui.screens.manager

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.rabiteach.rento.viewModels.TenantViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDetailScreen(viewModel: TenantViewModel, tenantId: String, navController: NavHostController) {
    val tenant = viewModel.tenants.value.find { it.passcode == tenantId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tenant Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        tenant?.let {
            Column(modifier = Modifier
                .padding(padding)
                .padding(16.dp)
            ) {
                Text("Name: ${it.name}", style = MaterialTheme.typography.titleMedium)
                Text("Contact: ${it.contact}")
                Text("Location: ${it.location}")
                Text("House No: ${it.houseNumber}")
                Text("Entry Date: ${it.entryDate!!.toReadableDate()}")
                Text("Last Payment: ${it.lastPaymentDate!!.toReadableDate()}")
                it.nextPaymentDate?.let { it1 -> Text("Next Payment: ${it1.toReadableDate()}") }
                Text("Rent ID: ${it.rentID}")
                Text("Passcode: ${it.passcode}")

                Spacer(Modifier.height(16.dp))
                Text("Receipts", style = MaterialTheme.typography.titleMedium)
                if (it.receipts.isEmpty()) {
                    Text("No receipts yet.")
                } else {
                    it.receipts.forEach { receipt ->
                        Text("• ${receipt.receiptID} — ${receipt.date!!.toReadableDate()}")
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tenant not found", color = Color.Red)
            }
        }
    }
}

fun Timestamp.toReadableDate(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(this.toDate())
}

