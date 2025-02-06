package com.project.learningkitsupplier.ui.screen.detailsupplier.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailCallback
import com.project.learningkitsupplier.ui.screen.detailsupplier.component.DeleteDialogSupplierDetail
import com.project.learningkitsupplier.ui.screen.detailsupplier.uistate.SupplierDetailUiState
import com.tagsamurai.tscomponents.model.Menu
import com.tagsamurai.tscomponents.pagetitle.PageTitle
import com.tagsamurai.tscomponents.textfield.SearchFieldTopAppBar
import com.tagsamurai.tscomponents.topappbar.TopAppBar

@Composable
fun SupplierDetailTopBar(
    uiState: SupplierDetailUiState,
    navController: NavController,
    onNavigateUp: () -> Unit,
    supplierDetailCallback: SupplierDetailCallback
) {

    var showSearch by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column {
        if (showSearch) {
            SearchFieldTopAppBar(
                onNavigateUp = { showSearch = false },
                onSearchConfirm = {}
            )
        } else {
            TopAppBar(
                menu = listOf(Menu.SEARCH, Menu.FILTER, Menu.OTHER),
                canNavigateBack = true,
                onMenuAction = { menu ->
                    when (menu) {
                        Menu.SEARCH -> showSearch = true
                        Menu.FILTER -> showFilter = true
                        Menu.OTHER -> showActionSheet = true
                        else -> Unit
                    }
                },
                navigateUp = onNavigateUp ,
            )
        }

        PageTitle("Supplier Detail")
    }

    SupplierDetailActionSheet(
        navController = navController,
        uiState = uiState,
        onDismissRequest = {state -> showActionSheet = state},
        showSheet = showActionSheet,
        onDelete = { showDeleteDialog = true}
    )

    DeleteDialogSupplierDetail(
        item = uiState.supplierDetailEntity,
        onDismissRequest = { state -> showDeleteDialog = state },
        showDialog = showDeleteDialog,
        supplierDetailCallback = supplierDetailCallback,
        uiState = uiState
    )

    SupplierDetailFilterSheet(
        onDismissRequest = { state -> showFilter = state},
        uiState = uiState,
        showFilter = showFilter,
        onApplyConfirm = {}
    )
}
