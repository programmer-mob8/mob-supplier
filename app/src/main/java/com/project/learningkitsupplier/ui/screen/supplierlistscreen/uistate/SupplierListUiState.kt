package com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate

import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterData
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterOption
import com.project.libs.data.model.SupplierEntity
import com.project.libs.data.source.network.model.request.GetSupplierListParamsRequest
import com.tagsamurai.tscomponents.utils.Utils

data class SupplierListUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isLoadingGroup: Boolean = false,
    val isAllSelected: Boolean = false,
    val itemSelected: List<SupplierEntity> = emptyList(),
    val searchQuery: String = "",
    val filterOption: SupplierListFilterOption = SupplierListFilterOption(),
    val filterData: SupplierListFilterData = SupplierListFilterData(),
    val supplierDefault: List<SupplierEntity> = emptyList(),
    val supplier: List<SupplierEntity> = emptyList(),
    val deleteState: Boolean? = null,
    val statusState: Boolean? = null,
    val isActive: Boolean = false
) {
    val queryParams
        get() = GetSupplierListParamsRequest(
            search = searchQuery.ifBlank { null },
            status = Utils.toJsonIfNotEmpty(filterData.status),
            supplier = Utils.toJsonIfNotEmpty(filterData.companyName),
            city = Utils.toJsonIfNotEmpty(filterData.country),
            date = Utils.toJsonIfNotEmpty(filterData.lastModified),
            itemName = Utils.toJsonIfNotEmpty(filterData.itemName),
            modifiedBy = Utils.toJsonIfNotEmpty(filterData.modifiedBy),
        )
}