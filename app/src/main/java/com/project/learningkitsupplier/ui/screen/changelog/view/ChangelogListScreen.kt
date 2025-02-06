package com.project.learningkitsupplier.ui.screen.changelog.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.learningkitsupplier.module.changelog.ChangelogListCallback
import com.project.learningkitsupplier.ui.screen.changelog.uistate.ChangelogUiState
import com.project.learningkitsupplier.ui.screen.changelog.view.changeloglist.LoadChangelogList
import com.project.learningkitsupplier.ui.screen.changelog.viewmodel.ChangelogViewModel
import com.tagsamurai.tscomponents.handlestate.HandleDownloadState
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar

@Composable
fun ChangelogListScreen(
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar
) {
    val changelogListViewModel: ChangelogViewModel = hiltViewModel()
    val uiState = changelogListViewModel.uiState.collectAsStateWithLifecycle()
    val changelogListCallback = changelogListViewModel.getCallback()

    LaunchedEffect(Unit) {
        changelogListViewModel.init()
    }

    ChangelogListScreen(
        uiState = uiState.value,
        changelogListCallback = changelogListCallback,
        onNavigateUp = onNavigateUp,
        viewModel = changelogListViewModel,
        onShowSnackBar = onShowSnackBar
    )
}

@Composable
fun ChangelogListScreen(
    uiState: ChangelogUiState,
    changelogListCallback: ChangelogListCallback,
    onNavigateUp: () -> Unit,
    viewModel: ChangelogViewModel,
    onShowSnackBar: OnShowSnackBar
){

    HandleDownloadState(
        state = uiState.downloadState,
        onShowSnackBar = onShowSnackBar,
        onDispose = changelogListCallback.onResetMessageState
    )

    Scaffold(
        topBar = {
            ChangelogTopBar(
                uiState = uiState,
                changelogListCallback = changelogListCallback,
                onNavigateUp = onNavigateUp,
                viewModel = viewModel,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            LoadChangelogList(
                uiState = uiState,
                supplierChangelogListCallback = changelogListCallback
            )
        }
    }
}