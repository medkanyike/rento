package com.rabiteach.rento.ui.nav

import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.repository.AppPreferences
import com.rabiteach.rento.ui.screens.AccessCodeScreen
import com.rabiteach.rento.ui.screens.ManagerHomeScreen
import com.rabiteach.rento.ui.screens.TenantHomeScreen

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    role: UserRole?,
    context: Context,
    onRoleUpdated:(UserRole)->Unit
) {
    NavHost(navController, startDestination = startDestination) {
        composable(Screen.AccessCode.route) {
            AccessCodeScreen { detectedRole ->
                onRoleUpdated(detectedRole)
                val target = if (detectedRole == UserRole.TENANT)
                    Screen.TenantHome.route else Screen.ManagerHome.route

                navController.navigate(target) {
                    popUpTo(Screen.AccessCode.route) { inclusive = true }
                }
            }
        }

        composable(Screen.TenantHome.route) {
            TenantHomeScreen(onLogout = {
                CoroutineScope(Dispatchers.IO).launch {
                    AppPreferences.clearUserData(context)
                    withContext(Dispatchers.Main) {
                        navController.navigate(Screen.AccessCode.route) {
                            popUpTo(0)
                        }
                    }
                }
            })
        }

        composable(Screen.ManagerHome.route) {
            ManagerHomeScreen(role = role ?: UserRole.ASSISTANT, onLogout = {
                CoroutineScope(Dispatchers.IO).launch {
                    AppPreferences.clearUserData(context)
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
