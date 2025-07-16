package com.rabiteach.rento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.repository.AppPreferences
import com.rabiteach.rento.ui.nav.AppNavGraph
import com.rabiteach.rento.ui.nav.Screen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val context = this
            var startDestination by remember { mutableStateOf(Screen.AccessCode.route) }
            var role by remember { mutableStateOf<UserRole?>(null) }
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
                }
            }

            val navController = rememberNavController()
            AppNavGraph(navController, startDestination, role, context,onRoleUpdated)
        }
    }
}
