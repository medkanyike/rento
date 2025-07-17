package com.rabiteach.rento.viewModels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.rabiteach.rento.model.Receipt
import com.rabiteach.rento.model.Tenant
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date
import java.util.UUID

class TenantViewModel  @Inject constructor(
//    private val repository: TenantRepository // or whatever dependencies
)  : ViewModel() {
    private val db = Firebase.firestore
    private val tenantsRef = db.collection("tenants")

    private val _tenants = MutableStateFlow<List<Tenant>>(emptyList())
    val tenants: StateFlow<List<Tenant>> = _tenants.asStateFlow()

    var filteredTenants = mutableStateListOf<Tenant>()
        private set

    private var currentSortOrder = SortOrder.ASCENDING
    private val _page = mutableIntStateOf(1)
    private val _pageSize = 10

    val pagedTenants: List<Tenant>
        get() = filteredTenants.take(_page.intValue * _pageSize)

    fun loadMore() {
        _page.intValue += 1
    }


    init {
        fetchTenants()
    }


    fun fetchTenants() {
        tenantsRef.get().addOnSuccessListener { result ->
            val list = result.mapNotNull { it.toObject(Tenant::class.java) }
            _tenants.value = list
            filteredTenants.clear()
            filteredTenants.addAll(list)
        }
    }

    fun filterTenants(query: String) {
        val lower = query.trim().lowercase()
        filteredTenants.clear()
        filteredTenants.addAll(
            _tenants.value.filter {
                it.location.lowercase().contains(lower) || it.houseNumber.lowercase().contains(lower)
            }
        )
    }

    fun showOverdueOnly() {
        filteredTenants.clear()
        filteredTenants.addAll(
            _tenants.value.filter {
                it.nextPaymentDate!!.toDate().before(Date())
            }
        )
    }

    fun clearFilter() {
        filteredTenants.clear()
        filteredTenants.addAll(_tenants.value)
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

    fun sortByDueDate(order: SortOrder) {
        currentSortOrder = order
        filteredTenants.sortByDueDate(order)
    }

    private fun applyFiltersAndSort() {
        filteredTenants.clear()
        filteredTenants.addAll(_tenants.value.sortedByDueDate(currentSortOrder))
    }

    private fun List<Tenant>.sortedByDueDate(order: SortOrder): List<Tenant> {
        return when (order) {
            SortOrder.ASCENDING -> sortedBy { it.nextPaymentDate!!.toDate() }
            SortOrder.DESCENDING -> sortedByDescending { it.nextPaymentDate!!.toDate() }
        }
    }

    private fun SnapshotStateList<Tenant>.sortByDueDate(order: SortOrder) {
        val sorted = this.sortedByDueDate(order)
        this.clear()
        this.addAll(sorted)
    }

    fun createReceiptAndUpdateTenant(
        tenant: Tenant,
        amountPaid: Int,
        monthsPaid: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = Firebase.firestore
        val tenantDoc = db.collection("tenants")
            .whereEqualTo("passcode", tenant.passcode)
        val tenantIndex = filteredTenants.indexOfFirst { it.passcode == tenant.passcode }
        val tenant = filteredTenants[tenantIndex]

        tenantDoc.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val doc = querySnapshot.documents.first()
                val tenantRef = doc.reference
                val currentData = doc.toObject(Tenant::class.java) ?: return@addOnSuccessListener

                val newReceipt = Receipt(
                    receiptID = UUID.randomUUID().toString(),
                    date = Timestamp.now(),
                    amountPaid = amountPaid,
                    monthsPaidFor = monthsPaid
                )

                val updatedReceipts = currentData.receipts.toMutableList()
                updatedReceipts.add(newReceipt)

                val now = Timestamp.now()
                val calendar = Calendar.getInstance()
                calendar.time = now.toDate()
                calendar.add(Calendar.MONTH, monthsPaid)

                val newNextDate = Timestamp(calendar.time)

                tenantRef.update(
                    mapOf(
                        "receipts" to updatedReceipts,
                        "lastPaymentDate" to now,
                        "nextPaymentDate" to newNextDate,
                        "monthsPaidFor" to  monthsPaid
                    )
                ).addOnSuccessListener {
                    onSuccess()
                    val updatedTenant = tenant.copy(
                        receipts = updatedReceipts,
                        lastPaymentDate = now,
                        nextPaymentDate = newNextDate,
                        monthsPaidFor =  monthsPaid
                    )
                    filteredTenants[tenantIndex] = updatedTenant
                }.addOnFailureListener { it-> onFailure(it) }
            }
        }
    }
    }

