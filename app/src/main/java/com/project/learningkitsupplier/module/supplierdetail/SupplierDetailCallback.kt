package com.project.learningkitsupplier.module.supplierdetail

data class SupplierDetailCallback(
    val deleteSupplier: (List<String>) -> Unit,
    val onFilter: (SupplierDetailFilterData) -> Unit = {},
    val resetMessageState: () -> Unit
)