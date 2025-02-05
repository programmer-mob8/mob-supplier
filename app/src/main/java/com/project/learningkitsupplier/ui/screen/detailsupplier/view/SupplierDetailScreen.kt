package com.project.learningkitsupplier.ui.screen.detailsupplier.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailCallback
import com.project.learningkitsupplier.ui.screen.detailsupplier.uistate.SupplierDetailUiState
import com.project.learningkitsupplier.ui.screen.detailsupplier.viewmodel.SupplierDetailViewModel
import com.tagsamurai.tscomponents.handlestate.HandleState
import com.tagsamurai.tscomponents.scaffold.Scaffold
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar

@Composable
fun SupplierDetailScreen(
    navController: NavController,
    onShowSnackBar: OnShowSnackBar,
    onNavigateUp: () -> Unit,
) {

    val supplierDetailViewModel: SupplierDetailViewModel = hiltViewModel()
    val uiState = supplierDetailViewModel.uiState.collectAsStateWithLifecycle().value
    val callback = supplierDetailViewModel.getCallback()

    LaunchedEffect(Unit) {
        supplierDetailViewModel.init()
    }

    SupplierDetailScreen(
        navController = navController,
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        supplierDetailCallback = callback,
        onShowSnackBar = onShowSnackBar
    )

}

@Composable
fun SupplierDetailScreen(
    navController: NavController,
    onNavigateUp: () -> Unit,
    uiState: SupplierDetailUiState,
    supplierDetailCallback: SupplierDetailCallback,
    onShowSnackBar: OnShowSnackBar,
) {

    val successMessage = "Success, supplier has been deleted"
    val errorMessage = "Error, failed to remove supplier. Please check your internet connection and try again"

    HandleState(
        state = uiState.deleteState,
        onShowSnackBar = onShowSnackBar,
        onSuccess = onNavigateUp,
        successMsg = successMessage,
        errorMsg = errorMessage,
        onDispose = supplierDetailCallback.resetMessageState
    )

    Scaffold(
        topBar = {
            SupplierDetailTopBar(
                navController = navController,
                uiState = uiState,
                onNavigateUp = onNavigateUp,
                supplierDetailCallback = supplierDetailCallback
            )
        },
        isShowLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            SupplierDetailData(
                uiState = uiState
            )
        }
    }
}