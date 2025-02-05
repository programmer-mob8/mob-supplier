package com.project.learningkitsupplier.ui.screen.changelog.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.project.learningkitsupplier.module.changelog.ChangelogListCallback
import com.project.learningkitsupplier.ui.screen.changelog.components.DownloadDialog
import com.project.learningkitsupplier.ui.screen.changelog.uistate.ChangelogUiState
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.components.DownloadSupplierDialogs
import com.tagsamurai.tscomponents.model.Menu
import com.tagsamurai.tscomponents.pagetitle.PageTitle
import com.tagsamurai.tscomponents.textfield.SearchFieldTopAppBar
import com.tagsamurai.tscomponents.topappbar.TopAppBar

@Composable
fun ChangelogTopBar(
    uiState: ChangelogUiState,
    changelogListCallback: ChangelogListCallback,
    onNavigateUp: () -> Unit
) {

    var showSearch by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }

    Column {

        if (showSearch){
            SearchFieldTopAppBar(
                onNavigateUp = {showSearch = false},
                onSearchConfirm = changelogListCallback.onSearch
            )
        } else {
            TopAppBar(
                menu = listOf(Menu.SEARCH, Menu.FILTER, Menu.DOWNLOAD),
                canNavigateBack = true,
                onMenuAction = {menu ->
                    when (menu) {
                        Menu.SEARCH -> showSearch = true
                        Menu.FILTER -> showFilter = true
                        Menu.DOWNLOAD -> showDialog = true
                        else -> Unit
                    }
                },
                navigateUp = onNavigateUp
            )
        }
        PageTitle(
            title = "Change log: Supplier"
        )
    }

    DownloadDialog(
        onDismissRequest = { state -> showDialog = state},
        showDialog = showDialog
    )

    ChangeLogFilterSheet(
        onDismissRequest = {state -> showFilter = state},
        uiState = uiState,
        showFilter = showFilter,
        onApplyConfirm = changelogListCallback.onFilter
    )
}
