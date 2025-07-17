package com.rabiteach.rento.ui.screens.manager.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.model.AccountReceipt

@Composable
fun ReceiptList(receipts: List<AccountReceipt>) {
    LazyColumn {
        items(receipts) { receipt ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Tenant: ${receipt.tenantName}", style = MaterialTheme.typography.titleMedium)
                    Text("Amount: UGX ${receipt.amount}")
                    Text("Location: ${receipt.location}")
                    Text("Date: ${receipt.date}")
                }
            }
        }
    }
}
