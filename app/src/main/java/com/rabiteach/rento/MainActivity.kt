package com.rabiteach.rento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.repository.AppPreferences
import com.rabiteach.rento.ui.nav.AppNavGraph
import com.rabiteach.rento.ui.nav.Screen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val context = this
            var startDestination by remember { mutableStateOf(Screen.AccessCode.route) }
            var role by remember { mutableStateOf<UserRole?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            val onRoleUpdated: (UserRole) -> Unit = { updatedRole ->
                role = updatedRole
            }

            LaunchedEffect(Unit) {
                if (AppPreferences.isAccessCodeStored(context)) {
                    role = AppPreferences.getStoredRole(context)
                    startDestination = when (role) {
                        UserRole.TENANT -> Screen.TenantHome.route
                        UserRole.LANDLORD, UserRole.ASSISTANT -> Screen.ManagerHome.route
                        else -> Screen.AccessCode.route
                    }
                } else {
                    startDestination = Screen.AccessCode.route
                }
                isLoading = false
            }

            val navController = rememberNavController()

            if (isLoading || startDestination == null) {
                LoadingScreen()
            } else {
                AppNavGraph(
                    navController = navController,
                    startDestination = startDestination,
                    role = role,
                    context = context,
                    onRoleUpdated = onRoleUpdated
                )
            }
        }

    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Or your theme background
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
