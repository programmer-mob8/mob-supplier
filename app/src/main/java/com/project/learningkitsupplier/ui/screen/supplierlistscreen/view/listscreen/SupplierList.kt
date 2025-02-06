package com.project.learningkitsupplier.ui.screen.supplierlistscreen.view.listscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.components.DeleteSupplierAlert
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.view.SupplierListActionSheet
import com.project.libs.data.model.SupplierEntity
import com.tagsamurai.tscomponents.card.AdaptiveCardItem
import com.tagsamurai.tscomponents.chip.Chip
import com.tagsamurai.tscomponents.model.Severity
import com.tagsamurai.tscomponents.model.TypeChip
import com.tagsamurai.tscomponents.textfield.UserRecord
import com.tagsamurai.tscomponents.theme.LocalTheme
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.Spacer.widthBox
import com.tagsamurai.tscomponents.utils.Utils.toDateFormatter
import com.tagsamurai.tscomponents.utils.itemGap4
import com.tagsamurai.tscomponents.utils.popupBodyStyle
import com.tagsamurai.tscomponents.utils.popupBoldStyle

@Composable
fun SupplierList(
    item: SupplierEntity,
    supplierListCallback: SupplierListCallback,
    uiState: SupplierListUiState,
    navController: NavController
) {

    var showActionSheet by remember { mutableStateOf(false) }
    val isSelected = uiState.itemSelected.contains(item)
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }

    AdaptiveCardItem(
        onClick = {
            if (uiState.itemSelected.isNotEmpty()) supplierListCallback.onUpdateSelectedItem(
                item
            )
        },
        onLongClick = { supplierListCallback.onUpdateSelectedItem(item) },
        containerColor = if (isSelected) theme.popupBackgroundSelected else Color.Transparent,
        showMoreIcon = true,
        onClickAction = { showActionSheet = true }
    ) {
        Column {
            if (!item.status) {
                Chip(
                    label = "Inactive",
                    type = TypeChip.BULLET,
                    severity = Severity.DANGER
                )
            } else {
                Chip(
                    label = "Active",
                    type = TypeChip.BULLET,
                    severity = Severity.SUPPLY
                )
            }

            itemGap4.heightBox()
            Text(
                text = item.companyName,
                style = popupBoldStyle
            )

            itemGap4.heightBox()
            Text(
                text = item.state + ", " + item.country,
                style = popupBodyStyle
            )

            itemGap4.heightBox()

            val allSkus = item.item.flatMap { it.sku }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val skuToShow = allSkus.take(2)
                println("SKU to show: $skuToShow")

                skuToShow.forEach { sku ->
                    Chip(
                        label = sku,
                        type = TypeChip.PILL,
                        severity = Severity.DARK,
                    )
                    4.widthBox()
                }
                if (allSkus.size > 2) {
                    Text(
                        text = "+ ${allSkus.size - 2} more",
                        style = popupBodyStyle
                    )
                }
            }



            itemGap4.heightBox()
            Row {
                Text(
                    text = item.updatedAt.toDateFormatter(),
                    style = popupBodyStyle
                )
                Spacer(Modifier.weight(1f))

                UserRecord(
                    username = item.picName,
                    textColor = LocalTheme.current.primary
                )
            }
        }

        SupplierListActionSheet(
            onDismissRequest = { showActionSheet = it },
            isShowSheet = showActionSheet,
            item = item,
            uiState = uiState,
            onDelete = { showDeleteDialog = true },
            onUpdateStatus = { showStatusDialog = true },
            navController = navController,
            supplierListCallback = supplierListCallback,
            onConfirmDismiss = { showActionSheet = false}
        )

        DeleteSupplierAlert(
            onDismissRequest = { showDeleteDialog = it },
            supplier = listOf(item),
            showDialog = showDeleteDialog,
            onConfirm = {
                supplierListCallback.deleteSupplier(listOf(item.id))
                supplierListCallback.onClearSelectedItem()
                showDeleteDialog = false
            }
        )
    }
}