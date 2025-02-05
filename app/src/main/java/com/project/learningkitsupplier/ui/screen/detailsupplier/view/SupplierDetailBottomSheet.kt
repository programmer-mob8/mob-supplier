package com.project.learningkitsupplier.ui.screen.detailsupplier.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.libs.data.model.SupplierDetailEntity
import com.tagsamurai.tscomponents.bottomsheet.BottomSheet
import com.tagsamurai.tscomponents.chip.Chip
import com.tagsamurai.tscomponents.model.Severity
import com.tagsamurai.tscomponents.model.TypeChip
import com.tagsamurai.tscomponents.textfield.UserRecord
import com.tagsamurai.tscomponents.theme.LocalTheme
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.Spacer.widthBox
import com.tagsamurai.tscomponents.utils.itemGap4
import com.tagsamurai.tscomponents.utils.itemGap8
import com.tagsamurai.tscomponents.utils.popupBodyStyle
import com.tagsamurai.tscomponents.utils.popupBoldStyle

@Composable
fun SupplierDetailBottomSheet(
    onDismissRequest: (Boolean) -> Unit,
    item: SupplierDetailEntity,
    showSheet: Boolean
) {

    var showSuppliedBottomSheet by remember { mutableStateOf(false) }

    BottomSheet(
        onDismissRequest = onDismissRequest,
        isShowSheet = showSheet,
        content = {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                if (item.status) {
                    Chip(
                        label = "Active",
                        type = TypeChip.BULLET,
                        severity = Severity.SUPPLY
                    )
                } else {
                    Chip(
                        label = "Inactive",
                        type = TypeChip.BULLET,
                        severity = Severity.DANGER
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Supplied Item",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        text = "View Supplied Item",
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                            showSuppliedBottomSheet = true
                        },
                        style = popupBodyStyle,
                        color = Color(0xFF047857)
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "PIC",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    UserRecord(
                        modifier = Modifier.weight(1f),
                        username = item.picName,
                        textColor = LocalTheme.current.primary
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "PIC Phone",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.picPhoneNumber,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "PIC Email",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.picEmail,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Country",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.country,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "State",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.state,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "City",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.city,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "ZIP Code",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.zipCode,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Company Phone",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.companyPhoneNumber,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }

                itemGap8.heightBox()

                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Company Address",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    Text(
                        text = ":",
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )

                    4.widthBox()

                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.companyLocation,
                        style = popupBodyStyle,
                        color = theme.popupContent
                    )
                }
            }
        }
    )

    BottomSheet(
        onDismissRequest = { showSuppliedBottomSheet = false },
        isShowSheet = showSuppliedBottomSheet,
        content = {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row {
                    Icon(
                        painterResource(com.tagsamurai.tscomponents.R.drawable.ic_information_line_24dp),
                        tint = theme.success,
                        contentDescription = null
                    )

                    8.widthBox()

                    Text(
                        text = "Supplied Item",
                        style = popupBoldStyle,
                        color = theme.popupContent
                    )
                }

                ItemList(
                    item = item
                )
            }
        }
    )
}

@Composable
fun ItemList(
    item: SupplierDetailEntity
) {

    item.item.forEach { currentItem ->
        val itemName = currentItem.itemName
        val skuList = currentItem.sku

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Row {
                Text(
                    text = "Item : ",
                    style = popupBodyStyle,
                    color = theme.popupContent
                )

                Text(
                    text = itemName,
                    style = popupBodyStyle,
                    color = theme.popupContent
                )

            }

            itemGap4.heightBox()

            Row {

                Text(
                    text = "SKU :  ",
                    style = popupBodyStyle,
                    color = theme.popupContent
                )

                skuList.forEach { sku ->
                    Chip(
                        label = sku,
                        type = TypeChip.PILL,
                        severity = Severity.DARK,
                    )

                    4.widthBox()
                }
            }
        }
    }
}