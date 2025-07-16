package com.rabiteach.rento.ui.screens.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabiteach.rento.model.Tenant
import com.rabiteach.rento.model.UserRole
import com.rabiteach.rento.ui.screens.manager.components.TenantCard
import com.rabiteach.rento.ui.screens.manager.components.utils.groupTenants
import com.rabiteach.rento.viewModels.SortOrder
import com.rabiteach.rento.viewModels.TenantViewModel

@Composable
fun HomeScreen(role: UserRole, onTenantClick: (Tenant) -> Unit, viewModel: TenantViewModel) {

    val searchQuery = remember { mutableStateOf("") }
    val showOverdue = remember { mutableStateOf(false) }
    val sortOrder = remember { mutableStateOf(SortOrder.ASCENDING) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome, ${role.name.lowercase().replaceFirstChar { it.uppercase() }}!")
        Spacer(Modifier.height(16.dp))
        Text("Tenants to collect from:", style = MaterialTheme.typography.titleMedium)

        // 🔍 Search field
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                viewModel.filterTenants(it)
            },
            label = { Text("Search by location or house") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // 📌 Overdue toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = showOverdue.value,
                onCheckedChange = {
                    showOverdue.value = it
                    if (it) {
                        viewModel.showOverdueOnly()
                    } else {
                        viewModel.filterTenants(searchQuery.value)
                    }
                }
            )
            Text("Show Overdue Only")
        }
        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sort by Due Date: ")
            Spacer(Modifier.width(8.dp))
            DropdownMenuWithSortOptions(sortOrder.value) { selected ->
                sortOrder.value = selected
                viewModel.sortByDueDate(selected)
            }
        }


        Spacer(Modifier.height(12.dp))


        val pagedTenants = viewModel.pagedTenants

//        val grouped = groupTenants(viewModel.filteredTenants)
        val grouped = groupTenants(pagedTenants)


        LazyColumn {
            grouped.forEach { (groupTitle, groupTenants) ->
                item {
                    Text(
                        text = groupTitle,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(groupTenants) { tenant ->
                    Box(modifier = Modifier.clickable { onTenantClick(tenant) }) {
                        TenantCard(tenant)
                    }
                }
            }

            item {
                if (pagedTenants.size < viewModel.filteredTenants.size) {
                    Button(
                        onClick = { viewModel.loadMore() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Load More")
                    }
                }
            }
        }
    }
}


@Composable
fun DropdownMenuWithSortOptions(
    selected: SortOrder,
    onSortSelected: (SortOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text("Sort: ${selected.name.lowercase().replaceFirstChar { it.uppercase() }}")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Ascending") }, onClick = {
                onSortSelected(SortOrder.ASCENDING)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Descending") }, onClick = {
                onSortSelected(SortOrder.DESCENDING)
                expanded = false
            })
        }
    }
}





@Composable
fun ComplaintsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Complaints Screen")
    }
}

@Composable
fun CollectedScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Collected Amounts Screen")
    }
}
