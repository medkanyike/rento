package com.rabiteach.rento.ui.screens.manager.components.utils

import com.rabiteach.rento.model.Tenant
import java.util.Calendar
import java.util.Date

fun groupTenants(tenants: List<Tenant>): Map<String, List<Tenant>> {
    val now = Calendar.getInstance()
    val grouped = mutableMapOf<String, MutableList<Tenant>>()

    tenants.forEach { tenant ->
        val due = tenant.nextPaymentDate!!.toDate()
        val cal = Calendar.getInstance().apply { time = due }

        val label = when {
            due.before(Date()) -> "Overdue"
            cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) -> "Due Today"
            cal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR) -> "This Week"
            cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) -> "This Month"
            else -> "Later"
        }

        grouped.getOrPut(label) { mutableListOf() }.add(tenant)
    }

    return grouped
}
