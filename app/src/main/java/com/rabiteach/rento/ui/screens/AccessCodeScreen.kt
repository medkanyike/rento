package com.rabiteach.rento.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)), // optional light background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
//                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(24.dp)
//                .shadow(4.dp, shape = RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "Access Code Login",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Enter Access Code") },
                isError = errorMessage != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (code.isBlank()) {
                        errorMessage = "Code cannot be empty"
                        return@Button
                    }
                    isLoading = true
                    errorMessage = null
                    CoroutineScope(Dispatchers.IO).launch {
                        val role = checkPasscodeAndGetRole(code)
                        isLoading = false
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
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Blue,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Submit")
                }
            }
        }
    }
}


@Composable
@Preview
fun ShowScreen(){
    AccessCodeScreen(){

    }
}