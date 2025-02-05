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

@Composable
fun ChangelogListScreen(
    onNavigateUp: () -> Unit
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
        onNavigateUp = onNavigateUp
    )
}

@Composable
fun ChangelogListScreen(
    uiState: ChangelogUiState,
    changelogListCallback: ChangelogListCallback,
    onNavigateUp: () -> Unit
){

    Scaffold(
        topBar = {
            ChangelogTopBar(
                uiState = uiState,
                changelogListCallback = changelogListCallback,
                onNavigateUp = onNavigateUp
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