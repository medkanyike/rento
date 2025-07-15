package com.rabiteach.rento.ui.nav


enum class UserRole {
    LANDLORD,
    ASSISTANT,
    TENANT
}



sealed class Screen(val route: String) {
    object AccessCode : Screen("access_code")
    object TenantHome : Screen("tenant_home")
    object ManagerHome : Screen("manager_home") // landlord + assistant
}
