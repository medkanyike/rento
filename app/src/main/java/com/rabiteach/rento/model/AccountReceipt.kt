package com.rabiteach.rento.model

import java.time.LocalDate

data class AccountReceipt(
    val receiptID: String,
    val tenantName: String,
    val houseNumber: String,
    val location: String,
    val amount: Int,
    val date: LocalDate
)

enum class DateFilter {
    Today, ThisWeek, ThisMonth, Custom
}
