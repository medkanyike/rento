package com.rabiteach.rento.ui.screens.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.viewModels.TenantViewModel

@Composable
fun HomeScreen(role: UserRole, viewModel: TenantViewModel) {
    val tenants by viewModel.tenants.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome, ${role.name.lowercase().replaceFirstChar { it.uppercase() }}!")
        Spacer(Modifier.height(16.dp))
        Text("Tenants to collect from:", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(tenants.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(tenants[index].name)
                            Text("House: ${tenants[index].houseNumber}")
                        }
                        Button(onClick = { /* TODO: Collect rent */ }) {
                            Text("Collect")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ComplaintsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Complaints Screen")
    }
}

@Composable
fun CollectedScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Collected Amounts Screen")
    }
}
