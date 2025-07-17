package com.rabiteach.rento.ui.screens.manager.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.model.DateFilter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    searchQuery: String,
    selectedDateFilter: DateFilter,
    locationOptions: List<String>,
    onSearchQueryChanged: (String) -> Unit,
    onDateFilterChanged: (DateFilter) -> Unit,
    onLocationSelected: (String?) -> Unit,
    onFilterChange: (DateFilter, Pair<LocalDate?, LocalDate?>?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
//    var customStartDate by remember { mutableStateOf<LocalDate?>(null) }
//    var customEndDate by remember { mutableStateOf<LocalDate?>(null) }

    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            label = { Text("Search tenant or house") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateFilter.entries.forEach { filter ->
                FilterChip(
                    selected = selectedDateFilter == filter,
                    onClick = {
                        if (filter == DateFilter.Custom) {
                            showDatePicker = true
                        } else {
                            onDateFilterChanged(filter)
                        }
                    },
                    label = { Text(filter.name) }
                )
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }
        var selectedLocation by remember { mutableStateOf<String?>("All") }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedLocation ?: "All",
                onValueChange = {},
                readOnly = true,
                label = { Text("Filter by Location") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(text = { Text("All") }, onClick = {
                    selectedLocation = null
                    onLocationSelected(null)
                    expanded = false
                })
                locationOptions.forEach { location ->
                    DropdownMenuItem(text = { Text(location) }, onClick = {
                        selectedLocation = location
                        onLocationSelected(location)
                        expanded = false
                    })
                }
            }
        }
    }

    if (showDatePicker) {
        DateRangePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { start, end ->
                onFilterChange(DateFilter.Custom, start to end)
                showDatePicker = false
            }
        )
    }

}
