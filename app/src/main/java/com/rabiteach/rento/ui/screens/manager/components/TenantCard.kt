package com.rabiteach.rento.ui.screens.manager.components

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.model.Tenant
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun TenantCard(tenant: Tenant) {
    val dueDate = tenant.nextPaymentDate!!.toDate()
    val isOverdue = dueDate.before(Date())
    val backgroundColor = if (isOverdue) Color(0xFFFFE6E6) else Color(0xFFE6FFE6)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(tenant.name, style = MaterialTheme.typography.bodyLarge)
                Text("House: ${tenant.houseNumber}")
                Text("Due: ${SimpleDateFormat("dd MMM yyyy").format(dueDate)}")
                if (isOverdue) {
                    Text("Overdue", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
            Button(onClick = { /* Collect */ }) {
                Text("Collect")
            }
        }
    }
}
