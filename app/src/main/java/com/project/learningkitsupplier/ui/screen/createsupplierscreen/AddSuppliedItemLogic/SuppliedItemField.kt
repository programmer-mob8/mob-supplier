package com.project.learningkitsupplier.ui.screen.createsupplierscreen.AddSuppliedItemLogic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierCallback
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormData
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.uistate.CreateSupplierUiState
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.button.Button
import com.tagsamurai.tscomponents.button.MultiSelector
import com.tagsamurai.tscomponents.button.SingleSelector
import com.tagsamurai.tscomponents.model.Severity
import com.tagsamurai.tscomponents.model.TypeButton
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Spacer.widthBox

@Composable
fun SuppliedItemBaseField(
    isRemovable: Boolean,
    onRemove: () -> Unit,
    item: CreateSupplierFormData.Item,
    uiState: CreateSupplierUiState,
    getCallback: CreateSupplierCallback
) {

    val itemName = item.itemName
    val sku = item.sku

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            SingleSelector(
                onValueChange = { result ->
                    getCallback.onUpdateListSuppliedItem(
                        item.copy(
                            itemName = result
                        )
                    )
                },
                items = uiState.formOption.itemName,
                placeHolder = "Select item name",
                value = itemName,
                title = "Item name",
                required = true,
                isError = uiState.formError.itemName != null,
                textError = uiState.formError.itemName
            )
        }

        10.widthBox()

        Column(
            modifier = Modifier.weight(1f)
        ) {

            MultiSelector(
                onValueChange = { result ->
                    getCallback.onUpdateListSuppliedItem(
                        item.copy(
                            sku = result
                        )
                    )
                },
                items = uiState.formOption.sku,
                value = sku,
                enabled = uiState.formData.items.any { it.itemName.isNotEmpty() },
                placeHolder = "Select SKU",
                title = "SKU",
                required = true,
                isError = uiState.formError.sku != null,
                textError = uiState.formError.sku,
                isUseChip = true,
            )
        }

        if (isRemovable) {
            10.widthBox()
            Button(
                onClick = onRemove,
                type = TypeButton.OUTLINED,
                severity = Severity.DANGER,
                content = {
                    Icon(painterResource(R.drawable.ic_subtract_line_24dp), contentDescription = null, tint = theme.danger)
                }
            )
        }
    }
}