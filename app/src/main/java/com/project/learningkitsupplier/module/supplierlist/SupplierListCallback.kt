package com.project.learningkitsupplier.module.supplierlist

import com.project.libs.data.model.SupplierEntity

data class SupplierListCallback(
    val onRefresh: () -> Unit,
    val onSearch: (String) -> Unit,
    val onFilter: (SupplierListFilterData) -> Unit = {},
    val onToggleSelectAll: () -> Unit = {},
    val onUpdateSelectedItem: (SupplierEntity) -> Unit = {},
    val onClearSelectedItem: () -> Unit,
    val onStatusUpdate: (List<String>, Boolean) -> Unit,
    val deleteSupplier: (List<String>) -> Unit,
    val onResetMessageState: () -> Unit = {},
    val onUpdateSupplier: (SupplierEntity) -> Unit = {}
)