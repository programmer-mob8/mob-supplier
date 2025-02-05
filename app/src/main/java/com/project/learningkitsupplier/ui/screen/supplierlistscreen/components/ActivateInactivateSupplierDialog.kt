package com.project.learningkitsupplier.ui.screen.supplierlistscreen.components

import android.util.Log
import androidx.compose.runtime.Composable
import com.project.libs.data.model.SupplierEntity
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.alertdialog.AlertDialog
import com.tagsamurai.tscomponents.model.Severity
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Utils.generateAnnotated

@Composable
fun ActivateInactiveSupply(
    onDismissRequest: (Boolean) -> Unit,
    supplier: List<SupplierEntity>,
    showDialog: Boolean,
    isActive: Boolean,
    onConfirm: () -> Unit
) {
    val isSingle = supplier.size == 1

    Log.d("ActivateInactiveSupply", "{$isActive}")

    val rawMessage =
        if (isSingle) {
            if (isActive) {
                "${supplier[0].companyName} will be inactivated. Are you sure you want to inactive it?"
            } else {
                "${supplier[0].companyName} will be activated. Are you sure you want to activate it?"
            }
        } else {
            if (isActive) {
                "you have selected ${supplier.size} supplier(s) to be inactivated. Are you sure you want to inactive it?"
            } else {
                "you have selected ${supplier.size} supplier(s) to be activated. Are you sure you want to active it?"
            }
        }

    val message = rawMessage.generateAnnotated()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            onButtonCancel = { onDismissRequest(false) },
            onButtonConfirm = {
                onDismissRequest(false)
                onConfirm()
            },
            icon = if (isActive) R.drawable.ic_delete_bin_6_line_24dp else R.drawable.ic_checkbox_circle_line_24dp,
            iconColor = if (isActive) theme.danger else theme.primary,
            title = if (isActive) "Inactive Supplier" else "Activate Supplier",
            severity = if (isActive) Severity.DANGER else Severity.SUPPLY,
            textButtonCancel = "Cancel",
            textButtonConfirm = if (isActive) "Inactivate" else "Activate",
            content = message
        )
    }
}