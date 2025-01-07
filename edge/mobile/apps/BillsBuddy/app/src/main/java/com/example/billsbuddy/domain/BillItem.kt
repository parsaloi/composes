package com.example.billsbuddy.domain

import java.time.LocalDate
import java.util.UUID

data class BillItem(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: Double,
    val dueDate: LocalDate,
    val category: BillCategory = BillCategory.OTHER,
    val isPaid: Boolean = false
)