package com.project.learningkitsupplier.ui.screen.detailsupplier.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.project.learningkitsupplier.ui.screen.detailsupplier.uistate.SupplierDetailUiState
import com.tagsamurai.tscomponents.chip.Chip
import com.tagsamurai.tscomponents.model.Severity
import com.tagsamurai.tscomponents.model.TypeChip
import com.tagsamurai.tscomponents.textfield.UserRecord
import com.tagsamurai.tscomponents.theme.LocalTheme
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.Spacer.widthBox
import com.tagsamurai.tscomponents.utils.Utils.toDateFormatter
import com.tagsamurai.tscomponents.utils.itemGap4
import com.tagsamurai.tscomponents.utils.popupBodyStyle
import com.tagsamurai.tscomponents.utils.popupBoldStyle


@Composable
fun SupplierDetailData(
    uiState: SupplierDetailUiState
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    Log.d("TAG", "SupplierDetailData: ${uiState.supplierDetailEntity}")
    Column {
        if (!uiState.supplierDetailEntity.status) {
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

        Text(
            text = uiState.supplierDetailEntity.companyName,
            style = popupBoldStyle
        )

        itemGap4.heightBox()

        Row {
            Text(
                text = "Modified by: ",
                style = popupBodyStyle
            )

            4.widthBox()

            UserRecord(
                username = uiState.supplierDetailEntity.picName,
                textColor = LocalTheme.current.primary
            )
        }

        itemGap4.heightBox()

        Row {

            Text(
                text = uiState.supplierDetailEntity.createdAt.toDateFormatter(),
                style = popupBodyStyle
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.clickable {
                    showBottomSheet = true
                },
                text = "Detail",
                style = popupBodyStyle,
                color = Color(0xFF047857)
            )
        }
    }

    SupplierDetailBottomSheet(
        onDismissRequest =  { showBottomSheet =  false },
        item = uiState.supplierDetailEntity,
        showSheet = showBottomSheet
    )
}