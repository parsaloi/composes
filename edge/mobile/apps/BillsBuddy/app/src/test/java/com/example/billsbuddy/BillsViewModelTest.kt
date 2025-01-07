package com.example.billsbuddy

import com.example.billsbuddy.domain.BillCategory
import com.example.billsbuddy.domain.SortOption
import com.example.billsbuddy.ui.screens.BillsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BillsViewModelTest {
    private lateinit var viewModel: BillsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BillsViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has sample bills`() = runTest {
        val bills = viewModel.uiState.value.bills
        assertTrue(bills.isNotEmpty())
    }

    @Test
    fun `adding new bill updates state`() = runTest {
        val initialCount = viewModel.uiState.value.bills.size
        viewModel.addBill(
            name = "Test Bill",
            amount = 100.0,
            dueDate = LocalDate.now(),
            category = BillCategory.OTHER
        )
        assertEquals(initialCount + 1, viewModel.uiState.value.bills.size)
    }

    @Test
    fun `filtering by category shows only selected categories`() = runTest {
        // Start with all categories
        val initialBills = viewModel.uiState.value.filteredBills

        // Filter to only UTILITIES
        viewModel.toggleCategory(BillCategory.HOUSING)
        viewModel.toggleCategory(BillCategory.ENTERTAINMENT)
        viewModel.toggleCategory(BillCategory.INSURANCE)
        viewModel.toggleCategory(BillCategory.TRANSPORTATION)
        viewModel.toggleCategory(BillCategory.OTHER)

        val filteredBills = viewModel.uiState.value.filteredBills
        assertTrue(filteredBills.all { it.category == BillCategory.UTILITIES })
    }

    @Test
    fun `sorting by amount high to low works correctly`() = runTest {
        viewModel.addBill("High Bill", 200.0, LocalDate.now(), BillCategory.OTHER)
        viewModel.addBill("Low Bill", 50.0, LocalDate.now(), BillCategory.OTHER)

        viewModel.updateSortOption(SortOption.AMOUNT_HIGH_TO_LOW)

        val sortedBills = viewModel.uiState.value.filteredBills
        for (i in 0 until sortedBills.size - 1) {
            assertTrue(sortedBills[i].amount >= sortedBills[i + 1].amount)
        }
    }

    @Test
    fun `toggle add dialog visibility works`() = runTest {
        assertFalse(viewModel.uiState.value.isAddDialogVisible)

        viewModel.showAddDialog()
        assertTrue(viewModel.uiState.value.isAddDialogVisible)

        viewModel.hideAddDialog()
        assertFalse(viewModel.uiState.value.isAddDialogVisible)
    }

    @Test
    fun `filter with no categories selected shows empty list`() = runTest {
        BillCategory.entries.forEach { viewModel.toggleCategory(it) }
        assertTrue(viewModel.uiState.value.filteredBills.isEmpty())
    }

    @Test
    fun `sorting by name works correctly`() = runTest {
        viewModel.addBill("A Bill", 100.0, LocalDate.now(), BillCategory.OTHER)
        viewModel.addBill("B Bill", 100.0, LocalDate.now(), BillCategory.OTHER)
        viewModel.addBill("C Bill", 100.0, LocalDate.now(), BillCategory.OTHER)

        viewModel.updateSortOption(SortOption.NAME)

        val sortedBills = viewModel.uiState.value.filteredBills
        for (i in 0 until sortedBills.size - 1) {
            assertTrue(sortedBills[i].name <= sortedBills[i + 1].name)
        }
    }
}