package com.rabiteach.rento.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantHomeScreen(onLogout: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tenant Dashboard") }, actions = {
                IconButton(onClick = onLogout) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                }
            })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Welcome, Tenant!")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { /* View Receipts */ }) {
                Text("View Receipts")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { /* Raise Complaint */ }) {
                Text("Raise Complaint")
            }
        }
    }
}
