package com.project.learningkitsupplier.ui.screen.changelog.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.project.learningkitsupplier.module.changelog.ChangelogFilterData
import com.project.learningkitsupplier.ui.screen.changelog.uistate.ChangelogUiState
import com.tagsamurai.tscomponents.bottomsheet.FilterBottomSheet
import com.tagsamurai.tscomponents.chip.ChipSelectorWithOptionData
import com.tagsamurai.tscomponents.datepicker.FilterDatePicker

@Composable
fun ChangeLogFilterSheet(
    onDismissRequest: (Boolean) -> Unit,
    uiState: ChangelogUiState,
    showFilter: Boolean,
    onApplyConfirm: (ChangelogFilterData) -> Unit
) {
    
    var tempFilterData by remember { mutableStateOf(uiState.filterData) }

    LaunchedEffect(Unit) { 
        if (uiState.filterData != tempFilterData){
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
            tempFilterData = ChangelogFilterData()
        },
        isItemSelected = tempFilterData != ChangelogFilterData(),
        isShowSheet = showFilter,
        content = { reset ->
            
            FilterDatePicker(
                title = "Date",
                value = tempFilterData.date,
                isReset = reset,
                onApplyConfirm = { fromDate, toDate ->
                    val result = listOf(fromDate, toDate)
                    tempFilterData = if(!result.contains(0L)){
                        tempFilterData.copy(date = result)
                    } else {
                        tempFilterData.copy(date = emptyList())
                    }
                }
            )
            
            ChipSelectorWithOptionData(
                title = "Action",
                items = uiState.filterOption.actionOption,
                value = tempFilterData.action,
                isReset = reset,
                onChipsSelected = {result -> 
                    tempFilterData = tempFilterData.copy(action = result)
                }
            )

            ChipSelectorWithOptionData(
                title = "Field",
                items = uiState.filterOption.fieldOption,
                value = tempFilterData.field,
                isReset = reset,
                onChipsSelected = {result ->
                    tempFilterData = tempFilterData.copy(field = result)
                }
            )

            ChipSelectorWithOptionData(
                title = "Modified by",
                items = uiState.filterOption.modifiedBy,
                value = tempFilterData.modifiedBy,
                isReset = reset,
                onChipsSelected = {result ->
                    tempFilterData = tempFilterData.copy(modifiedBy = result)
                }
            )
        }
    ) 
}