package com.rabiteach.rento.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.rabiteach.rento.repository.AppPreferences
import com.rabiteach.rento.ui.nav.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AccessCodeScreen(onCodeVerified: (UserRole) -> Unit) {
    var code by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserRole.TENANT) }

    val context = LocalContext.current

    Column {
        TextField(value = code, onValueChange = { code = it }, label = { Text("Enter Access Code") })
        // Add a dropdown or buttons for selecting role
        Button(onClick = {
            // Save to DataStore and navigate
            CoroutineScope(Dispatchers.IO).launch {
                AppPreferences.saveAccessCode(context, code, role)
                withContext(Dispatchers.Main) {
                    onCodeVerified(role)
                }
            }
        }) {
            Text("Submit")
        }
    }
}
