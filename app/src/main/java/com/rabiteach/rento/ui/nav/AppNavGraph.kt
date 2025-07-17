package com.rabiteach.rento.ui.nav

import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.repository.AppPreferences
import com.rabiteach.rento.ui.screens.AccessCodeScreen
import com.rabiteach.rento.ui.screens.ManagerHomeScreen
import com.rabiteach.rento.ui.screens.TenantHomeScreen

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rabiteach.rento.model.Tenant
import com.rabiteach.rento.ui.screens.manager.TenantDetailScreen
import com.rabiteach.rento.viewModels.TenantViewModel

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
//    val viewModel: TenantViewModel = viewModel()

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
            ManagerHomeScreen(role = role ?: UserRole.ASSISTANT,
                onTenantClick = { tenant ->
                    navController.navigate(Screen.TenantDetail.createRoute(tenant.passcode)) // unique ID
                },
//                viewModel=viewModel,
                onLogout = {
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
        composable(
            route = Screen.TenantDetail.route,
            arguments = listOf(navArgument("tenantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tenantId = backStackEntry.arguments?.getString("tenantId") ?: return@composable
            TenantDetailScreen(tenantId=tenantId, navController=navController)
        }
    }
}
