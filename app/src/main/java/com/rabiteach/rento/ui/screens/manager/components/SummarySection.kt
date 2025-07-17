package com.rabiteach.rento.ui.screens.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SummarySection(totalReceipts: Int, totalCollected: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Receipts: $totalReceipts", style = MaterialTheme.typography.bodyLarge)
        Text("Total: UGX $totalCollected", style = MaterialTheme.typography.bodyLarge)
    }
}
