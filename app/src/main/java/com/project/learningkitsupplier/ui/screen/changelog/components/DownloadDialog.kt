package com.project.learningkitsupplier.ui.screen.changelog.components

import androidx.compose.runtime.Composable
import com.tagsamurai.tscomponents.alertdialog.DownloadConfirmDialog

@Composable
fun DownloadDialog(
    onDismissRequest: (Boolean) -> Unit,
    showDialog: Boolean
) {

    if(showDialog) {
        DownloadConfirmDialog(
            filename = "Changelog-Supplier.xlsx",
            onDismissRequest = onDismissRequest,
            onConfirm = {},
        )
    }
}