package com.rabiteach.rento.ui.nav






sealed class Screen(val route: String) {
    object AccessCode : Screen("access_code")
    object TenantHome : Screen("tenant_home")
    object ManagerHome : Screen("manager_home") // landlord + assistant
    object TenantDetail : Screen("tenantDetail/{tenantId}") {
        fun createRoute(tenantId: String) = "tenantDetail/$tenantId"
    }
}
