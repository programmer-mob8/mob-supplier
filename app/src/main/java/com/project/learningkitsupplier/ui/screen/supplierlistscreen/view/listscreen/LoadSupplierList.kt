package com.project.learningkitsupplier.ui.screen.supplierlistscreen.view.listscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.tagsamurai.tscomponents.pullrefresh.PullRefresh
import com.tagsamurai.tscomponents.screen.EmptyState
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.paddingList

@Composable
fun LoadSupplierList(
    uiState: SupplierListUiState,
    supplierListCallback: SupplierListCallback,
    navController: NavController
) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier.padding(paddingList)
            ) {
                repeat(5) {
                    LoadingSupplierList()
                    4.heightBox()
                }
            }
        }
        else -> {
            if (uiState.supplier.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    EmptyState()
                }
            } else {
                PullRefresh(
                    onRefresh = supplierListCallback.onRefresh
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingList,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(uiState.supplier.size) { index ->
                            SupplierList(
                                item = uiState.supplier[index],
                                supplierListCallback = supplierListCallback,
                                uiState = uiState,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}