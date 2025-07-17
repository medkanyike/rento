package com.rabiteach.rento.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.rabiteach.rento.model.AccountReceipt
import com.rabiteach.rento.model.DateFilter
import com.rabiteach.rento.model.Receipt
import com.rabiteach.rento.model.Tenant
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

data class AccountsUiState(
    val totalCollected: Int = 0,
    val totalReceipts: Int = 0,
    val searchQuery: String = "",
    val selectedLocation: String = "All",
    val selectedDateFilter: DateFilter = DateFilter.Today,
    val filteredReceipts: List<AccountReceipt> = emptyList(),
    val customDateRange: Pair<LocalDate?, LocalDate?>? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AccountsViewModel @Inject constructor(
//    private val db: FirebaseFirestore
) : ViewModel() {

    private val db = Firebase.firestore


    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState

    private var allReceipts: List<AccountReceipt> = emptyList()




    init {
        fetchReceiptsFromTenants()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchReceiptsFromTenants() {
        db.collection("tenants").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            val tenants = snapshot.toObjects(Tenant::class.java)
            allReceipts = tenants.flatMap { it.toAccountReceipts() }

            applyFilters()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun applyFilters() {
        val state = _uiState.value
        val now = LocalDate.now()

        val filtered = allReceipts.filter { receipt ->
            val matchQuery = receipt.tenantName.contains(state.searchQuery, ignoreCase = true)
                    || receipt.houseNumber.contains(state.searchQuery, ignoreCase = true)

            val matchDate = when (state.selectedDateFilter) {
                DateFilter.Today -> receipt.date == now
                DateFilter.ThisWeek -> receipt.date.isAfter(now.minusDays(7))
                DateFilter.ThisMonth -> receipt.date.month == now.month
                DateFilter.Custom -> {
                    val (start, end) = state.customDateRange ?: return@filter false
                    if (start == null || end == null) false else
                        !receipt.date.isBefore(start) && !receipt.date.isAfter(end)
                }
            }

            val matchLocation = state.selectedLocation == "All" || receipt.location == state.selectedLocation

            matchQuery && matchDate && matchLocation
        }

        _uiState.value = _uiState.value.copy(
            filteredReceipts = filtered,
            totalCollected = filtered.sumOf { it.amount },
            totalReceipts = filtered.size
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onLocationFilterChanged(location: String?) {
        if (location != null )
            _uiState.update { it.copy(selectedLocation = location) }
        applyFilters()
    }

    fun getAvailableLocations(): List<String> {
        return allReceipts.map { it.location }.distinct().sorted()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateFilterChanged(filter: DateFilter) {
        _uiState.update { it.copy(selectedDateFilter = filter) }
        applyFilters()
    }

    fun setAllReceipts(receipts: List<AccountReceipt>) {
        allReceipts = receipts
        applyFilters()
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun updateDateFilter(filter: DateFilter, customRange: Pair<LocalDate?, LocalDate?>? = null) {
        _uiState.value = _uiState.value.copy(
            selectedDateFilter = filter,
            customDateRange = customRange
        )
        applyFilters()
    }

    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(selectedLocation = location)
        applyFilters()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun Tenant.toAccountReceipts(): List<AccountReceipt> {
        return receipts.map { receipt ->
            AccountReceipt(
                receiptID = receipt.receiptID,
                tenantName = this.name,
                houseNumber = this.houseNumber,
                location = this.location,
                amount = receipt.amountPaid,
                date = receipt.date?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate() ?: LocalDate.now()
            )
        }
    }
}
