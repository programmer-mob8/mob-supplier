package com.project.learningkitsupplier.ui.screen.supplierlistscreen.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.learningkitsupplier.navigation.NavigationRoute
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.view.listscreen.LoadSupplierList
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.view.listscreen.SupplierActivity
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.viewmodel.SupplierListViewModel
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.button.CustomFloatingIconButton
import com.tagsamurai.tscomponents.handlestate.HandleState
import com.tagsamurai.tscomponents.scaffold.Scaffold
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar
import com.tagsamurai.tscomponents.theme.theme


@Composable
fun SupplierListScreen(
    navController: NavController,
    onShowSnackBar: OnShowSnackBar
) {
    val supplierListViewModel: SupplierListViewModel = hiltViewModel()
    val uiState = supplierListViewModel.uiState.collectAsStateWithLifecycle()
    val supplierListCallback = supplierListViewModel.getCallback()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        supplierListViewModel.init()
    }

    SupplierListScreen(
        uiState = uiState.value,
        supplierListCallback = supplierListCallback,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = {index -> selectedTabIndex = index},
        navController = navController,
        onShowSnackBar = onShowSnackBar
    )

}

@Composable
fun SupplierListScreen(
    uiState: SupplierListUiState,
    supplierListCallback: SupplierListCallback,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onShowSnackBar: OnShowSnackBar,
    navController: NavController
){

    val statusSuccessMessage: String
    val statusErrorMessage: String

    val successMessage = "Success, supplier has been delete"
    val errorMessage = "Error, failed to delete supplier. Please check your internet connection and try again"

    if (uiState.isActive) {
        statusSuccessMessage = "Success, supplier has been activated"
        statusErrorMessage = "Error, failed to activate supplier. Please check your internet connection and try again"
    } else {
        statusSuccessMessage = "Success, supplier has been inactivated"
        statusErrorMessage = "Error, failed to inactivate supplier. Please check your internet connection and try again"
    }


    HandleState(
        state = uiState.deleteState,
        onShowSnackBar = onShowSnackBar,
        successMsg = successMessage,
        errorMsg = errorMessage,
        onDispose = supplierListCallback.onResetMessageState
    )

    HandleState(
        state = uiState.statusState,
        onShowSnackBar = onShowSnackBar,
        successMsg = statusSuccessMessage,
        errorMsg = statusErrorMessage,
        onDispose = supplierListCallback.onResetMessageState
    )

    Scaffold(
        topBar = {
            SupplierListTopBar(uiState, supplierListCallback, selectedTabIndex, onTabSelected, navController)
        },

        floatingActionButton = {
            CustomFloatingIconButton(
                icon = R.drawable.ic_add_fill_24dp,
                containerColor = theme.warning500,
                iconColor = theme.warning500,
                onClick = { navController.navigate(NavigationRoute.CreateSupplierScreen.route) }
            )
        },
        isShowLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column {
            when(selectedTabIndex) {
                0 -> LoadSupplierList(
                    uiState = uiState,
                    supplierListCallback = supplierListCallback,
                    navController = navController
                )
                1 -> SupplierActivity()
            }
        }
    }
}