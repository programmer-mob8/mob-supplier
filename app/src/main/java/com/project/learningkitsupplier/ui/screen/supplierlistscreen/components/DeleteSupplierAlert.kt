package com.project.learningkitsupplier.ui.screen.supplierlistscreen.components

import androidx.compose.runtime.Composable
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.libs.data.model.SupplierEntity
import com.tagsamurai.tscomponents.alertdialog.AlertDialog
import com.tagsamurai.tscomponents.model.Severity
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Utils.generateAnnotated

@Composable
fun DeleteSupplierAlert(
    onDismissRequest: (Boolean) -> Unit,
    supplier: List<SupplierEntity>,
    showDialog: Boolean,
    supplierListCallback: SupplierListCallback
) {
    val isSingle = supplier.size == 1
    val rawMessage = if(isSingle){
        "${supplier[0].companyName} will be deleted. Are you sure you want to delete it?"
    } else {
        "You have selected ${supplier.size} to be deleted. Are you sure you want to continue?"
    }
    val message = rawMessage.generateAnnotated()


    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            onButtonCancel = { onDismissRequest(false) },
            onButtonConfirm = {
                onDismissRequest(false)
                supplierListCallback.deleteSupplier(supplier.map { it.id })
            },
            icon = com.tagsamurai.tscomponents.R.drawable.ic_delete_bin_6_line_24dp,
            iconColor = theme.danger,
            title = "Delete Asset",
            textButtonCancel = "Cancel",
            textButtonConfirm = "Confirm",
            severity = Severity.DANGER,
            content = message
        )
    }
}