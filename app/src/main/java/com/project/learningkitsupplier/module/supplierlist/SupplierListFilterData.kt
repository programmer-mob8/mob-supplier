package com.project.learningkitsupplier.module.supplierlist

data class SupplierListFilterData(
    val country: List<String> = emptyList(),
    val status: List<Boolean> = emptyList(),
    val companyName: List<String> = emptyList(),
    val itemName: List<String> = emptyList(),
    val modifiedBy: List<String> = emptyList(),
    val lastModified: List<Long> = emptyList()
)
