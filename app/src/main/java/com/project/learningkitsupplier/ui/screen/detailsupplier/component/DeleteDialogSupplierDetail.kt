package com.project.learningkitsupplier.ui.screen.detailsupplier.component

import androidx.compose.runtime.Composable
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailCallback
import com.project.learningkitsupplier.ui.screen.detailsupplier.uistate.SupplierDetailUiState
import com.project.libs.data.model.SupplierDetailEntity
import com.tagsamurai.tscomponents.alertdialog.AlertDialog
import com.tagsamurai.tscomponents.model.Severity
import com.tagsamurai.tscomponents.theme.theme

@Composable
fun DeleteDialogSupplierDetail(
    item: SupplierDetailEntity,
    onDismissRequest: (Boolean) -> Unit,
    showDialog: Boolean,
    uiState: SupplierDetailUiState,
    supplierDetailCallback: SupplierDetailCallback
) {
    if(showDialog){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            onButtonCancel = { onDismissRequest(false) },
            onButtonConfirm = {
                onDismissRequest(false)
                supplierDetailCallback.deleteSupplier(listOf(uiState.supplierId))
            },
            icon = com.tagsamurai.tscomponents.R.drawable.ic_delete_bin_6_line_24dp,
            iconColor = theme.danger,
            title = "Delete Asset",
            textButtonCancel = "Cancel",
            textButtonConfirm = "Confirm",
            severity = Severity.DANGER,
            content = "${item.companyName} will be deleted. Are you sure you want to continue"
        )
    }
}