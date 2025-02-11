package com.project.learningkitsupplier.screen.createsupplier.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.project.learningkitsupplier.MainDispatcherRule
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierCallback
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormData
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormError
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormOption
import com.project.learningkitsupplier.navigation.SUPPLIER_ID
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.uistate.CreateSupplierUiState
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.viewmodel.CreateSupplierViewModel
import com.project.learningkitsupplier.util.generatedOptionDataString
import com.project.learningkitsupplier.util.getCity
import com.project.learningkitsupplier.util.getCountry
import com.project.learningkitsupplier.util.getItemName
import com.project.learningkitsupplier.util.getSku
import com.project.learningkitsupplier.util.getState
import com.project.libs.base.Result
import com.project.libs.data.model.Item
import com.project.libs.data.model.SupplierEntity
import com.project.libs.data.source.network.model.request.CreateEditSupplierBodyRequest
import com.project.libs.domain.supplier.CreateSupplierUseCase
import com.project.libs.domain.supplier.EditSupplierUseCase
import com.project.libs.domain.supplier.GetSupplierByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateSupplierViewModelTest {
    private lateinit var createSupplierViewModel: CreateSupplierViewModel

    private lateinit var callback: CreateSupplierCallback

    private val supplierId = "1"

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var createSupplierUseCase: CreateSupplierUseCase

    @MockK
    private lateinit var editSupplierUseCase: EditSupplierUseCase

    @MockK
    private lateinit var getSupplierByIdUseCase: GetSupplierByIdUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { savedStateHandle.get<String>(SUPPLIER_ID) } returns supplierId

        createSupplierViewModel = CreateSupplierViewModel(
            createSupplierUseCase = createSupplierUseCase,
            editSupplierUseCase = editSupplierUseCase,
            getSupplierByIdUseCase = getSupplierByIdUseCase,
            savedStateHandle = savedStateHandle
        )

        callback = createSupplierViewModel.getCallback()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun arrangeCreateSupplierUseCase(
        body: CreateEditSupplierBodyRequest,
        result: Result<Unit>
    ) {
        coEvery {
            createSupplierUseCase(body)
        } returns flowOf(result)
    }

    private fun arrangeEditSupplierUseCase(
        body: CreateEditSupplierBodyRequest,
        result: Result<Unit>
    ) {
        coEvery {
            editSupplierUseCase(supplierId, body)
        } returns flowOf(result)
    }

    private fun arrangeGetSupplierByIdUseCase(
        result: Result<SupplierEntity>
    ) {
        coEvery {
            getSupplierByIdUseCase(supplierId)
        } returns flowOf(result)
    }

    // SubmitForm Test
    @Test
    fun `submitForm should set isLoadingOverlay to false when form is valid`() = runTest {
        // Arrange
        val body = CreateEditSupplierBodyRequest(
            companyName = "Test Company",
            item = listOf(
                CreateEditSupplierBodyRequest.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        val formData = CreateSupplierFormData(
            companyName = "Test Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        arrangeCreateSupplierUseCase(body, Result.Success(Unit))

        callback.onUpdateFormData(formData)

        // Act
        callback.onSubmitForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isLoadingOverlay).isFalse()
    }

    @Test
    fun `submitForm should not set isLoadingOverlay when form is invalid`() = runTest {
        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "", sku = listOf(""))
            )
        )
            callback.onUpdateFormData(formData)

        // Act
        callback.onSubmitForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isLoadingOverlay).isFalse()
    }

    @Test
    fun `submitForm should call createSupplierUseCase when isEditForm is false`() = runTest {
        // Arrange
        val body = CreateEditSupplierBodyRequest(
            companyName = "Test Company",
            item = listOf(
                CreateEditSupplierBodyRequest.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        val formData = CreateSupplierFormData(
            companyName = "Test Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        arrangeCreateSupplierUseCase(body, Result.Success(Unit))

        // Act
        callback.onUpdateFormData(formData)
        callback.onSubmitForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isLoadingOverlay).isFalse()
        assertThat(createSupplierViewModel.uiState.value.submitState).isTrue()
        coVerify { createSupplierUseCase(body) }
    }

    @Test
    fun `submitForm should call editSupplierUseCase when isEditForm is true`() = runTest {
        // Arrange
        val body = CreateEditSupplierBodyRequest(
            companyName = "Test Company",
            item = listOf(
                CreateEditSupplierBodyRequest.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        val formData = CreateSupplierFormData(
            companyName = "Test Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )
        arrangeCreateSupplierUseCase(body, Result.Success(Unit))
        arrangeEditSupplierUseCase(body, Result.Success(Unit))
        arrangeGetSupplierByIdUseCase(Result.Success(SupplierEntity()))

        // Act
        createSupplierViewModel.init()
        callback.onUpdateFormData(formData)
        callback.onSubmitForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isEditForm).isTrue()
        assertThat(createSupplierViewModel.uiState.value.supplierId).isEqualTo(supplierId)
        coVerify { editSupplierUseCase(supplierId, body) }
    }

    @Test
    fun `submitForm should set submitState to true on success`() = runTest {
        // Arrange
        val body = CreateEditSupplierBodyRequest(
            companyName = "Test Company",
            item = listOf(
                CreateEditSupplierBodyRequest.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        val formData = CreateSupplierFormData(
            companyName = "Test Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        arrangeCreateSupplierUseCase(body, Result.Success(Unit))

        // Act
        callback.onUpdateFormData(formData)
        callback.onSubmitForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.submitState).isTrue()
    }

    @Test
    fun `submitForm should set submitState to false on error`() = runTest {
        // Arrange
        val body = CreateEditSupplierBodyRequest(
            companyName = "Test Company",
            item = listOf(
                CreateEditSupplierBodyRequest.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        val formData = CreateSupplierFormData(
            companyName = "Test Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        arrangeCreateSupplierUseCase(body, Result.Error("Error"))

        // Act
        callback.onUpdateFormData(formData)
        callback.onSubmitForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.submitState).isFalse()
    }

    @Test
    fun `submitForm should clear fields on success when isStayOnForm is true`() = runTest {
        // Arrange
        val body = CreateEditSupplierBodyRequest(
            companyName = "Test Company",
            item = listOf(
                CreateEditSupplierBodyRequest.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        val formData = CreateSupplierFormData(
            companyName = "Test Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))
            )
        )

        val expected = CreateSupplierUiState(
            isStayOnForm = true,
            submitState = true,
        )

        arrangeCreateSupplierUseCase(body, Result.Success(Unit))

        // Act
        callback.onUpdateStayOnForm()
        callback.onUpdateFormData(formData)
        callback.onSubmitForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value).isEqualTo(expected)
        assertThat(createSupplierViewModel.uiState.value.formData).isEqualTo(CreateSupplierFormData())
    }


    // GeSupplierId Test
    @Test
    fun `get supplier by id success updates uiState correctly`() = runTest {
        // Arrange
        val supplierEntity = SupplierEntity(
                companyName = "Test Company",
                item = listOf(
                    Item
                        (id = "1", supplierId = "1", itemName = "Item", sku = listOf("SKU1"))
                ),
                country = "Indonesia",
                state = "DKI Jakarta",
                city = "Jakarta",
                zipCode = "12345",
                companyLocation = "Jl. Test",
                companyPhoneNumber = "08123456789",
                picPhoneNumber = "08123456789",
                picName = "Test",
                picEmail = "picEmail",
                status = false,
                modifiedBy = "Test",
                createdAt = "2021-01-01",
                updatedAt = "2021-01-01"
            )


        val result = Result.Success(supplierEntity)

        arrangeGetSupplierByIdUseCase(result)

        // Act
        createSupplierViewModel.getSupplierId(supplierId)

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isLoadingOverlay).isFalse()
        assertThat(createSupplierViewModel.uiState.value.formData).isEqualTo(
            CreateSupplierFormData(
                companyName = "Test Company",
                items = listOf(
                    CreateSupplierFormData.Item(
                        id = "1",
                        supplierId = "1",
                        itemName = "Item",
                        sku = listOf("SKU1")
                    )
                ),
                country = "Indonesia",
                state = "DKI Jakarta",
                city = "Jakarta",
                zipCode = "12345",
                companyLocation = "Jl. Test",
                companyPhoneNumber = "08123456789",
                picPhoneNumber = "08123456789",
                picName = "Test",
                picEmail = "picEmail",
            )
        )
    }

    @Test
    fun `get supplier by id error updates uiState correctly`() = runTest {
        // Arrange
        arrangeGetSupplierByIdUseCase(Result.Error("Error"))

        // Act
        createSupplierViewModel.getSupplierId(supplierId)

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isLoadingOverlay).isFalse()
        assertThat(createSupplierViewModel.uiState.value.formData).isEqualTo(CreateSupplierFormData())
    }



    // ResetMessage Test
    @Test
    fun `resetMessage should set submitState to null`() = runTest {
        // Arrange
        val expected = CreateSupplierUiState(
            submitState = null
        )

        // Act
        callback.onResetMessageState

        // Assert
        assertThat(createSupplierViewModel.uiState.value).isEqualTo(expected)
    }

    @Test
    fun `reset message state does not affect other uiState properties`() = runTest {
        // Arrange
        val initialUiState = createSupplierViewModel.uiState.value

        // Act
        callback.onResetMessageState

        // Assert
        assertThat(createSupplierViewModel.uiState.value.copy(submitState = initialUiState.submitState)).isEqualTo(initialUiState)
    }


    // FormValidation Test
    @Test
    fun `formValidation should return true when form data is valid`() = runTest {
        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "Valid Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Valid Item", sku = listOf("Valid SKU"))
            ),
            picEmail = "valid.email@example.com"
        )

        // Act
        val result = createSupplierViewModel.formValidation(formData)

        // Assert
        assertThat(result).isTrue()
        assertThat(createSupplierViewModel.uiState.value.formError.hasError()).isFalse()
    }

    @Test
    fun `formValidation should return false when company name is empty`() = runTest {
        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Valid Item", sku = listOf("Valid SKU"))
            ),
            picEmail = "valid.email@example.com"
        )

        // Act
        val result = createSupplierViewModel.formValidation(formData)

        // Assert
        assertThat(result).isFalse()
        assertThat(createSupplierViewModel.uiState.value.formError.companyName).isEqualTo("Company name must not be empty")
    }

    @Test
    fun `formValidation should return false when item name is empty`() = runTest {
        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "Valid Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "", sku = listOf("Valid SKU"))
            ),
            picEmail = "valid.email@example.com"
        )

        // Act
        val result = createSupplierViewModel.formValidation(formData)

        // Assert
        assertThat(result).isFalse()
        assertThat(createSupplierViewModel.uiState.value.formError.itemName).isEqualTo("You must pick an item name")
    }

    @Test
    fun `formValidation should return false when SKU is empty`() = runTest {

        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "Valid Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Valid Item", sku = listOf())
            ),
            picEmail = "valid.email@example.com"
        )

        // Act
        val result = createSupplierViewModel.formValidation(formData)

        // Assert
        assertThat(result).isFalse()
        assertThat(createSupplierViewModel.uiState.value.formError.sku).isEqualTo("You must pick a SKU")
    }

    @Test
    fun `formValidation should return false when email format is incorrect`() = runTest {

        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "Valid Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Valid Item", sku = listOf("Valid SKU"))
            ),
            picEmail = "invalid-email"
        )

        // Act
        val result = createSupplierViewModel.formValidation(formData)

        // Assert
        assertThat(result).isFalse()
        assertThat(createSupplierViewModel.uiState.value.formError.picEmail).isEqualTo("Email format is incorrect")
    }



    // UpdateFormData Test
    @Test
    fun `updateFormData should update formData and clear formError`() = runTest {

        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "Updated Company",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "Updated Item", sku = listOf("Updated SKU"))
            )
        )

        // Act
        callback.onUpdateFormData(formData)

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData).isEqualTo(formData)
        assertThat(createSupplierViewModel.uiState.value.formError).isEqualTo(CreateSupplierFormError())
    }

    @Test
    fun `updateFormData should handle empty formData`() = runTest {

        // Arrange
        val formData = CreateSupplierFormData()

        // Act
        callback.onUpdateFormData(formData)

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData).isEqualTo(formData)
        assertThat(createSupplierViewModel.uiState.value.formError).isEqualTo(CreateSupplierFormError())
    }

    @Test
    fun `updateFormData should handle null values in formData`() = runTest {

        // Arrange
        val formData = CreateSupplierFormData(
            companyName = "",
            items = listOf(
                CreateSupplierFormData.Item(itemName = "", sku = listOf(""))
            ),
            picEmail = null
        )

        // Act
        callback.onUpdateFormData(formData)

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData).isEqualTo(formData)
        assertThat(createSupplierViewModel.uiState.value.formError).isEqualTo(CreateSupplierFormError())
    }



    // UpdateAddSupplierItems Test
    @Test
    fun `updateAddSupplierItems should update the item in formData`() = runTest {
        // Arrange
        val initialItems = listOf(
            CreateSupplierFormData.Item(id = "1", itemName = "Item 1", sku = listOf("SKU1")),
            CreateSupplierFormData.Item(id = "2", itemName = "Item 2", sku = listOf("SKU2"))
        )
        val updatedItem = CreateSupplierFormData.Item(id = "1", itemName = "Updated Item", sku = listOf("Updated SKU"))
        callback.onUpdateFormData(CreateSupplierFormData(items = initialItems))
        callback.onUpdateListSuppliedItem(updatedItem)

        // Act
        val expectedItems = listOf(
            updatedItem,
            CreateSupplierFormData.Item(id = "2", itemName = "Item 2", sku = listOf("SKU2"))
        )

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData.items).isEqualTo(expectedItems)
    }

    @Test
    fun `updateAddSupplierItems should not change items if id does not match`() = runTest {
        // Arrange
        val initialItems = listOf(
            CreateSupplierFormData.Item(id = "1", itemName = "Item 1", sku = listOf("SKU1")),
            CreateSupplierFormData.Item(id = "2", itemName = "Item 2", sku = listOf("SKU2"))
        )
        val nonMatchingItem = CreateSupplierFormData.Item(id = "3", itemName = "Non-matching Item", sku = listOf("Non-matching SKU"))
        // Act
        callback.onUpdateFormData(CreateSupplierFormData(items = initialItems))
        callback.onUpdateListSuppliedItem(nonMatchingItem)

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData.items).isEqualTo(initialItems)
    }



    // GetFormOption Test
    @Test
    fun `getFormOption should update formOption with generated data`() = runTest {
        // Arrange
        val expectedFormOption = CreateSupplierFormOption(
            itemName = generatedOptionDataString(getItemName()),
            sku = generatedOptionDataString(getSku()),
            country = generatedOptionDataString(getCountry()),
            state = generatedOptionDataString(getState()),
            city = generatedOptionDataString(getCity())
        )

        // Act
        createSupplierViewModel.getFormOption()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formOption).isEqualTo(expectedFormOption)
    }



    // UpdateStayOnForm Test
    @Test
    fun `updateStayOnForm should toggle isStayOnForm from false to true`() = runTest {
        // Act
        callback.onUpdateStayOnForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isStayOnForm).isTrue()
    }

    @Test
    fun `updateStayOnForm should toggle isStayOnForm from true to false`() = runTest {
        // Act
        callback.onUpdateStayOnForm()
        callback.onUpdateStayOnForm()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isStayOnForm).isFalse()
    }



    // AddSupplierButton Test
    @Test
    fun `addSupplierButton should add a new item to formData`() = runTest {
        // Arrange
        val initialItems = listOf(
            CreateSupplierFormData.Item(id = "1", itemName = "Item 1", sku = listOf("SKU1"))
        )

        // Act
        callback.onUpdateFormData(CreateSupplierFormData(items = initialItems))
        callback.addSupplierButton()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData.items.size).isEqualTo(2)
        assertThat(createSupplierViewModel.uiState.value.formData.items[1].id).isNotEmpty()
    }

    @Test
    fun `addSupplierButton should add a new item with unique id`() = runTest {
        // Arrange
        val initialItems = listOf(
            CreateSupplierFormData.Item(id = "1", itemName = "Item 1", sku = listOf("SKU1"))
        )

        // Act
        callback.onUpdateFormData(CreateSupplierFormData(items = initialItems))
        callback.addSupplierButton()
        callback.addSupplierButton()

        // Assert
        val ids = createSupplierViewModel.uiState.value.formData.items.map { it.id }
        assertThat(ids.distinct().size).isEqualTo(3)
    }



    // OnRemoveSupplierButton Test
    @Test
    fun `onRemoveSupplierButton should remove the item from formData`() = runTest {
        // Arrange
        val initialItems = listOf(
            CreateSupplierFormData.Item(id = "1", itemName = "Item 1", sku = listOf("SKU1")),
            CreateSupplierFormData.Item(id = "2", itemName = "Item 2", sku = listOf("SKU2"))
        )

        val expectedItems = listOf(
            CreateSupplierFormData.Item(id = "2", itemName = "Item 2", sku = listOf("SKU2"))
        )

        // Act
        callback.onUpdateFormData(CreateSupplierFormData(items = initialItems))
        callback.removeSupplierButton(CreateSupplierFormData.Item(id = "1"))


        assertThat(createSupplierViewModel.uiState.value.formData.items).isEqualTo(expectedItems)
    }

    @Test
    fun `onRemoveSupplierButton should not remove the item if only one item exists`() = runTest {

        // Arrange
        val initialItems = listOf(
            CreateSupplierFormData.Item(id = "1", itemName = "Item 1", sku = listOf("SKU1"))
        )

        // Act
        callback.onUpdateFormData(CreateSupplierFormData(items = initialItems))
        callback.removeSupplierButton(CreateSupplierFormData.Item(id = "1"))

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData.items).isEqualTo(initialItems)
    }

    @Test
    fun `onRemoveSupplierButton should not remove the item if id does not match`() = runTest {

        // Arrange
        val initialItems = listOf(
            CreateSupplierFormData.Item(id = "1", itemName = "Item 1", sku = listOf("SKU1")),
            CreateSupplierFormData.Item(id = "2", itemName = "Item 2", sku = listOf("SKU2"))
        )

        // Act
        callback.onUpdateFormData(CreateSupplierFormData(items = initialItems))
        callback.removeSupplierButton(CreateSupplierFormData.Item(id = "3"))

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formData.items).isEqualTo(initialItems)
    }



    // ClearField Test
    @Test
    fun `clearField should reset formData and formError`() = runTest {
        val initialFormData = CreateSupplierFormData(
            companyName = "Test Company",
            items = listOf(CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))),
            picEmail = "test@example.com"
        )

        val expected = CreateSupplierUiState(
            formData = CreateSupplierFormData(),
        )

        callback.onUpdateFormData(initialFormData)


        createSupplierViewModel.clearField()

        assertThat(createSupplierViewModel.uiState.value).isEqualTo(expected)
        assertThat(createSupplierViewModel.uiState.value.formError).isEqualTo(CreateSupplierFormError())
    }



    // Init Test
    @Test
    fun `init should set isEditForm to true when supplierId is not blank`() = runTest {
        // Arrange
        every { savedStateHandle.get<String>(SUPPLIER_ID) } returns "123"

        arrangeGetSupplierByIdUseCase(Result.Success(SupplierEntity()))
        // Act
        createSupplierViewModel.init()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isEditForm).isTrue()
    }

    @Test
    fun `init should set isEditForm to false when supplierId is blank`() = runTest {
        every { savedStateHandle.get<String>(SUPPLIER_ID) } returns ""
        createSupplierViewModel = CreateSupplierViewModel(
            createSupplierUseCase = createSupplierUseCase,
            editSupplierUseCase = editSupplierUseCase,
            getSupplierByIdUseCase = getSupplierByIdUseCase,
            savedStateHandle = savedStateHandle
        )

        // Act
        createSupplierViewModel.init()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.isEditForm).isFalse()
    }

    @Test
    fun `init should call getFormOption`() = runTest {
        // Arrange
        arrangeGetSupplierByIdUseCase(Result.Success(SupplierEntity()))

        // Act
        createSupplierViewModel.init()

        // Assert
        assertThat(createSupplierViewModel.uiState.value.formOption).isNotNull()
    }



    // Callback Test
    @Test
    fun `getCallback should return correct object`() = runTest {
        // Arrange
        var onResetMessageState = false
        var onSubmitForm = false
        var onUpdateFormData = false
        var onUpdateStayOnForm = false
        var onUpdateListSuppliedItem = false
        var addSupplierButton = false
        var removeSupplierButton = false

        // Act
        val createSupplierCb = CreateSupplierCallback(
            onResetMessageState = { onResetMessageState = true },
            onSubmitForm = { onSubmitForm = true },
            onUpdateFormData = { onUpdateFormData = true },
            onUpdateStayOnForm = { onUpdateStayOnForm = true },
            onUpdateListSuppliedItem = { onUpdateListSuppliedItem = true },
            addSupplierButton = { addSupplierButton = true },
            removeSupplierButton = { removeSupplierButton = true }
        )

        createSupplierCb.onResetMessageState()
        createSupplierCb.onSubmitForm()
        callback.onUpdateFormData(CreateSupplierFormData())
        createSupplierCb.onUpdateFormData(
            CreateSupplierFormData(
                companyName = "Test Company",
                items = listOf(
                    CreateSupplierFormData.Item(itemName = "Item", sku = listOf("SKU1"))
                )
            )
        )
        createSupplierCb.onUpdateStayOnForm()
        createSupplierCb.onUpdateListSuppliedItem(CreateSupplierFormData.Item())
        createSupplierCb.addSupplierButton()
        createSupplierCb.removeSupplierButton(CreateSupplierFormData.Item())

        // Assert
        Truth.assertThat(onResetMessageState).isTrue()
        Truth.assertThat(onSubmitForm).isTrue()
        Truth.assertThat(onUpdateFormData).isTrue()
        Truth.assertThat(onUpdateStayOnForm).isTrue()
        Truth.assertThat(onUpdateListSuppliedItem).isTrue()
        Truth.assertThat(addSupplierButton).isTrue()
        Truth.assertThat(removeSupplierButton).isTrue()
    }
}