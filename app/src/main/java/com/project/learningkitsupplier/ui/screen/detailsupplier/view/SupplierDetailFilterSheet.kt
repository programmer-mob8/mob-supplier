package com.project.learningkitsupplier.ui.screen.detailsupplier.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailFilterData
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterData
import com.project.learningkitsupplier.ui.screen.detailsupplier.uistate.SupplierDetailUiState
import com.tagsamurai.tscomponents.bottomsheet.FilterBottomSheet
import com.tagsamurai.tscomponents.bottomsheet.TreeMultiSelectBottomSheet
import com.tagsamurai.tscomponents.chip.ChipSelectorWithOptionData
import com.tagsamurai.tscomponents.datepicker.FilterDatePicker

@Composable
fun SupplierDetailFilterSheet(
    onDismissRequest: (Boolean) -> Unit,
    uiState: SupplierDetailUiState,
    showFilter: Boolean,
    onApplyConfirm: (SupplierDetailFilterData) -> Unit
) {
    var tempFilterData by remember { mutableStateOf(uiState.filterData) }

    LaunchedEffect(Unit) {
        if(uiState.filterData != tempFilterData) {
            tempFilterData = uiState.filterData
        }
    }

    FilterBottomSheet(
        onDismissRequest = onDismissRequest,
        onApplyConfirm = {
            onApplyConfirm(tempFilterData)
            onDismissRequest(false)
        },
        onResetConfirm = {
            tempFilterData = SupplierDetailFilterData()
        },
        isItemSelected = tempFilterData != SupplierListFilterData(),
        isShowSheet = showFilter,
        content = { reset ->
            ChipSelectorWithOptionData(
                title = "Transaction",
                value = tempFilterData.transactionSelected,
                isReset = reset,
                items = uiState.filterOption.transactionOption,
                onChipsSelected = { result ->
                    tempFilterData = tempFilterData.copy(transactionSelected = result)
                }
            )

            TreeMultiSelectBottomSheet(
                title = "Group",
                nodes = uiState.filterOption.groupOption,
                value = tempFilterData.groupSelected,
                isCategory = false,
                isReset = reset,
                refreshing = uiState.isLoadingGroup,
                onResult = { result ->
                    tempFilterData = tempFilterData.copy(groupSelected = result)
                }
            )

            ChipSelectorWithOptionData(
                title = "PIC Name",
                value = tempFilterData.picSelected,
                isReset = reset,
                items = uiState.filterOption.picNameOption,
                onChipsSelected = { result ->
                    tempFilterData = tempFilterData.copy(picSelected = result)
                }
            )

            FilterDatePicker(
                title = "Finished Date",
                isReset = reset,
            )

        }
    )
}