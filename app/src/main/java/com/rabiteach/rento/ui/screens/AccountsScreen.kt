package com.rabiteach.rento.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rabiteach.rento.model.DateFilter
import com.rabiteach.rento.ui.screens.manager.components.FilterSection
import com.rabiteach.rento.ui.screens.manager.components.ReceiptList
import com.rabiteach.rento.ui.screens.manager.components.SummarySection
import com.rabiteach.rento.viewModels.AccountsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AccountsScreen(viewModel: AccountsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // 🔸 Summary
        SummarySection(
            totalReceipts = uiState.totalReceipts,
            totalCollected = uiState.totalCollected
        )

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(Modifier.height(8.dp))



        // 🔸 Search and Filters
        FilterSection(
            searchQuery = uiState.searchQuery,
            selectedDateFilter = uiState.selectedDateFilter,
            locationOptions = viewModel.getAvailableLocations(), // dynamic list
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onDateFilterChanged = viewModel::onDateFilterChanged,
            onLocationSelected = viewModel::onLocationFilterChanged,
            onFilterChange =  viewModel::updateDateFilter,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔸 List of Receipts
        ReceiptList(receipts = uiState.filteredReceipts)
    }
}
