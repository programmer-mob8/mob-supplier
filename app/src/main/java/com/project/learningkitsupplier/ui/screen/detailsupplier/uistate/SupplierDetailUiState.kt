package com.project.learningkitsupplier.ui.screen.detailsupplier.uistate

import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailFilterData
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailFilterOption
import com.project.libs.data.model.SupplierDetailEntity

data class SupplierDetailUiState(
    val isLoadingGroup: Boolean = false,
    val showBottomSheet: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val supplierId: String = "",
    val supplierDetailEntity: SupplierDetailEntity = SupplierDetailEntity(),
    val deleteState: Boolean? = null,
    val filterOption: SupplierDetailFilterOption = SupplierDetailFilterOption(),
    val filterData: SupplierDetailFilterData = SupplierDetailFilterData(),
)