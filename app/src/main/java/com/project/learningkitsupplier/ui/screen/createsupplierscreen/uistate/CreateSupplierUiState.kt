package com.project.learningkitsupplier.ui.screen.createsupplierscreen.uistate

import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormData
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormError
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormOption

data class CreateSupplierUiState(
    val isLoadingOverlay: Boolean = false,
    val isLoadingFormOption: Boolean = false,
    val isStayOnForm: Boolean = false,
    val supplierId: String = "",
    val isEditForm: Boolean = false,
    val formData: CreateSupplierFormData = CreateSupplierFormData(items = listOf(
        CreateSupplierFormData.Item(id = "1")
    )),
    val formError: CreateSupplierFormError = CreateSupplierFormError(),
    val formOption: CreateSupplierFormOption = CreateSupplierFormOption(),
    val submitState: Boolean? = null
)