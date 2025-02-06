package com.project.learningkitsupplier.ui.screen.supplierlistscreen.components

import androidx.compose.runtime.Composable
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.viewmodel.SupplierListViewModel
import com.tagsamurai.tscomponents.alertdialog.DownloadConfirmDialog
import com.tagsamurai.tscomponents.utils.Utils.getDownloadFilename

@Composable
fun DownloadSupplierDialogs(
    onDismissRequest: (Boolean) -> Unit,
    showDialog: Boolean,
    viewModel: SupplierListViewModel
) {
    val fileName = "Supplier-List".getDownloadFilename()

    if(showDialog) {
        DownloadConfirmDialog(
            filename = fileName,
            onDismissRequest = onDismissRequest,
            onConfirm = {
                onDismissRequest(false)
                viewModel.downloadList(fileName)
            },
        )
    }
}
