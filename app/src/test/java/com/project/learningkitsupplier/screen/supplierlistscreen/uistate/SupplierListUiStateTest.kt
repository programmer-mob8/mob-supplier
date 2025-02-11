package com.project.learningkitsupplier.screen.supplierlistscreen.uistate

import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterData
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterOption
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.project.libs.data.model.SupplierEntity
import com.tagsamurai.tscomponents.utils.Utils
import org.junit.Test

class SupplierListUiStateTest {

    @Test
    fun `constructor should return correct object with default values`() {
        // Arrange
        val expectedUiState = SupplierListUiState()

        // Act
        val result = SupplierListUiState()

        // Assert
        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `constructor should return correct object with custom values`() {
        // Arrange
        val isLoading = true
        val isLoadingOverlay = true
        val isLoadingGroup = true
        val isAllSelected = true
        val showSearch = true
        val showActionSheet = true
        val showDeleteDialog = true
        val showDownloadDialog = true
        val showUpdateStatus = true
        val showFilter = true
        val itemSelected = listOf(SupplierEntity())
        val searchQuery = "test"
        val filterOption = SupplierListFilterOption()
        val filterData = SupplierListFilterData(
            status = listOf(false),
            companyName = listOf("company1"),
            country = listOf("country1"),
            lastModified = listOf(),
            itemName = listOf("item1"),
            modifiedBy = listOf("user1")
        )
        val supplierDefault = listOf(SupplierEntity())
        val supplier = listOf(SupplierEntity())
        val deleteState = true
        val downloadState = true
        val statusState = true
        val isActive = true

        val expectedUiState = SupplierListUiState(
            isLoading = isLoading,
            isLoadingOverlay = isLoadingOverlay,
            isLoadingGroup = isLoadingGroup,
            isAllSelected = isAllSelected,
            showSearch = showSearch,
            showActionSheet = showActionSheet,
            showDeleteDialog = showDeleteDialog,
            showDownloadDialog = showDownloadDialog,
            showUpdateStatus = showUpdateStatus,
            showFilter = showFilter,
            itemSelected = itemSelected,
            searchQuery = searchQuery,
            filterOption = filterOption,
            filterData = filterData,
            supplierDefault = supplierDefault,
            supplier = supplier,
            deleteState = deleteState,
            downloadState = downloadState,
            statusState = statusState,
            isActive = isActive
        )

        // Act
        val result = SupplierListUiState(
            isLoading = isLoading,
            isLoadingOverlay = isLoadingOverlay,
            isLoadingGroup = isLoadingGroup,
            isAllSelected = isAllSelected,
            showSearch = showSearch,
            showActionSheet = showActionSheet,
            showDeleteDialog = showDeleteDialog,
            showDownloadDialog = showDownloadDialog,
            showUpdateStatus = showUpdateStatus,
            showFilter = showFilter,
            itemSelected = itemSelected,
            searchQuery = searchQuery,
            filterOption = filterOption,
            filterData = filterData,
            supplierDefault = supplierDefault,
            supplier = supplier,
            deleteState = deleteState,
            downloadState = downloadState,
            statusState = statusState,
            isActive = isActive
        )

        // Assert
        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `queryParams should return correct value when all fields are empty`() {
        // Arrange
        val uiState = SupplierListUiState()

        // Act
        val queryParams = uiState.queryParams

        // Assert
        assertThat(queryParams.search).isNull()
        assertThat(queryParams.status).isNull()
        assertThat(queryParams.supplier).isNull()
        assertThat(queryParams.city).isNull()
        assertThat(queryParams.date).isNull()
        assertThat(queryParams.itemName).isNull()
        assertThat(queryParams.modifiedBy).isNull()
    }

    @Test
    fun `queryParams should return correct value when all fields are filled`() {
        // Arrange
        val filterData = SupplierListFilterData(
            status = listOf(true),
            companyName = listOf("company1"),
            country = listOf("country1"),
            lastModified = listOf(),
            itemName = listOf("item1"),
            modifiedBy = listOf("user1")
        )
        val searchQuery = "test"
        val uiState = SupplierListUiState(
            searchQuery = searchQuery,
            filterData = filterData
        )

        // Act
        val queryParams = uiState.queryParams

        // Assert
        assertThat(queryParams.search).isEqualTo(searchQuery)
        assertThat(queryParams.status).isEqualTo(Utils.toJsonIfNotEmpty(filterData.status))
        assertThat(queryParams.supplier).isEqualTo(Utils.toJsonIfNotEmpty(filterData.companyName))
        assertThat(queryParams.city).isEqualTo(Utils.toJsonIfNotEmpty(filterData.country))
        assertThat(queryParams.date).isEqualTo(Utils.toJsonIfNotEmpty(filterData.lastModified))
        assertThat(queryParams.itemName).isEqualTo(Utils.toJsonIfNotEmpty(filterData.itemName))
        assertThat(queryParams.modifiedBy).isEqualTo(Utils.toJsonIfNotEmpty(filterData.modifiedBy))
    }
}