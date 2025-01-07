package com.example.billsbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billsbuddy.domain.BillCategory
import com.example.billsbuddy.domain.BillItem
import com.example.billsbuddy.domain.SortOption
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillsScreen(
    modifier: Modifier = Modifier,
    viewModel: BillsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bills") },
                actions = {
                    FilterButton(
                        selectedCategories = uiState.selectedCategories,
                        onCategoryToggle = viewModel::toggleCategory
                    )
                    SortButton(
                        currentSort = uiState.sortOption,
                        onSortSelected = viewModel::updateSortOption
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::showAddDialog) {
                Icon(Icons.Default.Add, "Add Bill")
            }
        }
    ) { padding ->
        BillsList(
            bills = uiState.filteredBills,
            modifier = modifier.padding(padding)
        )
    }

    if (uiState.isAddDialogVisible) {
        AddBillDialog(
            onDismiss = viewModel::hideAddDialog,
            onConfirm = viewModel::addBill
        )
    }
}

@Composable
private fun FilterButton(
    selectedCategories: Set<BillCategory>,
    onCategoryToggle: (BillCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(Icons.Default.FilterList, "Filter")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        BillCategory.entries.forEach { category ->
            DropdownMenuItem(
                text = { Text(category.name) },
                leadingIcon = {
                    Checkbox(
                        checked = category in selectedCategories,
                        onCheckedChange = null
                    )
                },
                onClick = {
                    onCategoryToggle(category)
                }
            )
        }
    }
}

@Composable
private fun SortButton(
    currentSort: SortOption,
    onSortSelected: (SortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(Icons.Default.Sort, "Sort")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        SortOption.entries.forEach { sortOption ->
            DropdownMenuItem(
                text = { Text(sortOption.name.replace("_", " ")) },
                onClick = {
                    onSortSelected(sortOption)
                    expanded = false
                },
                leadingIcon = {
                    if (sortOption == currentSort) {
                        Icon(Icons.Default.Check, null)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddBillDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, LocalDate, BillCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(BillCategory.OTHER) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Bill") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Bill Name") }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") }
                )
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {},
                ) {
                    OutlinedTextField(
                        value = selectedCategory.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") }
                    )
                }
                // DatePicker would go here - simplified for now
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    amount.toDoubleOrNull()?.let { amountValue ->
                        onConfirm(name, amountValue, selectedDate, selectedCategory)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun BillsList(
    bills: List<BillItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
               verticalArrangement = Arrangement.spacedBy(8.dp),
               contentPadding = PaddingValues(16.dp)
    ) {
        items(bills) { bill ->
            BillCard(bill = bill)
        }
    }
}

@Composable
private fun BillCard(
    bill: BillItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Text(
                text = bill.name,
                 style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${bill.amount}",
                 style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Due: ${bill.dueDate}",
                 style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Category: ${bill.category}",
                 style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "No bills yet. Tap + to add one!",
             style = MaterialTheme.typography.bodyLarge,
             color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
