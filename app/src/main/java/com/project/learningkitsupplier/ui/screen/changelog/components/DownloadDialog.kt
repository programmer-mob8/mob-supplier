package com.project.learningkitsupplier.ui.screen.changelog.components

import androidx.compose.runtime.Composable
import com.project.learningkitsupplier.ui.screen.changelog.viewmodel.ChangelogViewModel
import com.tagsamurai.tscomponents.alertdialog.DownloadConfirmDialog
import com.tagsamurai.tscomponents.utils.Utils.getDownloadFilename

@Composable
fun DownloadDialog(
    onDismissRequest: (Boolean) -> Unit,
    showDialog: Boolean,
    viewModel: ChangelogViewModel
) {

    val fileName = "Changelog-Supplier".getDownloadFilename()

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