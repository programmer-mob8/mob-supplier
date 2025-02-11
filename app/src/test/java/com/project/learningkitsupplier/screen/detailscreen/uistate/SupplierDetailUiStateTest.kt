package com.project.learningkitsupplier.screen.detailscreen.uistate

import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailFilterData
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailFilterOption
import com.project.learningkitsupplier.ui.screen.detailsupplier.uistate.SupplierDetailUiState
import com.project.libs.data.model.SupplierDetailEntity
import org.junit.Test

class SupplierDetailUiStateTest {

    @Test
    fun `constructor should return correct object with default values`() {
        // Arrange
        val expectedUiState = SupplierDetailUiState()

        // Act
        val result = SupplierDetailUiState()

        // Assert
        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `constructor should return correct object with custom values`() {
        // Arrange
        val isLoadingGroup = true
        val showBottomSheet = true
        val isLoadingOverlay = true
        val supplierId = "123"
        val supplierDetailEntity = SupplierDetailEntity()
        val deleteState = true
        val filterOption = SupplierDetailFilterOption()
        val filterData = SupplierDetailFilterData()

        val expectedUiState = SupplierDetailUiState(
            isLoadingGroup = isLoadingGroup,
            showBottomSheet = showBottomSheet,
            isLoadingOverlay = isLoadingOverlay,
            supplierId = supplierId,
            supplierDetailEntity = supplierDetailEntity,
            deleteState = deleteState,
            filterOption = filterOption,
            filterData = filterData
        )

        // Act
        val result = SupplierDetailUiState(
            isLoadingGroup = isLoadingGroup,
            showBottomSheet = showBottomSheet,
            isLoadingOverlay = isLoadingOverlay,
            supplierId = supplierId,
            supplierDetailEntity = supplierDetailEntity,
            deleteState = deleteState,
            filterOption = filterOption,
            filterData = filterData
        )

        // Assert
        assertThat(result).isEqualTo(expectedUiState)
    }
}