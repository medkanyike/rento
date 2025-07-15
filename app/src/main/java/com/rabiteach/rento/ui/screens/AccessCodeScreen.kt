package com.rabiteach.rento.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.repository.AppPreferences
import com.rabiteach.rento.repository.FirestoreRepository.checkPasscodeAndGetRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AccessCodeScreen(onCodeVerified: (UserRole) -> Unit) {
    var code by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Enter Access Code") },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (code.isBlank()) {
                errorMessage = "Code cannot be empty"
                return@Button
            }

            errorMessage = null // Reset error
            CoroutineScope(Dispatchers.IO).launch {
                val role = checkPasscodeAndGetRole(code)
                if (role != null) {
                    AppPreferences.saveAccessCode(context, code, role)
                    withContext(Dispatchers.Main) {
                        onCodeVerified(role)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        errorMessage = "Invalid access code. Please try again."
                    }
                }
            }
        }) {
            Text("Submit")
        }
    }
}

