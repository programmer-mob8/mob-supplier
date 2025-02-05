package com.project.learningkitsupplier.ui.screen.changelog.view.changeloglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.learningkitsupplier.module.changelog.ChangelogListCallback
import com.project.learningkitsupplier.ui.screen.changelog.uistate.ChangelogUiState
import com.tagsamurai.tscomponents.pullrefresh.PullRefresh
import com.tagsamurai.tscomponents.screen.EmptyState
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.paddingList

@Composable
fun LoadChangelogList(
    uiState: ChangelogUiState,
    supplierChangelogListCallback: ChangelogListCallback
) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier.padding(paddingList)
            ) {
                repeat(5) {
                    LoadingUiChangelog()
                    4.heightBox()
                }
            }
        }
        else -> {
            if (uiState.changelog.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    EmptyState()
                }
            } else {
                PullRefresh(
                    onRefresh = supplierChangelogListCallback.onRefresh
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingList,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(uiState.changelog.size) { index ->
                            ChangelogList(
                                item = uiState.changelog[index],
                            )
                        }
                    }
                }
            }
        }
    }
}