package com.rabiteach.rento.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.rabiteach.rento.model.Receipt
import com.rabiteach.rento.model.Tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

class TenantViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val tenantsRef = db.collection("tenants")

    private val _tenants = MutableStateFlow<List<Tenant>>(emptyList())
    val tenants: StateFlow<List<Tenant>> = _tenants.asStateFlow()

    init {
        fetchTenants()
    }

    fun fetchTenants() {
        tenantsRef.get().addOnSuccessListener { result ->
            val list = result.mapNotNull { it.toObject(Tenant::class.java) }
            _tenants.value = list
        }
    }

    suspend fun isPasscodeUnique(passcode: String): Boolean {
        val snapshot = tenantsRef.whereEqualTo("passcode", passcode).get().await()
        return snapshot.isEmpty
    }

    fun addTenant(
        name: String,
        contact: String,
        houseNumber: String,
        location: String,
        passcode: String,
        monthsPaidFor: Int,
        rentID: String? = null,
        amountPaid:Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (!isPasscodeUnique(passcode)) {
                onError("Passcode already in use.")
                return@launch
            }

            val now = Timestamp.now()
            val calendar = Calendar.getInstance()
            calendar.time = now.toDate()
            calendar.add(Calendar.MONTH, monthsPaidFor)
            val nextPayment = Timestamp(calendar.time)

            val receipt = Receipt(
                amountPaid = amountPaid,
                receiptID = "323",
                date = now
            )

            val receipts = listOf(receipt)

            val newTenant = Tenant(
                name = name,
                contact = contact,
                entryDate = now,
                lastPaymentDate = now,
                nextPaymentDate = nextPayment,
                location = location,
                houseNumber = houseNumber,
                passcode = passcode,
                monthsPaidFor = monthsPaidFor,
                rentID = "rentID",
                receipts = receipts
            )

            tenantsRef.add(newTenant)
                .addOnSuccessListener {
                    fetchTenants()
                    onSuccess()
                }
                .addOnFailureListener {
                    onError(it.message ?: "Failed to add tenant.")
                }
        }
    }

    fun filterByLocation(location: String): List<Tenant> =
        tenants.value.filter { it.location.equals(location, ignoreCase = true) }

    fun filterByHouseNumber(houseNumber: String): List<Tenant> =
        tenants.value.filter { it.houseNumber == houseNumber }

    fun filterOverdueTenants(): List<Tenant> =
        tenants.value.filter {
            it.nextPaymentDate?.toDate()!!.before(Date())
        }
}