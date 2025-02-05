package com.project.learningkitsupplier.ui.screen.createsupplierscreen.view

import androidx.compose.runtime.Composable
import com.tagsamurai.tscomponents.topappbar.TopAppBar

@Composable
fun CreateSupplierTopBar(
    onDismissRequest: () -> Unit,
    title: String
) {
    TopAppBar(
        menu = emptyList(),
        canNavigateBack = true,
        onMenuAction = {},
        navigateUp = onDismissRequest,
        title = title
    )
}
