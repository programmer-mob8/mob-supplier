package com.project.learningkitsupplier.module.createsupplier

data class CreateSupplierCallback(
    val onResetMessageState: () -> Unit = {},
    val onSubmitForm: () -> Unit = {},
    val onUpdateStayOnForm: () -> Unit = {},
    val onUpdateFormData: (CreateSupplierFormData) -> Unit = {},
    val onUpdateListSuppliedItem: (CreateSupplierFormData.Item) -> Unit,
    val addSupplierButton: () -> Unit,
    val removeSupplierButton: (CreateSupplierFormData.Item) -> Unit
)