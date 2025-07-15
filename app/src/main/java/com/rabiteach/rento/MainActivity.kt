package com.rabiteach.rento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rabiteach.rento.repository.AppPreferences
import com.rabiteach.rento.repository.AppPreferences.clearUserData
import com.rabiteach.rento.ui.nav.UserRole
import com.rabiteach.rento.ui.nav.Screen
import com.rabiteach.rento.ui.screens.AccessCodeScreen
import com.rabiteach.rento.ui.screens.ManagerHomeScreen
import com.rabiteach.rento.ui.screens.TenantHomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            var startDestination by remember { mutableStateOf(Screen.AccessCode.route) }
            var userRole by remember { mutableStateOf<UserRole?>(null) }

            LaunchedEffect(true) {
                if (AppPreferences.isAccessCodeStored(context)) {
                    userRole = AppPreferences.getStoredRole(context)
                    startDestination = if (userRole == UserRole.TENANT)
                        Screen.TenantHome.route
                    else
                        Screen.ManagerHome.route
                }
            }

            AppNavigation(startDestination = startDestination, role = userRole)
        }
    }
}


@Composable
fun AppNavigation(startDestination: String, role: UserRole?) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController, startDestination = startDestination) {
        composable(Screen.AccessCode.route) {
            AccessCodeScreen { savedRole ->
                val nextRoute = if (savedRole == UserRole.TENANT)
                    Screen.TenantHome.route
                else Screen.ManagerHome.route

                navController.navigate(nextRoute) {
                    popUpTo(Screen.AccessCode.route) { inclusive = true }
                }
            }
        }

        composable(Screen.TenantHome.route) {
            TenantHomeScreen(onLogout = {
                CoroutineScope(Dispatchers.IO).launch {
                    clearUserData(context)
                    withContext(Dispatchers.Main) {
                        navController.navigate(Screen.AccessCode.route) {
                            popUpTo(0) // clear backstack
                        }
                    }
                }
            })
        }

        composable(Screen.ManagerHome.route) {
            ManagerHomeScreen(role = role ?: UserRole.ASSISTANT, onLogout = {
                CoroutineScope(Dispatchers.IO).launch {
                    clearUserData(context)
                    withContext(Dispatchers.Main) {
                        navController.navigate(Screen.AccessCode.route) {
                            popUpTo(0)
                        }
                    }
                }
            })
        }
    }
}
