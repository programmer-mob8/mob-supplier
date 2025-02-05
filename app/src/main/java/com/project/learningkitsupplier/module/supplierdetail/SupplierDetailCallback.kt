package com.project.learningkitsupplier.module.supplierdetail

import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterData

data class SupplierDetailCallback(
    val deleteSupplier: (List<String>) -> Unit,
    val onFilter: (SupplierDetailFilterData) -> Unit = {},
    val resetMessageState: () -> Unit
)