package com.project.learningkitsupplier.ui.screen.supplierlistscreen.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterData
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.tagsamurai.tscomponents.bottomsheet.FilterBottomSheet
import com.tagsamurai.tscomponents.chip.ChipSelectorLoading
import com.tagsamurai.tscomponents.chip.ChipSelectorWithOptionData
import com.tagsamurai.tscomponents.datepicker.FilterDatePicker


@Composable
fun LoadingFilterSheet(){
    val filterList = listOf(
        "Status",
        "Supplier",
        "City",
        "Item name",
        "Modified By",
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        filterList.forEach { field ->
            ChipSelectorLoading(
                title = field
            )
        }
    }
}

@Composable
fun SupplierListFilterSheet(
    onDismissRequest: (Boolean) -> Unit,
    uiState: SupplierListUiState,
    showFilter: Boolean,
    onApplyConfirm: (SupplierListFilterData) -> Unit
) {
    var tempFilterData by remember { mutableStateOf(uiState.filterData) }


    LaunchedEffect(Unit) {
        if (uiState.filterData != tempFilterData) {
            tempFilterData = uiState.filterData
        }
    }

    FilterBottomSheet(
        onDismissRequest = {
            tempFilterData = uiState.filterData
            onDismissRequest(false)
        },
        onApplyConfirm = {
            onApplyConfirm(tempFilterData)
            onDismissRequest(false)
        },
        onResetConfirm = {
            tempFilterData = SupplierListFilterData()
        },
        isItemSelected = tempFilterData != SupplierListFilterData(),
        isShowSheet = showFilter,
        content = { reset ->

            if (uiState.isLoadingGroup) {
                LoadingFilterSheet()
            } else {

                ChipSelectorWithOptionData(
                    title = "Status",
                    items = uiState.filterOption.statusOption,
                    value = tempFilterData.status,
                    isReset = reset,
                    onChipsSelected = { result ->
                        tempFilterData = tempFilterData.copy(status = result)
                    }
                )

                ChipSelectorWithOptionData(
                    title = "Supplier",
                    value = tempFilterData.companyName,
                    items = uiState.filterOption.companyNameOption,
                    isReset = reset,
                    onChipsSelected = { result ->
                        tempFilterData = tempFilterData.copy(companyName = result)
                    }
                )

                ChipSelectorWithOptionData(
                    title = "City",
                    value = tempFilterData.country,
                    items = uiState.filterOption.cityOption,
                    isReset = reset,
                    onChipsSelected = { result ->
                        tempFilterData = tempFilterData.copy(country = result)
                    }
                )

                ChipSelectorWithOptionData(
                    title = "Item name",
                    value = tempFilterData.itemName,
                    items = uiState.filterOption.itemNameOption,
                    isReset = reset,
                    onChipsSelected = { result ->
                        tempFilterData = tempFilterData.copy(itemName = result)
                    }
                )

                ChipSelectorWithOptionData(
                    title = "Modified By",
                    value = tempFilterData.modifiedBy,
                    items = uiState.filterOption.modifiedByOption,
                    isReset = reset,
                    onChipsSelected = { result ->
                        tempFilterData = tempFilterData.copy(modifiedBy = result)
                    }
                )

                FilterDatePicker(
                    title = "Last Modified",
                    value = tempFilterData.lastModified,
                    isReset = reset,
                    onApplyConfirm = { dateFrom, dateTo ->
                        val result = listOf(dateFrom, dateTo)
                        tempFilterData = if (!result.contains(0L)) {
                            tempFilterData.copy(lastModified = result)
                        } else {
                            tempFilterData.copy(lastModified = emptyList())
                        }
                    }
                )
            }
        }
    )
}