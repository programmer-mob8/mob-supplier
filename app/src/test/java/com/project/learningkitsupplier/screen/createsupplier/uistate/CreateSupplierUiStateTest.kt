package com.project.learningkitsupplier.screen.createsupplier.uistate

import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormData
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormError
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormOption
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.uistate.CreateSupplierUiState
import org.junit.Test

class CreateSupplierUiStateTest {
    @Test
    fun `constructor should return correct object with default values`() {
        val expectedUiState = CreateSupplierUiState()

        val result = CreateSupplierUiState()

        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `constructor should return correct object with custom values`() {
        // Arrange
        val isLoadingOverlay = true
        val isLoadingFormOption = true
        val isStayOnForm = true
        val supplierId = "supplierId"
        val isEditForm = true
        val clearField: () -> Unit = {}
        val formData = CreateSupplierFormData(items = listOf(CreateSupplierFormData.Item(id = "2")))
        val formError = CreateSupplierFormError()
        val formOption = CreateSupplierFormOption()
        val submitState = true

        val expectedUiState = CreateSupplierUiState(
            isLoadingOverlay = isLoadingOverlay,
            isLoadingFormOption = isLoadingFormOption,
            isStayOnForm = isStayOnForm,
            supplierId = supplierId,
            isEditForm = isEditForm,
            clearField = clearField,
            formData = formData,
            formError = formError,
            formOption = formOption,
            submitState = submitState
        )

        // Act
        val result = CreateSupplierUiState(
            isLoadingOverlay = isLoadingOverlay,
            isLoadingFormOption = isLoadingFormOption,
            isStayOnForm = isStayOnForm,
            supplierId = supplierId,
            isEditForm = isEditForm,
            clearField = clearField,
            formData = formData,
            formError = formError,
            formOption = formOption,
            submitState = submitState
        )

        // Assert
        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `submitState should be null by default`() {
        // Act
        val uiState = CreateSupplierUiState()

        // Assert
        assertThat(uiState.submitState).isNull()
    }

    @Test
    fun `clearField should be a no-op by default`() {
        // Arrange
        val uiState = CreateSupplierUiState()

        // Act
        uiState.clearField()

        // Assert
        assertThat(uiState.clearField).isNotNull()
    }
}