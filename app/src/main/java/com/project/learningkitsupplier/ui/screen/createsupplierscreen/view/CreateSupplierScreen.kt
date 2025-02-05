package com.project.learningkitsupplier.ui.screen.createsupplierscreen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierCallback
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormData
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.uistate.CreateSupplierUiState
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.viewmodel.CreateSupplierViewModel
import com.tagsamurai.tscomponents.button.SingleActionButton
import com.tagsamurai.tscomponents.checkbox.CustomCheckbox
import com.tagsamurai.tscomponents.handlestate.HandleState
import com.tagsamurai.tscomponents.scaffold.Scaffold
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.bodyStyle

@Composable
fun CreateSupplierScreen(
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: CreateSupplierViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        uiState.formData == CreateSupplierFormData()
        viewModel.init()
    }

    CreateSupplierScreen(
        onNavigateUp = onNavigateUp,
        onShowSnackBar = onShowSnackBar,
        uiState = uiState,
        callback = viewModel.getCallback()
    )
}


@Composable
fun CreateSupplierScreen(
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
    uiState: CreateSupplierUiState,
    callback: CreateSupplierCallback
) {


    val successMessage: String
    val errorMessage: String
    val submitText: String
    val titleText: String

    if (uiState.isEditForm) {
        successMessage = "Success, supplier has been edited."
        errorMessage =
            "Error failed to edit supplier. Please check your internet connection and try again."
        submitText = "Save"
        titleText = "Edit Supplier"
    } else {
        successMessage = "Success, supplier has been created."
        errorMessage =
            "Error failed to create supplier. Please check your internet connection and try again."
        submitText = "Submit"
        titleText = "Add Supplier"
    }


    HandleState(
        state = uiState.submitState,
        onShowSnackBar = onShowSnackBar,
        successMsg = successMessage,
        errorMsg = errorMessage,
        onSuccess = {
            if (!uiState.isStayOnForm) {
                onNavigateUp()
            }
        },
        onDispose = callback.onResetMessageState
    )


    Scaffold(
        topBar = {
            CreateSupplierTopBar(
                onNavigateUp, titleText
            )
        },
    ) {
        Column {

            10.heightBox()

            Box(
                modifier = Modifier.weight(1f)
            ) {
                CreateSupplierFormData(
                    uiState = uiState,
                    onUpdateForm = { formData ->
                        callback.onUpdateFormData(formData)
                    },
                    supplierCallback = callback,
                )
            }

            10.heightBox()

            SingleActionButton(
                onButtonConfirm = callback.onSubmitForm,
                label = submitText,
                contentHeader = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(
                            role = Role.Checkbox,
                            onClick = callback.onUpdateStayOnForm
                        )
                    ) {
                        Text(
                            text = "Stay on this form after submitting?",
                            style = bodyStyle,
                            color = theme.bodyText,
                            modifier = Modifier.weight(1f)
                        )

                        CustomCheckbox(
                            checked = uiState.isStayOnForm,
                            onCheckedChange = { callback.onUpdateStayOnForm() }
                        )
                    }
                    10.heightBox()
                }
            )
        }
    }
}