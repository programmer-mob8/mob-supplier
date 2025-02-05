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
    navController: NavController
) {

    var showSearch by remember { mutableStateOf(false) }
    var isActivate: Boolean? by remember { mutableStateOf(null) }
    var showActionSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showUpdateStatus by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }
    val tabList = listOf("List", "Supplier Activities")
    val listMenu = getListMenu(uiState = uiState)

    Column {
        if (showSearch) {
            SearchFieldTopAppBar(
                onNavigateUp = { showSearch = false },
                onSearchConfirm = supplierListCallback.onSearch
            )
        } else {
            TopAppBar(
                menu = listMenu,
                canNavigateBack = true,
                onMenuAction = { menu ->
                    when (menu) {
                        Menu.SEARCH -> showSearch = true
                        Menu.SELECT_ALL, Menu.UNSELECT_ALL -> supplierListCallback.onToggleSelectAll()
                        Menu.FILTER -> showFilter = true
                        Menu.OTHER -> showActionSheet = true
                        Menu.DOWNLOAD -> showDownloadDialog = true
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
                            showSearch = false
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
        onDismissRequest = { state -> showActionSheet = state },
        isShowSheet = showActionSheet,
        item = item,
        uiState = uiState,
        onDelete = { showDeleteDialog = true },
        onUpdateStatus = {
            isActivate = it
            showUpdateStatus = true
        },
        navController = navController,
        supplierListCallback = supplierListCallback,
        onConfirmDismiss = { showActionSheet = false}
    )

    DeleteSupplierAlert(
        onDismissRequest = { state -> showDeleteDialog = state },
        supplier = uiState.itemSelected,
        showDialog = showDeleteDialog,
        supplierListCallback = supplierListCallback,
    )

    SupplierListFilterSheet(
        onDismissRequest = { state -> showFilter = state },
        uiState = uiState,
        showFilter = showFilter,
        onApplyConfirm = supplierListCallback.onFilter
    )

    isActivate?.let {state ->
        ActivateInactiveSupply(
            onDismissRequest = { state -> showUpdateStatus = state },
            supplier = uiState.itemSelected,
            showDialog = showUpdateStatus,
            onConfirm = {
                supplierListCallback.onStatusUpdate(uiState.itemSelected.map { it.id }, !state)
                supplierListCallback.onClearSelectedItem()
                showActionSheet = false
            },
            isActive = state,
        )
    }

    DownloadSupplierDialogs(
        onDismissRequest = { state -> showDownloadDialog = state },
        showDialog = showDownloadDialog
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