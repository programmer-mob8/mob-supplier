package com.project.learningkitsupplier.screen.supplierlistscreen.viewmodel

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.MainDispatcherRule
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterData
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterOption
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.viewmodel.SupplierListViewModel
import com.project.libs.base.ApiResponse
import com.project.libs.base.Result
import com.project.libs.data.model.FilterListOptionEntity
import com.project.libs.data.model.Item
import com.project.libs.data.model.SupplierEntity
import com.project.libs.data.source.network.model.request.DeleteSupplierBodyRequest
import com.project.libs.data.source.network.model.request.EditSupplierStatusBodyRequest
import com.project.libs.data.source.network.model.request.GetSupplierListParamsRequest
import com.project.libs.domain.supplier.DeleteSupplierUseCase
import com.project.libs.domain.supplier.EditSupplierStatusUseCase
import com.project.libs.domain.supplier.GetFilterListOptionUseCase
import com.project.libs.domain.supplier.SupplierUseCase
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

class SupplierListScreenViewModelTest {

    private lateinit var viewModel: SupplierListViewModel

    private lateinit var callback: SupplierListCallback

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var supplierUseCase: SupplierUseCase

    @MockK
    private lateinit var getFilterListOptionUseCase: GetFilterListOptionUseCase

    @MockK
    private lateinit var editStatusSupplierUseCase: EditSupplierStatusUseCase

    @MockK
    private lateinit var deleteSupplierUseCase: DeleteSupplierUseCase

    @MockK
    private lateinit var exportUtil: ExportUtil

    var queryParams = GetSupplierListParamsRequest()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = SupplierListViewModel(
            supplierUseCase = supplierUseCase,
            getFilterListOptionUseCase = getFilterListOptionUseCase,
            editSupplierStatusUseCase = editStatusSupplierUseCase,
            deleteSupplierUseCase = deleteSupplierUseCase,
            exportUtil = exportUtil
        )

        callback = viewModel.getCallback()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun arrangeSupplierListUseCase(
        params: GetSupplierListParamsRequest,
        result: Result<List<SupplierEntity>>
    ) {
        println(params)
        coEvery {
            supplierUseCase(params)
        } returns flowOf(result)
    }

    private fun arrangeGetFilterOptionUseCase(
        result: Result<FilterListOptionEntity>
    ) {
        coEvery {
            getFilterListOptionUseCase()
        } returns flowOf(result)
    }

    private fun arrangeEditStatusSupplierUseCase(
        id: List<String>,
        status: Boolean,
        result: Result<Unit>
    ) {
        coEvery {
            editStatusSupplierUseCase(EditSupplierStatusBodyRequest(id, status))
        } returns flowOf(result)

    }

    private fun arrangeDeleteSupplierUseCase(
        body: DeleteSupplierBodyRequest,
        result: Result<ApiResponse<Unit>>
    ) {

        coEvery {
            deleteSupplierUseCase(body)
        } returns flowOf(result)
    }

    @Test
    fun `when supplierUseCase success isLoading should be false and supplierList should be empty object`() =
        runTest {
            // Arrange
            val expected = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(),
                    country = "'country",
                    state = "state",
                    city = "city",
                    zipCode = "zipCode",
                    companyLocation = "companyLocation",
                    companyPhoneNumber = "companyPhoneNumber",
                    picName = "picName",
                    picPhoneNumber = "picPhoneNumber",
                    picEmail = "picEmail",
                    status = true,
                    modifiedBy = "modifiedBy",
                    createdAt = "createdAt",
                    updatedAt = "updatedAt"
                )
            )

            arrangeSupplierListUseCase(queryParams, Result.Success(expected))

            // Act
            viewModel.initSupplier()

            // Assert
            assertThat(viewModel.uiState.value.isLoading).isFalse()
            assertThat(viewModel.uiState.value.supplier).isEqualTo(expected)
        }

    @Test
    fun `when supplierUseCase error isLoading should be false and supplierList should be empty object`() =
        runTest {
            // Arrange
            arrangeSupplierListUseCase(queryParams, Result.Error("error"))

            // Act
            viewModel.initSupplier()

            // Assert
            assertThat(viewModel.uiState.value.isLoading).isFalse()
            assertThat(viewModel.uiState.value.supplier).isEmpty()
        }

    @Test
    fun `when getFilterOptionUseCase success isLoadingGroup should be false and filterOption should return correct object`() =
        runTest {
            // Arrange
            val result = FilterListOptionEntity(
                cityOption = listOf(),
                statusOption = listOf(),
                itemNameOption = listOf(),
                modifiedByOption = listOf(),
                supplierOption = listOf()
            )

            val expected = SupplierListFilterOption(

                cityOption = listOf(),
                statusOption = listOf(),
                itemNameOption = listOf(),
                modifiedByOption = listOf(),
                companyNameOption = listOf(),
            )

            arrangeGetFilterOptionUseCase(Result.Success(result))

            // Act
            viewModel.getFilterOption()

            // Assert
            assertThat(viewModel.uiState.value.isLoadingGroup).isFalse()
            assertThat(viewModel.uiState.value.filterOption).isEqualTo(expected)
        }

    @Test
    fun `when getFilterOptionUseCase error isLoadingGroup should be false and filterOption should be empty object`() =
        runTest {
            // Arrange
            arrangeGetFilterOptionUseCase(Result.Error("error"))

            // Act
            viewModel.getFilterOption()

            // Assert
            assertThat(viewModel.uiState.value.isLoadingGroup).isFalse()
            assertThat(viewModel.uiState.value.filterOption).isEqualTo(SupplierListFilterOption())
        }

    @Test
    fun `when editStatusSupplierUseCase success isLoadingOverlay should be false and isActive should be true`() =
        runTest {
            val id = listOf("id")
            val status = true

            val expected = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(),
                    country = "'country",
                    state = "state",
                    city = "city",
                    zipCode = "zipCode",
                    companyLocation = "companyLocation",
                    companyPhoneNumber = "companyPhoneNumber",
                    picName = "picName",
                    picPhoneNumber = "picPhoneNumber",
                    picEmail = "picEmail",
                    status = true,
                    modifiedBy = "modifiedBy",
                    createdAt = "createdAt",
                    updatedAt = "updatedAt"
                )
            )

            coEvery {
                supplierUseCase(any())
            } returns flowOf(Result.Success(expected))

            // Arrange
            arrangeEditStatusSupplierUseCase(
                id = id,
                status = status,
                result = Result.Success(Unit)
            )
            arrangeGetFilterOptionUseCase(
                Result.Success(
                    FilterListOptionEntity(
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList()
                    )
                )
            )

            // Act
            viewModel.initSupplier()
            callback.onStatusUpdate(id, status)

            // Assert
            assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewModel.uiState.value.isActive).isTrue()
            assertThat(viewModel.uiState.value.statusState).isTrue()
            assertThat(viewModel.uiState.value.supplier.any { it.id == expected[0].id && it.status == expected[0].status }).isTrue()
        }

    @Test
    fun `when editStatusSupplierUseCase error isLoadingOverlay should be false and isActive should be false`() =
        runTest {
            // Arrange
            arrangeEditStatusSupplierUseCase(
                id = listOf("id"),
                status = false,
                result = Result.Error("error")
            )
            arrangeGetFilterOptionUseCase(
                Result.Success(
                    FilterListOptionEntity(
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList()
                    )
                )
            )

            // Act
            callback.onStatusUpdate(listOf("id"), false)

            // Assert
            assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewModel.uiState.value.isActive).isFalse()
            assertThat(viewModel.uiState.value.statusState).isFalse()
            assertThat(viewModel.uiState.value.supplier.any { it.id == "id" && it.status }).isFalse()
        }

    @Test
    fun `when deleteSupplierUseCase success isLoadingOverlay should be false and supplier should be empty object`() =
        runTest {
            // Arrange
            val id = "id"
            arrangeDeleteSupplierUseCase(
                body = DeleteSupplierBodyRequest(listOf(id)),
                Result.Success(ApiResponse(Unit))
            )
            arrangeGetFilterOptionUseCase(
                Result.Success(
                    FilterListOptionEntity(
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList()
                    )
                )
            )
            arrangeSupplierListUseCase(queryParams, Result.Success(emptyList()))
            // Act

            callback.deleteSupplier(listOf(id))

            // Assert
            assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewModel.uiState.value.deleteState).isTrue()
        }

    @Test
    fun `when deleteSupplierUseCase error isLoadingOverlay should be false and supplier should be empty object`() =
        runTest {
            val id = "id"
            // Arrange
            arrangeDeleteSupplierUseCase(
                body = DeleteSupplierBodyRequest(listOf(id)),
                Result.Error("error")
            )
            arrangeGetFilterOptionUseCase(
                Result.Success(
                    FilterListOptionEntity(
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList()
                    )
                )
            )

            // Act
            callback.deleteSupplier(listOf("id"))

            // Assert
            assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewModel.uiState.value.deleteState).isFalse()
        }

    @Test
    fun `when onRefresh is called uiState should reset filterData and searchQuery`() = runTest {
        // Arrange
        val expected = SupplierListUiState(
            filterData = SupplierListFilterData(),
            searchQuery = ""
        )

        arrangeSupplierListUseCase(queryParams, Result.Success(emptyList()))
        arrangeGetFilterOptionUseCase(
            Result.Success(
                FilterListOptionEntity(
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList()
                )
            )
        )


        // Act
        callback.onRefresh()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expected)
    }

    @Test
    fun `when onSearch is called uiState should update supplier list`() = runTest {
        // Arrange
        val expected = SupplierListUiState(
            searchQuery = "test"
        )
        val params = GetSupplierListParamsRequest(
            search = "test",
            supplier = null,
            city = null,
            itemName = null,
            modifiedBy = null,
            page = null,
            limit = null,
            sortOrder = null,
            date = null,
            status = null,
            sortBy = null
        )
        arrangeSupplierListUseCase(queryParams, Result.Success(emptyList()))

        coEvery {
            supplierUseCase(params)
        } returns flowOf(Result.Success(emptyList()))

        // Act
        callback.onSearch("test")

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expected)
        assertThat(viewModel.uiState.value.supplier).isEqualTo(emptyList<SupplierEntity>())
    }

    @Test
    fun `when toggleSelectAll is called and isAllSelected is true itemSelected should be empty list`() =
        runTest {
            // Arrange
            val suppliers = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(),
                    country = "'country",
                    state = "state",
                    city = "city",
                    zipCode = "zipCode",
                    companyLocation = "companyLocation",
                    companyPhoneNumber = "companyPhoneNumber",
                    picName = "picName",
                    picPhoneNumber = "picPhoneNumber",
                    picEmail = "picEmail",
                    status = true,
                    modifiedBy = "modifiedBy",
                    createdAt = "createdAt",
                    updatedAt = "updatedAt"

                )
            )

            val expected = SupplierListUiState(
                supplier = suppliers,
                supplierDefault = suppliers,
                itemSelected = emptyList(),
                isAllSelected = false
            )
            arrangeSupplierListUseCase(queryParams, Result.Success(suppliers))

            // Act
            viewModel.initSupplier()
            callback.onToggleSelectAll // selectAll
            callback.onToggleSelectAll // unselectAll


            // Assert
            assertThat(viewModel.uiState.value).isEqualTo(expected)
        }

    @Test
    fun `when toggleSelectAll is called and isAllSelected is false itemSelected should be supplier list`() =
        runTest {
            // Arrange
            val suppliers = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(),
                    country = "'country",
                    state = "state",
                    city = "city",
                    zipCode = "zipCode",
                    companyLocation = "companyLocation",
                    companyPhoneNumber = "companyPhoneNumber",
                    picName = "picName",
                    picPhoneNumber = "picPhoneNumber",
                    picEmail = "picEmail",
                    status = true,
                    modifiedBy = "modifiedBy",
                    createdAt = "createdAt",
                    updatedAt = "updatedAt"

                )
            )
            arrangeSupplierListUseCase(queryParams, Result.Success(suppliers))

            // Act
            viewModel.initSupplier()
            callback.onToggleSelectAll()

            // Assert
            assertThat(viewModel.uiState.value.supplier).isEqualTo(suppliers)
            assertThat(viewModel.uiState.value.supplierDefault).isEqualTo(suppliers)
            assertThat(viewModel.uiState.value.itemSelected).isEqualTo(suppliers)
            assertThat(viewModel.uiState.value.isAllSelected).isTrue()
        }

    @Test
    fun `when updatedItemSelected is called with an unselected supplier it should be added to itemSelected`() =
        runTest {
            val supplier = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(),
                    country = "'country",
                    state = "state",
                    city = "city",
                    zipCode = "zipCode",
                    companyLocation = "companyLocation",
                    companyPhoneNumber = "companyPhoneNumber",
                    picName = "picName",
                    picPhoneNumber = "picPhoneNumber",
                    picEmail = "picEmail",
                    status = true,
                    modifiedBy = "modifiedBy",
                    createdAt = "createdAt",
                    updatedAt = "updatedAt"
                )
            )

            val expected = SupplierListUiState(
                itemSelected = supplier,
                supplierDefault = supplier,
                supplier = supplier
            )

            arrangeSupplierListUseCase(queryParams, Result.Success(supplier))

            // Act
            viewModel.initSupplier()
            callback.onUpdateSelectedItem(supplier[0])

            // Assert
            assertThat(viewModel.uiState.value).isEqualTo(expected)

        }

    @Test
    fun `when updatedItemSelected is called with a selected supplier it should be removed from itemSelected`() =
        runTest {
            val supplier = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(),
                    country = "'country",
                    state = "state",
                    city = "city",
                    zipCode = "zipCode",
                    companyLocation = "companyLocation",
                    companyPhoneNumber = "companyPhoneNumber",
                    picName = "picName",
                    picPhoneNumber = "picPhoneNumber",
                    picEmail = "picEmail",
                    status = true,
                    modifiedBy = "modifiedBy",
                    createdAt = "createdAt",
                    updatedAt = "updatedAt"
                )
            )

            val expected = SupplierListUiState(
                supplier = supplier,
                supplierDefault = supplier,
                itemSelected = emptyList()
            )

            arrangeSupplierListUseCase(queryParams, Result.Success(supplier))

            // Act
            viewModel.initSupplier()
            callback.onUpdateSelectedItem(supplier[0])
            callback.onUpdateSelectedItem(supplier[0])

            // Assert
            assertThat(viewModel.uiState.value).isEqualTo(expected)
        }

    @Test
    fun `when updateFilter is called it should return the filtered supplier data`() = runTest {

        val filterData = SupplierListFilterData(
            country = listOf("country"),
            status = listOf(false),
            companyName = listOf("companyName"),
            itemName = listOf("itemName"),
            modifiedBy = listOf("modifiedBy"),
        )

        val params = GetSupplierListParamsRequest(
            search = null,
            supplier = Utils.toJsonIfNotEmpty(filterData.companyName),
            city = Utils.toJsonIfNotEmpty(filterData.country),
            itemName = Utils.toJsonIfNotEmpty(filterData.itemName),
            modifiedBy = Utils.toJsonIfNotEmpty(filterData.modifiedBy),
            page = null,
            limit = null,
            sortOrder = null,
            date = null,
            status = Utils.toJsonIfNotEmpty(filterData.status),
            sortBy = null
        )

        val filterOption = FilterListOptionEntity(
            cityOption = listOf(),
            statusOption = listOf(),
            itemNameOption = listOf(),
            modifiedByOption = listOf(),
            supplierOption = listOf()
        )

        arrangeGetFilterOptionUseCase(Result.Success(filterOption))
        arrangeSupplierListUseCase(params, Result.Success(emptyList()))

        // Act
        callback.onFilter(filterData)

        // Assert
        println("Assert ${viewModel.uiState.value.queryParams}")
        coVerify(exactly = 1) {
            supplierUseCase(eq(params))
        }
    }

    @Test
    fun `when onUpdatedSuppliers is called with a new supplier it should be added to the list`() = runTest {
        val newSupplier = SupplierEntity(
            id = "",
            companyName = "companyName",
            item = listOf(),
            country = "country",
            state = "state",
            city = "city",
            zipCode = "zipCode",
            companyLocation = "companyLocation",
            companyPhoneNumber = "companyPhoneNumber",
            picName = "picName",
            picPhoneNumber = "picPhoneNumber",
            picEmail = "picEmail",
            status = false,
            modifiedBy = "modifiedBy",
            createdAt = "createdAt",
            updatedAt = "updatedAt"
        )

        val expected = SupplierListUiState(
            isLoading = false,
            supplier = listOf(newSupplier),
            supplierDefault = listOf(newSupplier)
        )

        arrangeSupplierListUseCase(queryParams, Result.Success(listOf(newSupplier)))

        // Act
        callback.onUpdateSupplier(newSupplier)
        viewModel.initSupplier()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expected)
    }

    @Test
    fun `when onUpdatedSuppliers is called with a deleted supplier it should be removed from the list`() = runTest {
        val deletedSupplier = SupplierEntity(
            id = "",
            companyName = "companyName",
            item = listOf(),
            country = "country",
            state = "state",
            city = "city",
            zipCode = "zipCode",
            companyLocation = "companyLocation",
            companyPhoneNumber = "companyPhoneNumber",
            picName = "picName",
            picPhoneNumber = "picPhoneNumber",
            picEmail = "picEmail",
            status = false,
            modifiedBy = "modifiedBy",
            createdAt = "createdAt",
            updatedAt = "updatedAt"
        )

        val expected = SupplierListUiState(
            isLoading = false,
            supplier = listOf(deletedSupplier),
            supplierDefault = listOf(deletedSupplier)
        )

        arrangeSupplierListUseCase(queryParams, Result.Success(listOf(deletedSupplier)))

        // Act
        callback.onUpdateSupplier(deletedSupplier)
        viewModel.initSupplier()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expected)
    }

    @Test
    fun `when onUpdatedSuppliers is called with an updated supplier it should be updated in the list`() = runTest {
        val existingSupplier = SupplierEntity(
            id = "id",
            companyName = "companyName",
            item = listOf(),
            country = "country",
            state = "state",
            city = "city",
            zipCode = "zipCode",
            companyLocation = "companyLocation",
            companyPhoneNumber = "companyPhoneNumber",
            picName = "picName",
            picPhoneNumber = "picPhoneNumber",
            picEmail = "picEmail",
            status = false,
            modifiedBy = "modifiedBy",
            createdAt = "createdAt",
            updatedAt = "updatedAt"
        )

        val updatedSupplier = existingSupplier.copy(companyName = "test")

        val expected = SupplierListUiState(
            isLoading = false,
            supplier = listOf(updatedSupplier),
            supplierDefault = listOf(updatedSupplier)
        )

        arrangeSupplierListUseCase(queryParams, Result.Success(listOf(updatedSupplier)))

        // Act
        viewModel.initSupplier()
        callback.onUpdateSupplier(updatedSupplier)
        viewModel.initSupplier()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expected)
    }

    @Test
    fun `when clearSelectedItems is called it should clear the selected items`() = runTest {
        val expected = SupplierListUiState(
            itemSelected = emptyList()
        )

        // Act
        callback.onClearSelectedItem()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expected)
    }

    @Test
    fun `when showSearch is called with true it should update showSearch to true`() = runTest {
        // Act
        viewModel.showSearch(true)

        // Assert
        assertThat(viewModel.uiState.value.showSearch).isTrue()
    }

    @Test
    fun `when showSearch is called with true it should update showSearch to false`() = runTest {
        // Act
        viewModel.showSearch(false)

        // Assert
        assertThat(viewModel.uiState.value.showSearch).isFalse()
    }

    @Test
    fun `when actionSheet is called with true it should update actionSheet to true`() = runTest {
        // Act
        viewModel.showActionSheet(true)

        // Assert
        assertThat(viewModel.uiState.value.showActionSheet).isTrue()
    }

    @Test
    fun `when actionSheet is called with true it should update actionSheet to false`() = runTest {
        // Act
        viewModel.showActionSheet(false)

        // Assert
        assertThat(viewModel.uiState.value.showActionSheet).isFalse()
    }

    @Test
    fun `when downloadDialog is called with true it should update downloadDialog to true`() = runTest {
        // Act
        viewModel.showDownloadDialog(true)

        // Assert
        assertThat(viewModel.uiState.value.showDownloadDialog).isTrue()
    }

    @Test
    fun `when downloadDialog is called with false it should update downloadDialog to false`() = runTest {
        // Act
        viewModel.showDownloadDialog(false)

        // Assert
        assertThat(viewModel.uiState.value.showDownloadDialog).isFalse()
    }

    @Test
    fun `when deleteDialog is called with true it should update deleteDialog to true`() = runTest {
        // Act
        viewModel.showDeleteDialog(true)

        // Assert
        assertThat(viewModel.uiState.value.showDeleteDialog).isTrue()
    }

    @Test
    fun `when deleteDialog is called with false it should update deleteDialog to false`() = runTest {
        // Act
        viewModel.showDeleteDialog(false)

        // Assert
        assertThat(viewModel.uiState.value.showDeleteDialog).isFalse()
    }

    @Test
    fun `when updateStatus is called with true it should update updateStatus to true`() = runTest {
        // Act
        viewModel.showUpdateStatus(true)

        // Assert
        assertThat(viewModel.uiState.value.showUpdateStatus).isTrue()
    }

    @Test
    fun `when updateStatus is called with false it should update updateStatus to false`() = runTest {
        // Act
        viewModel.showUpdateStatus(false)

        // Assert
        assertThat(viewModel.uiState.value.showUpdateStatus).isFalse()
    }

    @Test
    fun `when showFilter is called with false it should update showFilter to false`() = runTest {
        // Act
        viewModel.showFilter(false)

        // Assert
        assertThat(viewModel.uiState.value.showFilter).isFalse()
    }

    @Test
    fun `when showFilter is called with true it should update showFilter to true`() = runTest {
        // Act
        viewModel.showFilter(true)

        // Assert
        assertThat(viewModel.uiState.value.showFilter).isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onDownload should update the downloadState based on result`() = runTest {
        // Arrange
        val mockData = SupplierEntity(
            id = "id",
            companyName = "companyName",
            item = listOf(Item(
                id = "id",
                supplierId = "supplierId",
                itemName = "itemName",
                sku = listOf("sku1")
            )),
            country = "country",
            state = "state",
            city = "city",
            zipCode = "zipCode",
            companyLocation = "companyLocation",
            companyPhoneNumber = "companyPhoneNumber",
            picName = "picName",
            picPhoneNumber = "picPhoneNumber",
            picEmail = "picEmail",
            status = false,
            modifiedBy = "modifiedBy",
            createdAt = "2026-01-23T17:00:00.000Z",
            updatedAt = "2026-01-23T17:00:00.000Z"
        )

        val mockParams = GetSupplierListParamsRequest()
        val fileName = "test.xlsx"
        val formattedDate = "2026-01-23T17:00:00.000Z".toDateFormatter()
        val expectedContent = listOf(
            mapOf(
                "status" to false.toString(),
                "companyName" to "companyName",
                "state" to "state",
                "country" to "country",
                "sku" to mockData.item.map { it.sku }.joinToString(", "),
                "updatedAt" to formattedDate,
                "picName" to "picName"
            )
        )

        val exportSlot = slot<List<Map<String, String>>>()

        coEvery {
            exportUtil.exportToExcel(eq(fileName), capture(exportSlot))
        } just runs

        arrangeSupplierListUseCase(
            params = queryParams,
            result = Result.Success(listOf(mockData))
        )

        // Act
        viewModel.downloadList(fileName)

        advanceUntilIdle()

        val state = viewModel.uiState.value

        // Assert
        Truth.assertThat(state.isLoadingOverlay).isFalse()
        Truth.assertThat(state.downloadState).isTrue()

        coVerify {
            supplierUseCase(mockParams)
            exportUtil.exportToExcel(eq(fileName), expectedContent)
        }

        Truth.assertThat(exportSlot.captured).isEqualTo(expectedContent)
    }



    // Init Test
    @Test
    fun `init should call initSupplier and getFilterOption`() = runTest {
        // Arrange
        coEvery { supplierUseCase(any()) } returns flowOf(Result.Success(emptyList()))
        coEvery { getFilterListOptionUseCase() } returns flowOf(Result.Success(FilterListOptionEntity(emptyList(), emptyList(), emptyList(), emptyList(), emptyList())))

        // Act
        viewModel.init()

        // Assert
        coVerify(exactly = 1) { supplierUseCase(any()) }
        coVerify(exactly = 1) { getFilterListOptionUseCase() }
    }



    // Callback Test
    @Test
    fun `constructor should return correct object`() = runTest {
        // Arrange
        var onRefresh = false
        var onSearch = false
        var onFilter = false
        var onToggleSelectAll = false
        var onUpdateSelectedItem = false
        var onStatusUpdate = false
        var onUpdateSupplier = false
        var onClearSelectedItem = false
        var deleteSupplier = false
        var onResetMessageState = false

        // Act
        val supplierListCb = SupplierListCallback(
            onRefresh = {
                onRefresh = true
            },
            onSearch = {
                onSearch = true
            },
            onFilter = {
                onFilter = true
            },
            onToggleSelectAll = {
                onToggleSelectAll = true
            },
            onUpdateSelectedItem = {
                onUpdateSelectedItem = true
            },
            onStatusUpdate = { _,_ ->
                onStatusUpdate = true
            },
            onUpdateSupplier = {
                onUpdateSupplier = true
            },
            onClearSelectedItem = {
                onClearSelectedItem = true
            },
            deleteSupplier = {
                deleteSupplier = true
            },
            onResetMessageState = {
                onResetMessageState = true
            }
        )

        supplierListCb.onRefresh()
        supplierListCb.onSearch("test")
        supplierListCb.onFilter(SupplierListFilterData())
        supplierListCb.onToggleSelectAll()
        supplierListCb.onUpdateSelectedItem(SupplierEntity())
        supplierListCb.onStatusUpdate(listOf(), true)
        supplierListCb.onUpdateSupplier(SupplierEntity())
        supplierListCb.onClearSelectedItem()
        supplierListCb.deleteSupplier(listOf())
        supplierListCb.onResetMessageState()

        // Assert
        Truth.assertThat(onRefresh).isTrue()
        Truth.assertThat(onSearch).isTrue()
        Truth.assertThat(onFilter).isTrue()
        Truth.assertThat(onToggleSelectAll).isTrue()
        Truth.assertThat(onUpdateSelectedItem).isTrue()
        Truth.assertThat(onStatusUpdate).isTrue()
        Truth.assertThat(onUpdateSupplier).isTrue()
        Truth.assertThat(onClearSelectedItem).isTrue()
        Truth.assertThat(deleteSupplier).isTrue()
        Truth.assertThat(onResetMessageState).isTrue()
    }



    // ResetMessageState Test
    @Test
    fun `reset message sets all state to null`() = runTest {
        // Act
        callback.onResetMessageState()

        // Assert
        assertThat(viewModel.uiState.value.deleteState).isNull()
        assertThat(viewModel.uiState.value.downloadState).isNull()
        assertThat(viewModel.uiState.value.statusState).isNull()
    }

    @Test
    fun `reset message state does not affect other uiState properties`() = runTest {
        // Arrange
        val initialUiState = viewModel.uiState.value

        // Act
        callback.onResetMessageState()

        // Assert
        assertThat(viewModel.uiState.value.copy(
            deleteState = initialUiState.deleteState,
            downloadState = initialUiState.downloadState,
            statusState = initialUiState.statusState
        )).isEqualTo(initialUiState)
    }

}