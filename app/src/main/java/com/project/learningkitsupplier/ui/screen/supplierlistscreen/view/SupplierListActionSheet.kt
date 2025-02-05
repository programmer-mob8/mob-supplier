package com.project.learningkitsupplier.ui.screen.supplierlistscreen.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.learningkitsupplier.navigation.NavigationRoute
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.components.ActivateInactiveSupply
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.project.libs.data.model.SupplierEntity
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.bottomsheet.BottomSheet
import com.tagsamurai.tscomponents.button.ActionButton
import com.tagsamurai.tscomponents.switch.CustomSwitch
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.popupBoldStyle


@Composable
fun SupplierListActionSheet(
    onDismissRequest: (Boolean) -> Unit,
    isShowSheet: Boolean,
    item: SupplierEntity,
    uiState: SupplierListUiState,
    onUpdateStatus: (Boolean) -> Unit,
    navController: NavController,
    onDelete: () -> Unit,
    supplierListCallback: SupplierListCallback,
    onConfirmDismiss: () -> Unit
) {
    var newValue by remember { mutableStateOf(item.status) }
    var showDialog by remember { mutableStateOf(false) }

    BottomSheet(
        onDismissRequest = onDismissRequest,
        isShowSheet = isShowSheet,
        content = {
            Column {

                if (uiState.itemSelected.isEmpty()) {
                    Row(
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 24.dp, bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.companyName,
                            color = theme.bodyText,
                            fontSize = 16.sp,
                            style = popupBoldStyle
                        )

                        Spacer(Modifier.weight(1f))

                        CustomSwitch(
                            value = item.status,
                            onValueChange = {
                                newValue = it
                                showDialog = true
                            }
                        )
                    }

                    ActionButton(
                        onClickAction = { navController.navigate(NavigationRoute.EditSupplierScreen.navigate(supplierId = item.id)) },
                        title = "Edit",
                        icon = R.drawable.ic_edit_2_line_24dp,
                        iconTint = theme.success
                    )

                    ActionButton(
                        onClickAction = { navController.navigate(NavigationRoute.DetailSupplierScreen.navigateDetail(supplierId = item.id)) },
                        title = "Detail",
                        icon = R.drawable.ic_file_info_line_24dp,
                        iconTint = theme.success,
                    )

                    ActionButton(
                        onClickAction = onDelete,
                        title = "Delete",
                        icon = R.drawable.ic_delete_bin_6_line_24dp,
                        iconTint = theme.danger,

                        )
                } else {
                    ActionButton(
                        onClickAction = {onUpdateStatus(false)},
                        title = "Activate",
                        icon = R.drawable.ic_check_line_24dp,
                        iconTint = theme.success
                    )

                    ActionButton(
                        onClickAction = {onUpdateStatus(true)},
                        title = "Inactivate",
                        icon = R.drawable.ic_close_line_24dp,
                        iconTint = theme.success
                    )

                    ActionButton(
                        onClickAction = onDelete,
                        title = "Delete",
                        textColor = theme.danger,
                        icon = R.drawable.ic_delete_bin_6_line_24dp,
                        iconTint = theme.danger
                    )
                }
            }
        }
    )

    val selectedSuppliers = if (uiState.itemSelected.isEmpty()) listOf(item) else uiState.itemSelected

    ActivateInactiveSupply(
        onDismissRequest = { state -> showDialog = state },
        supplier = selectedSuppliers,
        showDialog = showDialog,
        onConfirm = {
            supplierListCallback.onStatusUpdate(listOf(item.id), newValue)
            onConfirmDismiss()
        },
        isActive = !newValue
    )
}