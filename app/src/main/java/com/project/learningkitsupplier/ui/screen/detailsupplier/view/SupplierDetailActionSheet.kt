package com.project.learningkitsupplier.ui.screen.detailsupplier.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.project.learningkitsupplier.navigation.NavigationRoute
import com.project.libs.data.model.SupplierDetailEntity
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.bottomsheet.BottomSheet
import com.tagsamurai.tscomponents.button.ActionButton
import com.tagsamurai.tscomponents.theme.theme

@Composable
fun SupplierDetailActionSheet(
    navController: NavController,
    item: SupplierDetailEntity,
    onDismissRequest: (Boolean) -> Unit,
    showSheet: Boolean,
    onDelete: () -> Unit
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        isShowSheet = showSheet,
        content = {
            ActionButton(
                onClickAction = { navController.navigate(NavigationRoute.EditSupplierScreen.navigate(supplierId = item.id)) },
                title = "Edit",
                icon = R.drawable.ic_edit_2_line_24dp,
                iconTint = theme.success
            )

            ActionButton(
                onClickAction = onDelete,
                title = "Delete",
                icon = R.drawable.ic_delete_bin_6_line_24dp,
                iconTint = theme.danger

            )
        }
    )
}