package com.project.learningkitsupplier.ui.screen.supplierlistscreen.components

import androidx.compose.runtime.Composable
import com.tagsamurai.tscomponents.alertdialog.DownloadConfirmDialog

@Composable
fun DownloadSupplierDialogs(
    onDismissRequest: (Boolean) -> Unit,
    showDialog: Boolean
) {
    if(showDialog) {
        DownloadConfirmDialog(
            filename = "Supplier-List.xlsx",
            onDismissRequest = onDismissRequest,
            onConfirm = {},
        )
    }
}
