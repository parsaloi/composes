package com.example.billsbuddy.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billsbuddy.domain.BillCategory
import com.example.billsbuddy.domain.BillItem
import com.example.billsbuddy.domain.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class BillsUiState(
    val bills: List<BillItem> = emptyList(),
    val filteredBills: List<BillItem> = emptyList(),
    val selectedCategories: Set<BillCategory> = BillCategory.values().toSet(),
    val sortOption: SortOption = SortOption.DUE_DATE,
    val isAddDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class BillsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState: StateFlow<BillsUiState> = _uiState.asStateFlow()

    private val _bills = mutableListOf<BillItem>()

    init {
        addSampleBills()
        updateFilteredBills()
    }

    private fun addSampleBills() {
        _bills.addAll(
            listOf(
                BillItem(
                    name = "Electricity",
                    amount = 85.50,
                    dueDate = LocalDate.now().plusDays(5),
                    category = BillCategory.UTILITIES
                ),
                BillItem(
                    name = "Internet",
                    amount = 59.99,
                    dueDate = LocalDate.now().plusDays(12),
                    category = BillCategory.UTILITIES
                ),
                BillItem(
                    name = "Rent",
                    amount = 1200.00,
                    dueDate = LocalDate.now().plusDays(1),
                    category = BillCategory.HOUSING
                )
            )
        )
        updateFilteredBills()
    }

    fun showAddDialog() {
        _uiState.update { it.copy(isAddDialogVisible = true) }
    }

    fun hideAddDialog() {
        _uiState.update { it.copy(isAddDialogVisible = false) }
    }

    fun addBill(name: String, amount: Double, dueDate: LocalDate, category: BillCategory) {
        _bills.add(BillItem(name = name, amount = amount, dueDate = dueDate, category = category))
        updateFilteredBills()
        hideAddDialog()
    }

    fun updateSortOption(sortOption: SortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
        updateFilteredBills()
    }

    fun toggleCategory(category: BillCategory) {
        _uiState.update { currentState ->
            val newCategories = currentState.selectedCategories.toMutableSet()
            if (newCategories.contains(category)) {
                newCategories.remove(category)
            } else {
                newCategories.add(category)
            }
            currentState.copy(selectedCategories = newCategories)
        }
        updateFilteredBills()
    }

    private fun updateFilteredBills() {
        _uiState.update { currentState ->
            val filteredBills = _bills
                .filter { it.category in currentState.selectedCategories }
                .sortedWith(getSortComparator(currentState.sortOption))
            currentState.copy(
                bills = _bills.toList(),
                filteredBills = filteredBills
            )
        }
    }

    private fun getSortComparator(sortOption: SortOption): Comparator<BillItem> =
        when (sortOption) {
            SortOption.DUE_DATE -> compareBy { it.dueDate }
            SortOption.AMOUNT_HIGH_TO_LOW -> compareByDescending { it.amount }
            SortOption.AMOUNT_LOW_TO_HIGH -> compareBy { it.amount }
            SortOption.NAME -> compareBy { it.name }
        }
}