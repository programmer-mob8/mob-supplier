package com.project.learningkitsupplier.ui.screen.changelog.view.changeloglist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.libs.data.model.ChangelogEntity
import com.tagsamurai.tscomponents.R.drawable.ic_arrow_right_line_24dp
import com.tagsamurai.tscomponents.card.AdaptiveCardItem
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
fun ChangelogList(
    item: ChangelogEntity,
) {
    val severity =  when (item.action) {
        "Create" -> Severity.SUCCESS
        "Edit" -> Severity.WARNING
        else -> Severity.DANGER
    }

    AdaptiveCardItem {
        Column {
            Chip(
                label = item.action,
                type = TypeChip.BULLET,
                severity = severity
            )

            itemGap4.heightBox()

            Text(
                text = item.objectName,
                style = popupBoldStyle,
            )

            itemGap4.heightBox()

            Text(
                text = item.field,
                style = popupBodyStyle
            )

            if (item.action != "Create" && item.action != "Delete") {
                Row {
                    Text(
                        text = item.oldValue,
                        style = popupBodyStyle
                    )

                    5.widthBox()

                    Icon(
                        painterResource(ic_arrow_right_line_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )

                    5.widthBox()

                    Text(
                        text = item.newValue,
                        style = popupBodyStyle
                    )
                }
            }

            Row {
                Text(
                    text = item.timeStamp.toDateFormatter(),
                    style = popupBodyStyle
                )

                Spacer(Modifier.weight(1f))

                UserRecord(
                    username = item.modifiedBy,
                    textColor = LocalTheme.current.primary
                )
            }
        }
    }
}