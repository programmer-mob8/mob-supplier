package com.project.learningkitsupplier.screen.changelog.uistate

import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.module.changelog.ChangelogFilterData
import com.project.learningkitsupplier.module.changelog.ChangelogFilterOption
import com.project.learningkitsupplier.ui.screen.changelog.uistate.ChangelogUiState
import com.project.libs.data.model.ChangelogEntity
import com.tagsamurai.tscomponents.utils.Utils
import org.junit.Test

class ChangelogUiStateTest {
    @Test
    fun `constructor should return correct object with default values`() {
        // Arrange
        val expectedUiState = ChangelogUiState()

        // Act
        val result = ChangelogUiState()

        // Assert
        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `constructor should return correct object with custom values`() {
        // Arrange
        val showSearch = true
        val showDownloadDialog = true
        val searchQuery = "test"
        val filterOption = ChangelogFilterOption()
        val filterData = ChangelogFilterData(
            date = emptyList(),
            action = listOf("create"),
            field = listOf("name"),
            modifiedBy = listOf("user1")
        )
        val changelogDefault = listOf(ChangelogEntity())
        val changelog = listOf(ChangelogEntity())
        val isLoadingOverlay = true
        val isLoadingGroup = true
        val isLoading = true
        val downloadState = true

        val expectedUiState = ChangelogUiState(
            showSearch = showSearch,
            showDownloadDialog = showDownloadDialog,
            searchQuery = searchQuery,
            filterOption = filterOption,
            filterData = filterData,
            changelogDefault = changelogDefault,
            changelog = changelog,
            isLoadingOverlay = isLoadingOverlay,
            isLoadingGroup = isLoadingGroup,
            isLoading = isLoading,
            downloadState = downloadState
        )

        // Act
        val result = ChangelogUiState(
            showSearch = showSearch,
            showDownloadDialog = showDownloadDialog,
            searchQuery = searchQuery,
            filterOption = filterOption,
            filterData = filterData,
            changelogDefault = changelogDefault,
            changelog = changelog,
            isLoadingOverlay = isLoadingOverlay,
            isLoadingGroup = isLoadingGroup,
            isLoading = isLoading,
            downloadState = downloadState
        )

        // Assert
        assertThat(result).isEqualTo(expectedUiState)
    }

    @Test
    fun `queryParams should return correct value when all fields are empty`() {
        // Act
        val uiState = ChangelogUiState()
        val queryParams = uiState.queryParams

        // Assert
        assertThat(queryParams.search).isNull()
        assertThat(queryParams.date).isNull()
        assertThat(queryParams.action).isNull()
        assertThat(queryParams.field).isNull()
        assertThat(queryParams.modifiedBy).isNull()
    }

    @Test
    fun `queryParams should return correct value when all fields are filled`() {
        // Arrange
        val filterData = ChangelogFilterData(
            date = emptyList(),
            action = listOf("create"),
            field = listOf("name"),
            modifiedBy = listOf("user1")
        )
        val searchQuery = "test"
        val uiState = ChangelogUiState(
            searchQuery = searchQuery,
            filterData = filterData
        )

        // Act
        val queryParams = uiState.queryParams

        // Assert
        assertThat(queryParams.search).isEqualTo(searchQuery)
        assertThat(queryParams.date).isEqualTo(Utils.toJsonIfNotEmpty(filterData.date))
        assertThat(queryParams.action).isEqualTo(Utils.toJsonIfNotEmpty(filterData.action))
        assertThat(queryParams.field).isEqualTo(Utils.toJsonIfNotEmpty(filterData.field))
        assertThat(queryParams.modifiedBy).isEqualTo(Utils.toJsonIfNotEmpty(filterData.modifiedBy))
    }
}