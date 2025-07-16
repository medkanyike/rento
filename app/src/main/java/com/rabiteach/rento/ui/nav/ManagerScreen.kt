package com.rabiteach.rento.ui.nav

sealed class ManagerScreen(val route: String, val label: String) {
    object Home : ManagerScreen("home", "Home")
    object Receipts : ManagerScreen("receipts", "Receipts")
    object Complaints : ManagerScreen("complaints", "Complaints")
    object Collected : ManagerScreen("collected", "Collected")
}



