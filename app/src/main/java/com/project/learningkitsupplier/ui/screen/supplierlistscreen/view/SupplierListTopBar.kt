package com.project.learningkitsupplier.ui.screen.supplierlistscreen.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.learningkitsupplier.navigation.NavigationRoute
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.components.ActivateInactiveSupply
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.components.DeleteSupplierAlert
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.components.DownloadSupplierDialogs
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.viewmodel.SupplierListViewModel
import com.project.libs.data.model.SupplierEntity
import com.tagsamurai.tscomponents.model.Menu
import com.tagsamurai.tscomponents.pagetitle.PageTitle
import com.tagsamurai.tscomponents.tab.TabList
import com.tagsamurai.tscomponents.textfield.SearchFieldTopAppBar
import com.tagsamurai.tscomponents.topappbar.TopAppBar

@Composable
fun SupplierListTopBar(
    uiState: SupplierListUiState,
    supplierListCallback: SupplierListCallback,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavController,
    viewModel: SupplierListViewModel
) {

    var isActivate: Boolean? by remember { mutableStateOf(null) }
    val tabList = listOf("List", "Supplier Activities")
    val listMenu = getListMenu(uiState = uiState)

    Column {
        if (uiState.showSearch) {
            SearchFieldTopAppBar(
                onNavigateUp = { viewModel.showSearch(false) },
                onSearchConfirm = supplierListCallback.onSearch
            )
        } else {
            TopAppBar(
                menu = listMenu,
                canNavigateBack = true,
                onMenuAction = { menu ->
                    when (menu) {
                        Menu.SEARCH -> viewModel.showSearch(true)
                        Menu.SELECT_ALL, Menu.UNSELECT_ALL -> supplierListCallback.onToggleSelectAll()
                        Menu.FILTER -> viewModel.showFilter(true)
                        Menu.OTHER -> viewModel.showActionSheet(true)
                        Menu.DOWNLOAD -> viewModel.showDownloadDialog(true)
                        Menu.LOG -> navController.navigate(NavigationRoute.ChangelogListScreen.route)
                        else -> Unit
                    }
                },
                navigateUp = {},
                title = if (uiState.itemSelected.isNotEmpty()) "${uiState.itemSelected.size}" else "",
            )
        }

        PageTitle(
            title = if (selectedTabIndex == 0) "Supplier" else "Supplier Activities",
            bottomContent = {
                TabList(
                    onTabChange = { index ->
                        onTabSelected(index)
                        if (index == 1) {
                            viewModel.showSearch(false)
                        }
                    },
                    tabs = tabList,
                    selectedTabIndex = selectedTabIndex
                )
            }
        )
    }

    val item: SupplierEntity = uiState.itemSelected.firstOrNull() ?: SupplierEntity()

    SupplierListActionSheet(
        onDismissRequest = { state -> viewModel.showActionSheet(state) },
        isShowSheet = uiState.showActionSheet,
        item = item,
        uiState = uiState,
        onDelete = { viewModel.showDeleteDialog(true) },
        onUpdateStatus = {
            isActivate = it
            viewModel.showUpdateStatus(true)
        },
        navController = navController,
        supplierListCallback = supplierListCallback,
        onConfirmDismiss = { viewModel.showActionSheet(false) }
    )

    DeleteSupplierAlert(
        onDismissRequest = { state -> viewModel.showDeleteDialog(state) },
        supplier = uiState.itemSelected,
        showDialog = uiState.showDeleteDialog,
        onConfirm = {
            supplierListCallback.deleteSupplier(uiState.itemSelected.map { it.id })
            supplierListCallback.onClearSelectedItem()
            viewModel.showActionSheet(false)
        }
    )

    SupplierListFilterSheet(
        onDismissRequest = { state -> viewModel.showFilter(state) },
        uiState = uiState,
        showFilter = uiState.showFilter,
        onApplyConfirm = supplierListCallback.onFilter
    )

    isActivate?.let {state ->
        ActivateInactiveSupply(
            onDismissRequest = { state -> viewModel.showUpdateStatus(false) },
            supplier = uiState.itemSelected,
            showDialog = uiState.showUpdateStatus,
            onConfirm = {
                supplierListCallback.onStatusUpdate(uiState.itemSelected.map { it.id }, !state)
                supplierListCallback.onClearSelectedItem()
                viewModel.showActionSheet(false)
            },
            isActive = state,
        )
    }

    DownloadSupplierDialogs(
        onDismissRequest = { state -> viewModel.showDownloadDialog(state) },
        showDialog = uiState.showDownloadDialog,
        viewModel = viewModel
    )
}

fun getListMenu(uiState: SupplierListUiState): List<Menu> {
    val selectingMenu = listOf(
        Menu.SEARCH,
        if (uiState.isAllSelected) Menu.SELECT_ALL else Menu.UNSELECT_ALL,
        Menu.OTHER

    )

    val notSelectingMenu = listOf(Menu.SEARCH, Menu.FILTER, Menu.DOWNLOAD, Menu.LOG)

    return if (uiState.itemSelected.isNotEmpty()) selectingMenu else notSelectingMenu
}