package com.project.learningkitsupplier.screen.changelog.viewmmodel

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.MainDispatcherRule
import com.project.learningkitsupplier.module.changelog.ChangelogFilterData
import com.project.learningkitsupplier.module.changelog.ChangelogFilterOption
import com.project.learningkitsupplier.module.changelog.ChangelogListCallback
import com.project.learningkitsupplier.ui.screen.changelog.uistate.ChangelogUiState
import com.project.learningkitsupplier.ui.screen.changelog.viewmodel.ChangelogViewModel
import com.project.libs.base.Result
import com.project.libs.data.model.ChangelogEntity
import com.project.libs.data.model.ChangelogFilterEntity
import com.project.libs.data.source.network.model.request.GetChangelogQueryParamsRequest
import com.project.libs.domain.supplier.GetChangelogOptionUseCase
import com.project.libs.domain.supplier.GetChangelogUseCase
import com.tagsamurai.tscomponents.utils.ExportUtil
import com.tagsamurai.tscomponents.utils.Utils
import com.tagsamurai.tscomponents.utils.Utils.toDateFormatter
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChangelogViewModelTest {

    private lateinit var changelogViewModel: ChangelogViewModel

    private lateinit var callback: ChangelogListCallback

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var changelogUseCase: GetChangelogUseCase

    @MockK
    private lateinit var changelogOptionUseCase: GetChangelogOptionUseCase

    @MockK
    private lateinit var exportUtil: ExportUtil

    private val queryParams = GetChangelogQueryParamsRequest()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        changelogViewModel = ChangelogViewModel(
            changelogUseCase = changelogUseCase,
            changelogOptionUseCase = changelogOptionUseCase,
            exportUtil = exportUtil
        )

        callback = changelogViewModel.getCallback()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun arrangeChangelogUseCase(
        query: GetChangelogQueryParamsRequest,
        result: Result<List<ChangelogEntity>>
    ) {
        coEvery {
            changelogUseCase(query)
        } returns flowOf(result)
    }

    private fun arrangeChangelogOptionUseCase(
        result: Result<ChangelogFilterEntity>
    ) {
        coEvery {
            changelogOptionUseCase()
        } returns flowOf(result)
    }

    @Test
    fun `when changelogUseCase is success isLoading should be false and changelogList should be correct`() =
        runTest {
            // Arrange
            val expected = listOf(
                ChangelogEntity(
                    id = "id",
                    action = "action",
                    field = "field",
                    oldValue = "oldValue",
                    newValue = "newValue",
                    modifiedBy = "modifiedBy",
                    objectX = "objectX",
                    objectName = "objectName",
                    timeStamp = "timeStamp"
                )
            )

            val filterEntity = ChangelogFilterEntity(
                actionOption = listOf(),
                fieldOption = listOf(),
                modifiedByOption = listOf()
            )

            arrangeChangelogUseCase(queryParams, Result.Success(expected))
            arrangeChangelogOptionUseCase(Result.Success(filterEntity))

            // Act
            changelogViewModel.initChangelog()

            // Assert
            assertThat(changelogViewModel.uiState.value.isLoading).isFalse()
            assertThat(changelogViewModel.uiState.value.changelog).isEqualTo(expected)

        }

    @Test
    fun `when changelogUseCase is error isLoading should be false and changelog list return an empty list`() =
        runTest {
            // Arrange
            val expected = emptyList<ChangelogEntity>()

            arrangeChangelogUseCase(queryParams, Result.Error("error"))

            // Act
            changelogViewModel.initChangelog()

            // Assert
            assertThat(changelogViewModel.uiState.value.isLoading).isFalse()
            assertThat(changelogViewModel.uiState.value.changelog).isEqualTo(expected)
        }


    // GetFilterOptions Test
    @Test
    fun `when changelogOptionUseCase is success isLoading should be false and filterOptions should be correct`() =
        runTest {
            // Arrange
            val result = ChangelogFilterEntity(
                actionOption = emptyList(),
                fieldOption = emptyList(),
                modifiedByOption = emptyList()
            )

            val expected = ChangelogFilterOption(
                actionOption = emptyList(),
                fieldOption = emptyList(),
                modifiedBy = emptyList(),
            )

            arrangeChangelogOptionUseCase(Result.Success(result))

            // Act
            changelogViewModel.getFilterOption()

            // Assert
            assertThat(changelogViewModel.uiState.value.isLoading).isFalse()
            assertThat(changelogViewModel.uiState.value.filterOption).isEqualTo(expected)
        }

    @Test
    fun `when changelogOptionUseCase is error isLoading should be false and filterOptions should be empty object`() =
        runTest {
            // Arrange
            arrangeChangelogOptionUseCase(Result.Error("error"))

            // Act
            changelogViewModel.getFilterOption()

            // Assert
            assertThat(changelogViewModel.uiState.value.isLoading).isFalse()
            assertThat(changelogViewModel.uiState.value.filterOption).isEqualTo(
                ChangelogFilterOption()
            )
        }


    // Search Test
    @Test
    fun `when onSearch is called uiState should update changelog list`() =
        runTest {

            // Arrange
            val expected = ChangelogUiState(
                searchQuery = "search"
            )

            val params = GetChangelogQueryParamsRequest(
                search = "search",
                action = null,
                field = null,
                modifiedBy = null,
                page = null,
                limit = null,
                date = null,
                sortOrder = null,
                sortBy = null
            )

            val filter = ChangelogFilterEntity(
                actionOption = emptyList(),
                fieldOption = emptyList(),
                modifiedByOption = emptyList()
            )

            arrangeChangelogOptionUseCase(Result.Success(filter))
            arrangeChangelogUseCase(params, Result.Success(emptyList()))

            coEvery {
                changelogUseCase(params)
            } returns flowOf(Result.Success(emptyList()))

            // Act
            callback.onSearch("search")
            changelogViewModel.getFilterOption()

            // Assert
            assertThat(changelogViewModel.uiState.value).isEqualTo(expected)
            assertThat(changelogViewModel.uiState.value.changelog).isEqualTo(emptyList<ChangelogEntity>())
            coVerify { changelogOptionUseCase() }
        }


    // onRefresh Test
    @Test
    fun `when onRefresh is called filterData and searchQuery should be empty and init should be called`() =
        runTest {
            // Arrange
            val changelog = listOf(
                ChangelogEntity(
                    id = "id",
                    action = "action",
                    field = "field",
                    oldValue = "oldValue",
                    newValue = "newValue",
                    modifiedBy = "modifiedBy",
                    objectX = "objectX",
                    objectName = "objectName",
                    timeStamp = "timeStamp"
                )
            )

            val expected = ChangelogUiState(
                filterData = ChangelogFilterData(),
                searchQuery = "",
                changelog = changelog,
                changelogDefault = changelog
            )


            val filter = ChangelogFilterEntity(
                actionOption = emptyList(),
                fieldOption = emptyList(),
                modifiedByOption = emptyList()
            )

            arrangeChangelogUseCase(queryParams, Result.Success(changelog))
            arrangeChangelogOptionUseCase(Result.Success(filter))
            // Act
            callback.onRefresh()

            // Assert
            assertThat(changelogViewModel.uiState.value).isEqualTo(expected)
        }


    // UpdateFilter Test
    @Test
    fun `when updateFilter is called uiState should update changelog list`() =
        runTest {

            val filterData = ChangelogFilterData(
                action = listOf("action"),
                field = listOf("field"),
                modifiedBy = listOf("modifiedBy")
            )

            val params = GetChangelogQueryParamsRequest(
                search = null,
                action = Utils.toJsonIfNotEmpty(filterData.action),
                field = Utils.toJsonIfNotEmpty(filterData.field),
                modifiedBy = Utils.toJsonIfNotEmpty(filterData.modifiedBy),
                page = null,
                limit = null,
                date = null,
                sortOrder = null,
                sortBy = null
            )

            val filterOption = ChangelogFilterEntity(
                actionOption = listOf(),
                fieldOption = listOf(),
                modifiedByOption = listOf()
            )

            arrangeChangelogOptionUseCase(Result.Success(filterOption))
            arrangeChangelogUseCase(params, Result.Success(emptyList()))

            // Act
            callback.onFilter(filterData)

            // Assert
            coVerify(exactly = 1) {
                changelogUseCase(eq(params))
            }
        }


    // OnUpdateChangelog Test
    @Test
    fun `when onUpdateChangelog is called uiState should update changelog list`() =
        runTest {

            val result = ChangelogEntity(
                    id = "id",
                    action = "action",
                    field = "field",
                    oldValue = "oldValue",
                    newValue = "newValue",
                    modifiedBy = "modifiedBy",
                    objectX = "objectX",
                    objectName = "objectName",
                    timeStamp = "timeStamp"
            )


            val expected = ChangelogUiState(
                isLoading = false,
                changelog = listOf(result),
                changelogDefault = listOf(result)
            )

            arrangeChangelogUseCase(queryParams, Result.Success(listOf(result)))

            // Act\
            changelogViewModel.initChangelog()
            callback.onUpdateChangelog(result)
            changelogViewModel.initChangelog()

            // Assert
            assertThat(changelogViewModel.uiState.value).isEqualTo(expected)
        }



    // parseDownloadContent Test
    @Test
    fun `parseDownloadContent should return correct map for given data`() = runTest {
        val data = listOf(
            ChangelogEntity(
                id = "id",
                action = "action",
                field = "field",
                oldValue = "oldValue",
                newValue = "newValue",
                modifiedBy = "modifiedBy",
                objectX = "objectX",
                objectName = "objectName",
                timeStamp = "2023-10-10T10:10:10Z"
            )
        )

        val dateFormatter = data.first().timeStamp.toDateFormatter()

        val expected = listOf(
            mapOf(
                "action" to "action",
                "field" to "field",
                "oldValue" to "oldValue",
                "newValue" to "newValue",
                "modifiedBy" to "modifiedBy",
                "objectName" to "objectName",
                "date" to dateFormatter
            )
        )

        val result = changelogViewModel.parseDownloadContent(data)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `parseDownloadContent should return empty list for empty data`() = runTest {
        val data = emptyList<ChangelogEntity>()

        val result = changelogViewModel.parseDownloadContent(data)

        assertThat(result).isEmpty()
    }

    @Test
    fun `parseDownloadContent should handle null values correctly`() = runTest {
        val data = listOf(
            ChangelogEntity(
                id = "id",
                action = "action",
                field = "field",
                oldValue = "",
                newValue = "",
                modifiedBy = "modifiedBy",
                objectX = "objectX",
                objectName = "objectName",
                timeStamp = "2023-10-10T10:10:10Z"
            )
        )

        val dateFormatter = data.first().timeStamp.toDateFormatter()

        val expected = listOf(
            mapOf(
                "action" to "action",
                "field" to "field",
                "oldValue" to "",
                "newValue" to "",
                "modifiedBy" to "modifiedBy",
                "objectName" to "objectName",
                "date" to dateFormatter
            )
        )

        val result = changelogViewModel.parseDownloadContent(data)

        assertThat(result).isEqualTo(expected)
    }



    // DownloadList Test
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onDownload should update the downloadState based on result`() = runTest {
        val mockData = ChangelogEntity(
            id = "id",
            action = "action",
            field = "field",
            oldValue = "oldValue",
            newValue = "newValue",
            modifiedBy = "modifiedBy",
            objectX = "objectX",
            objectName = "objectName",
            timeStamp = "2023-10-10T10:10:10Z"
        )

        val mockParams = GetChangelogQueryParamsRequest()
        val fileName = "test.xlsx"
        val formattedDate = mockData.timeStamp.toDateFormatter()
        val expectecDownloadContent = listOf(
            mapOf(
                "action" to "action",
                "field" to "field",
                "oldValue" to "oldValue",
                "newValue" to "newValue",
                "modifiedBy" to "modifiedBy",
                "objectName" to "objectName",
                "date" to formattedDate
            )
        )

        val exportSlot = slot<List<Map<String, String>>>()

        coEvery {
            exportUtil.exportToExcel(eq(fileName), capture(exportSlot))
        } just runs

        arrangeChangelogUseCase(
            query = mockParams,
            result = Result.Success(listOf(mockData))
        )

        // Act
        changelogViewModel.downloadList(fileName)
        advanceUntilIdle()

        val state = changelogViewModel.uiState.value

        // Assert
        Truth.assertThat(state.isLoadingOverlay).isFalse()
        Truth.assertThat(state.downloadState).isTrue()

        coVerify {
            changelogUseCase(mockParams)
            exportUtil.exportToExcel(fileName, expectecDownloadContent)
        }

        Truth.assertThat(exportSlot.captured).isEqualTo(expectecDownloadContent)
    }



    // ResetMessageTest Test
    @Test
    fun `reset message sets all state to null`() = runTest {
        // Act
        callback.onResetMessageState()

        // Assert
        assertThat(changelogViewModel.uiState.value.downloadState).isNull()
    }

    @Test
    fun `reset message state does not affect other uiState properties`() = runTest {
        // Arrange
        val initialUiState = changelogViewModel.uiState.value

        // Act
        callback.onResetMessageState()

        // Assert
        assertThat(changelogViewModel.uiState.value.copy(
            downloadState = initialUiState.downloadState
        )).isEqualTo(initialUiState)
    }



    // Init test
    @Test
    fun `init should call initChangelog and getFilterOption`() = runTest {
        // Arrange
        coEvery { changelogUseCase(any()) } returns flowOf(Result.Success(emptyList()))
        coEvery { changelogOptionUseCase() } returns flowOf(Result.Success(ChangelogFilterEntity()))

        // Act
        changelogViewModel.init()

        // Assert
        coVerify(exactly = 1) { changelogUseCase(any()) }
        coVerify(exactly = 1) { changelogOptionUseCase() }
    }



    // Callback Test
    @Test
    fun `constructor should return correct object`() = runTest {
        // Arrange
        var onRefresh = false
        var onSearch = false
        var onFilter = false
        var onUpdateChangelog = false
        var onResetMessageState = false

        // Act
        val callback = ChangelogListCallback(
            onRefresh = { onRefresh = true },
            onResetMessageState = { onResetMessageState = true },
            onSearch = { onSearch = true },
            onFilter = { onFilter = true },
            onUpdateChangelog = { onUpdateChangelog = true }
        )

        callback.onRefresh()
        callback.onResetMessageState()
        callback.onSearch("search")
        callback.onFilter(ChangelogFilterData())
        callback.onUpdateChangelog(ChangelogEntity())

        // Assert
        assertThat(onRefresh).isTrue()
        assertThat(onSearch).isTrue()
        assertThat(onFilter).isTrue()
        assertThat(onUpdateChangelog).isTrue()
        assertThat(onResetMessageState).isTrue()
    }

}