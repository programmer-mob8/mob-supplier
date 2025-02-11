package com.project.learningkitsupplier.screen.detailscreen.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.MainDispatcherRule
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailCallback
import com.project.learningkitsupplier.navigation.SUPPLIER_ID
import com.project.learningkitsupplier.ui.screen.detailsupplier.viewmodel.SupplierDetailViewModel
import com.project.learningkitsupplier.util.generatedOptionDataString
import com.project.learningkitsupplier.util.getGroups
import com.project.learningkitsupplier.util.getPic
import com.project.learningkitsupplier.util.getTransaction
import com.project.libs.base.ApiResponse
import com.project.libs.base.Result
import com.project.libs.data.model.Item
import com.project.libs.data.model.SupplierDetailEntity
import com.project.libs.data.model.SupplierEntity
import com.project.libs.data.source.network.model.request.DeleteSupplierBodyRequest
import com.project.libs.domain.supplier.DeleteSupplierUseCase
import com.project.libs.domain.supplier.GetSupplierByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SupplierDetailViewModelTest {

    private lateinit var viewModel: SupplierDetailViewModel

    private lateinit var callback: SupplierDetailCallback

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val supplierId = "1"

    @MockK
    private lateinit var deleteSupplierUseCase: DeleteSupplierUseCase

    @MockK
    private lateinit var getSupplierByIdUseCase: GetSupplierByIdUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { savedStateHandle.get<String>(SUPPLIER_ID) } returns supplierId

        viewModel = SupplierDetailViewModel(
            deleteSupplierUseCase = deleteSupplierUseCase,
            getSupplierByIdUseCase = getSupplierByIdUseCase,
            savedStateHandle = savedStateHandle,
        )

        callback = viewModel.getCallback()

        coEvery { getSupplierByIdUseCase(supplierId) } returns flowOf(
            Result.Success(SupplierEntity(
            companyName = "Test Company",
            item = listOf(
                Item(
                    id = "id",
                    supplierId = "supplierId",
                    itemName = "itemName",
                    sku = listOf("SKU1")
                )
            ),
            country = "Country",
            state = "State",
            city = "City",
            zipCode = "12345",
            companyLocation = "Location",
            companyPhoneNumber = "1234567890",
            picPhoneNumber = "0987654321",
            picName = "PIC Name",
            picEmail = "pic@example.com",
            status = true,
            modifiedBy = "Modifier",
            createdAt = "2023-01-01",
            updatedAt = "2023-01-02"
        )))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun arrangeDeleteSupplierUseCase(
        body: DeleteSupplierBodyRequest,
        result: Result<ApiResponse<Unit>>
    ) {

        coEvery {
            deleteSupplierUseCase(body)
        } returns flowOf(result)
    }

    private fun arrangeGetSupplierByIdUseCase(
        id: String,
        result: Result<SupplierEntity>
    ) {
        coEvery {
            getSupplierByIdUseCase(id)
        } returns flowOf(result)
    }


    // DeleteSupplierUseCase Test
    @Test
    fun `delete supplier success isLoadingOverlay should be false and supplier should be empty object`() {

        // Arrange
        val body = DeleteSupplierBodyRequest(listOf(supplierId))
        val result = Result.Success(ApiResponse(Unit))

        arrangeDeleteSupplierUseCase(body, result)

        // Act
        callback.deleteSupplier(listOf(supplierId))

        // Assert
        assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
        assertThat(viewModel.uiState.value.deleteState).isTrue()
    }

    @Test
    fun `delete supplier error isLoadingOverlay should be false and supplier should be empty object`() {
        // Arrange
        val id = listOf("1")
        arrangeDeleteSupplierUseCase(DeleteSupplierBodyRequest(id), Result.Error("error"))

        // Act
        callback.deleteSupplier(id)

        // Assert
        assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
        assertThat(viewModel.uiState.value.deleteState).isFalse()
    }


    // GetSupplierByIdUseCase Test
    @Test
    fun `get supplier by id success updates uiState correctly`() = runTest {
        // Arrange
        val supplier = SupplierEntity(
            companyName = "Test Company",
            item = listOf(
                Item(
                    id = "id",
                    supplierId = "supplierId",
                    itemName = "itemName",
                    sku = listOf("SKU1")
                )
            ),
            country = "Country",
            state = "State",
            city = "City",
            zipCode = "12345",
            companyLocation = "Location",
            companyPhoneNumber = "1234567890",
            picPhoneNumber = "0987654321",
            picName = "PIC Name",
            picEmail = "pic@example.com",
            status = true,
            modifiedBy = "Modifier",
            createdAt = "2023-01-01",
            updatedAt = "2023-01-02"
        )
        val result = Result.Success(supplier)

        arrangeGetSupplierByIdUseCase(supplierId, result)

        // Act
        viewModel.getSupplierId(supplierId)

        // Assert
        assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
        assertThat(viewModel.uiState.value.supplierDetailEntity).isEqualTo(
            SupplierDetailEntity(
                companyName = "Test Company",
                item = listOf(
                    SupplierDetailEntity.Item(
                        id = "id",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("SKU1")
                    )
                ),
                country = "Country",
                state = "State",
                city = "City",
                zipCode = "12345",
                companyLocation = "Location",
                companyPhoneNumber = "1234567890",
                picPhoneNumber = "0987654321",
                picName = "PIC Name",
                picEmail = "pic@example.com",
                status = true,
                modifiedBy = "Modifier",
                createdAt = "2023-01-01",
                updatedAt = "2023-01-02"
            )
        )
    }

    @Test
    fun `get supplier by id error updates uiState correctly`() = runTest {
        // Arrange
        arrangeGetSupplierByIdUseCase(supplierId, Result.Error("error"))

        // Act
        viewModel.getSupplierId(supplierId)

        // Assert
        assertThat(viewModel.uiState.value.isLoadingOverlay).isFalse()
        assertThat(viewModel.uiState.value.supplierDetailEntity).isEqualTo(
            SupplierDetailEntity(
                companyName = "",
                item = emptyList(),
                country = "",
                state = "",
                city = "",
                zipCode = "",
                companyLocation = "",
                companyPhoneNumber = "",
                picPhoneNumber = "",
                picName = "",
                picEmail = "",
                status = false,
                modifiedBy = "",
                createdAt = "",
                updatedAt = ""
            )
        )
    }


    // GetFilterOption Test
    @Test
    fun `get filter option success updates uiState correctly`() = runTest {
        // Act
        viewModel.getFilterOption()

        // Assert
        assertThat(viewModel.uiState.value.filterOption.transactionOption).isEqualTo(
            generatedOptionDataString(
                getTransaction()
            )
        )
        assertThat(viewModel.uiState.value.filterOption.picNameOption).isEqualTo(
            generatedOptionDataString(
                getPic()
            )
        )
    }



    // GetGroupOption Test
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get group option success updates uiState correctly`() = runTest {
        // Act
        viewModel.getGroupOption()
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.isLoadingGroup).isFalse()
        assertThat(viewModel.uiState.value.filterOption.groupOption).isEqualTo(getGroups())
    }

    @Test
    fun `get group option sets isLoadingGroup to true initially`() = runTest {
        // Act
        viewModel.getGroupOption()

        // Assert
        assertThat(viewModel.uiState.value.isLoadingGroup).isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get group option sets isLoadingGroup to false after delay`() = runTest {
        // Act
        viewModel.getGroupOption()
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.isLoadingGroup).isFalse()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get group option updates groupOption correctly`() = runTest {
        // Act
        viewModel.getGroupOption()
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.filterOption.groupOption).isEqualTo(getGroups())
    }



    // ResetMessage Test
    @Test
    fun `reset message state sets deleteState to null`() = runTest {
        // Act
        callback.resetMessageState()

        // Assert
        assertThat(viewModel.uiState.value.deleteState).isNull()
    }

    @Test
    fun `reset message state does not affect other uiState properties`() = runTest {
        // Arrange
        val initialUiState = viewModel.uiState.value

        // Act
        callback.resetMessageState()

        // Assert
        assertThat(viewModel.uiState.value.copy(deleteState = initialUiState.deleteState)).isEqualTo(initialUiState)
    }



    // Init test
    @Test
    fun `init should call getSupplierId`() {
        // Arrange
        every { savedStateHandle.get<String>(SUPPLIER_ID) } returns ""
        viewModel = SupplierDetailViewModel(
            deleteSupplierUseCase = deleteSupplierUseCase,
            getSupplierByIdUseCase = getSupplierByIdUseCase,
            savedStateHandle = savedStateHandle,
        )

        // Act
        viewModel.init()

        // Assert
        assertThat(viewModel.uiState.value.supplierId).isEmpty()
    }

    @Test
    fun `init calls getSupplierId when supplierId is not blank`() = runTest {
        // Arrange
        every { savedStateHandle.get<String>(SUPPLIER_ID) } returns "nonBlankId"
        viewModel = SupplierDetailViewModel(
            deleteSupplierUseCase = deleteSupplierUseCase,
            getSupplierByIdUseCase = getSupplierByIdUseCase,
            savedStateHandle = savedStateHandle,
        )

        coEvery { getSupplierByIdUseCase("nonBlankId") } returns flowOf(
            Result.Success(SupplierEntity(
                companyName = "Test Company",
                item = listOf(
                    Item(
                        id = "id",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("SKU1")
                    )
                ),
                country = "Country",
                state = "State",
                city = "City",
                zipCode = "12345",
                companyLocation = "Location",
                companyPhoneNumber = "1234567890",
                picPhoneNumber = "0987654321",
                picName = "PIC Name",
                picEmail = "pic@example.com",
                status = true,
                modifiedBy = "Modifier",
                createdAt = "2023-01-01",
                updatedAt = "2023-01-02"
            ))
        )

        // Act
        viewModel.init()

        // Assert
        coVerify { getSupplierByIdUseCase("nonBlankId") }
    }

    @Test
    fun `init calls getFilterOption`() = runTest {
        // Act
        viewModel.init()

        // Arrange
        assertThat(viewModel.uiState.value.filterOption.transactionOption).isEqualTo(generatedOptionDataString(getTransaction()))
        assertThat(viewModel.uiState.value.filterOption.picNameOption).isEqualTo(generatedOptionDataString(getPic()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init calls getGroupOption`() = runTest {
        // Act
        viewModel.init()
        advanceUntilIdle()

        // Arrange
        assertThat(viewModel.uiState.value.filterOption.groupOption).isEqualTo(getGroups())
    }



    // GetCallback Test
    @Test
    fun `constructor should return correct object`() = runTest {
        // Arrange
        var onDeleteSupplier = false
        var onResetMessageState = false

        // Act
        val supplierDetailCb = SupplierDetailCallback(
            deleteSupplier = {
                onDeleteSupplier = true
            },
            resetMessageState = {
                onResetMessageState = true
            }
        )

        supplierDetailCb.deleteSupplier(listOf("1"))
        supplierDetailCb.resetMessageState()

        // Assert
        assertThat(onDeleteSupplier).isTrue()
        assertThat(onResetMessageState).isTrue()
    }
}