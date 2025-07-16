package com.rabiteach.rento.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rabiteach.rento.model.Tenant
import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.ui.nav.ManagerScreen
import com.rabiteach.rento.ui.nav.Screen
import com.rabiteach.rento.ui.screens.manager.AddTenantScreen
import com.rabiteach.rento.ui.screens.manager.CollectedScreen
import com.rabiteach.rento.ui.screens.manager.ComplaintsScreen
import com.rabiteach.rento.ui.screens.manager.HomeScreen
import com.rabiteach.rento.viewModels.TenantViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerHomeScreen(
    role: UserRole,
    onLogout: () -> Unit
) {
    val navItems = listOf(
        ManagerScreen.Home,
        ManagerScreen.AddTenant,
        ManagerScreen.Complaints,
        ManagerScreen.Collected
    )
//    TODO:We may have to move the viewModel more up
    val viewModel: TenantViewModel = viewModel()
    val isLandlord = role == UserRole.LANDLORD
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manager Dashboard") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = navController.currentDestination?.route == item.route,
                        onClick = { navController.navigate(item.route) },
                        icon = {
                            when (item) {
                                is ManagerScreen.Home -> Icon(Icons.Default.Home, null)
                                is ManagerScreen.AddTenant -> Icon(Icons.Default.Add, null)
                                is ManagerScreen.Complaints -> Icon(Icons.Default.Warning, null)
                                is ManagerScreen.Collected -> Icon(Icons.Default.Notifications, null)
                            }
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ManagerScreen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(ManagerScreen.Home.route) {
                HomeScreen(role, viewModel)
            }
            composable(ManagerScreen.AddTenant.route) {
                AddTenantScreen(viewModel){
                    showBottomSheet = true
                }
            }
            composable(ManagerScreen.Complaints.route) {
                ComplaintsScreen()
            }
            composable(ManagerScreen.Collected.route) {
                CollectedScreen()
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }) {
                    Text("Added Successfully")
                }
            }
        }
    }
}