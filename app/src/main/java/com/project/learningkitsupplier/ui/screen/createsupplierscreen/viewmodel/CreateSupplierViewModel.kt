package com.project.learningkitsupplier.ui.screen.createsupplierscreen.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierCallback
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormData
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormError
import com.project.learningkitsupplier.module.createsupplier.CreateSupplierFormOption
import com.project.learningkitsupplier.navigation.SUPPLIER_ID
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.uistate.CreateSupplierUiState
import com.project.learningkitsupplier.util.generatedOptionDataString
import com.project.learningkitsupplier.util.getCity
import com.project.learningkitsupplier.util.getCountry
import com.project.learningkitsupplier.util.getItemName
import com.project.learningkitsupplier.util.getSku
import com.project.learningkitsupplier.util.getState
import com.project.libs.base.Result
import com.project.libs.data.source.network.model.request.CreateEditSupplierBodyRequest
import com.project.libs.domain.supplier.CreateSupplierUseCase
import com.project.libs.domain.supplier.EditSupplierUseCase
import com.project.libs.domain.supplier.GetSupplierByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateSupplierViewModel @Inject constructor(
    private val createSupplierUseCase: CreateSupplierUseCase,
    private val editSupplierUseCase: EditSupplierUseCase,
    private val getSupplierByIdUseCase: GetSupplierByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateSupplierUiState())
    val uiState = _uiState.asStateFlow()

    private val supplierId: String = savedStateHandle[SUPPLIER_ID] ?: ""

    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun init(){
        _uiState.value = _uiState.value.copy(
            supplierId = "",
            submitState = null,
            isEditForm = supplierId.isNotBlank()
        )

        if(supplierId.isNotBlank()) {
            getSupplierId(supplierId)
        }
        getFormOption()


    }

    private fun getSupplierId(id: String) {
        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

        getSupplierByIdUseCase(id).onEach { result ->
            when(result) {
                is Result.Success -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            formData = CreateSupplierFormData(
                                companyName = result.data.companyName,
                                items = result.data.item.map { item ->
                                    CreateSupplierFormData.Item(
                                        id = item.id,
                                        supplierId = item.supplierId,
                                        itemName = item.itemName,
                                        sku = item.sku
                                    )
                                },
                                country = result.data.country,
                                state = result.data.state,
                                city = result.data.city,
                                zipCode = result.data.zipCode,
                                companyLocation = result.data.companyLocation,
                                companyPhoneNumber = result.data.companyPhoneNumber,
                                picPhoneNumber = result.data.picPhoneNumber,
                                picName = result.data.picName,
                                picEmail = result.data.picEmail
                            ),
                            supplierId = id,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoadingOverlay = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCallback(): CreateSupplierCallback {
        return CreateSupplierCallback(
            onResetMessageState = ::resetMessageState,
            onSubmitForm = ::submitForm,
            onUpdateFormData = ::updateFormData,
            onUpdateStayOnForm = ::updateStayOnForm,
            onUpdateListSuppliedItem = ::updateAddSupplierItems,
            addSupplierButton = ::addSupplierButton,
            removeSupplierButton = ::onRemoveSupplierButton
        )
    }

    private fun resetMessageState() {
        _uiState.value = _uiState.value.copy(
            submitState = null
        )
    }

    private fun formValidation(data: CreateSupplierFormData): Boolean {
        var formError = CreateSupplierFormError()

        if (data.companyName.isEmpty()) {
            formError = formError.copy(companyName = "Company name must not be empty")
        }

        if (data.companyName.length > 30 ==  true){
            formError = formError.copy(companyName = "Max. 30 characters")
        }

        if (data.items.any {it.itemName.isEmpty()}){
            formError = formError.copy(itemName = "You must pick an item name")
        }

        if (data.items.any {it.sku.isEmpty()}){
            formError = formError.copy(sku = "You must pick a SKU")
        }

        if (data.zipCode?.let { it.length > 15 } == true) {
            formError = formError.copy(zipCode = "Max. 15 characters")
        }

        if (data.companyLocation?.let { it.length > 120 } == true) {
            formError = formError.copy(companyLocation = "Max. 120 characters")
        }


        if (data.picPhoneNumber?.let { it.length > 15 } == true) {
            formError = formError.copy(picPhoneNumber = "Max. 15 characters")
        }


        if (data.companyPhoneNumber?.let { it.length > 15 } == true) {
            formError = formError.copy(companyPhoneNumber = "Max. 15 characters")
        }

        if (data.picName?.let { it.length > 30 } == true) {
            formError = formError.copy(picName = "Max. 30 characters")
        }

        if (data.picEmail?.let { !emailPattern.toRegex().containsMatchIn(it) } == true) {
            formError = formError.copy(picEmail = "Email format is incorrect")
        }

        _uiState.value = _uiState.value.copy(formError = formError)
        return !formError.hasError()
    }

    private fun submitForm() {
        val data = _uiState.value.formData

        if (!formValidation(data)) return

        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

        val body = CreateEditSupplierBodyRequest(
            companyName = data.companyName,
            item = data.items.map { item ->
                CreateEditSupplierBodyRequest.Item(
                    itemName = item.itemName,
                    sku = item.sku
                )
            },
            country = data.country,
            state = data.state,
            city = data.city,
            zipCode = data.zipCode,
            companyLocation = data.companyLocation,
            companyPhoneNumber = data.companyPhoneNumber,
            picName = data.picName,
            picPhoneNumber = data.picPhoneNumber,
            picEmail = data.picEmail
        )

        Log.d("CreateSupplierViewModel", "data ${body.city} <> ${data.city}")

        val domain = if(_uiState.value.isEditForm) {
            editSupplierUseCase(_uiState.value.supplierId, body)
        } else {
            createSupplierUseCase(body)
        }

        domain.onEach { result ->
            _uiState.update { currData ->
                currData.copy(
                    isLoadingOverlay = false,
                    submitState = result is Result.Success
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateFormData(formData: CreateSupplierFormData) {
        _uiState.value = _uiState.value.copy(
            formData = formData,
            formError = CreateSupplierFormError()
        )
    }

    fun updateAddSupplierItems(item: CreateSupplierFormData.Item){
        val list = _uiState.value.formData.items
        _uiState.update { currData ->
            currData.copy(
                formData = currData.formData.copy(
                    items = list.map {
                        if (it.id == item.id) {
                            item
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }

    private fun getFormOption(){
        _uiState.value = _uiState.value.copy(
            formOption = CreateSupplierFormOption(
                itemName = generatedOptionDataString(getItemName()),
                sku = generatedOptionDataString(getSku()),
                country = generatedOptionDataString(getCountry()),
                state = generatedOptionDataString(getState()),
                city = generatedOptionDataString(getCity())
            )
        )
    }
    
    private fun updateStayOnForm(){
        _uiState.update { currData -> 
            currData.copy(isStayOnForm = !currData.isStayOnForm)
        }
    }

    private fun addSupplierButton(){
        _uiState.update { currData ->
            currData.copy(
                formData = currData.formData.copy(
                    items = currData.formData.items.plus(CreateSupplierFormData.Item(id = "${currData.formData.items.size + 1}"))
                )
            )
        }
    }

    private fun onRemoveSupplierButton(item: CreateSupplierFormData.Item){
        val list = uiState.value.formData.items

        _uiState.value = _uiState.value.copy(
            formData = _uiState.value.formData.copy(
                items = list.filter { it.id != item.id }
            )
        )
    }
}