package com.project.learningkitsupplier.ui.screen.changelog.view.changeloglist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tagsamurai.tscomponents.card.AdaptiveCardItem
import com.tagsamurai.tscomponents.shimmerEffect.ShimmerEffect
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.Spacer.widthBox
import com.tagsamurai.tscomponents.utils.itemGap4

@Composable
fun LoadingUiChangelog() {
    AdaptiveCardItem {
        Column {

            ShimmerEffect(width = 60.dp)
            itemGap4.heightBox()

            ShimmerEffect(width = 100.dp)
            itemGap4.heightBox()
            ShimmerEffect(width = 120.dp)
            itemGap4.heightBox()
            Row {
                ShimmerEffect(70.dp)
                4.widthBox()
                ShimmerEffect(70.dp)
            }
            itemGap4.heightBox()

            Row {
                ShimmerEffect(width = 120.dp)
                Spacer(Modifier.weight(1f))
                ShimmerEffect(width = 120.dp)
            }
        }
    }
}

@Preview
@Composable
fun Preview3(){
    LoadingUiChangelog()
}