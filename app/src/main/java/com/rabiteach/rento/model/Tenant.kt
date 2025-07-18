package com.rabiteach.rento.model

import com.google.firebase.Timestamp

data class Tenant(
    val name: String = "",
    val contact: String = "",
    val entryDate: Timestamp? = null,
    val houseNumber: String = "",
    val lastPaymentDate: Timestamp? = null,
    val nextPaymentDate: Timestamp? = null,
    val location: String = "",
    val passcode: String = "",
    val rentID: String = "",
    val monthsPaidFor: Int = 1,
    val receipts: List<Receipt> = emptyList()
)

data class Receipt(
    val receiptID: String = "",
    val date: Timestamp? = null,
    val amountPaid:Int=1,
    val monthsPaidFor: Int = 1,
    )
