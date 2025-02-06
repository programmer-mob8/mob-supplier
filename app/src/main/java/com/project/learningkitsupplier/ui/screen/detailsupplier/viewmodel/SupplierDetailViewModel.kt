package com.project.learningkitsupplier.ui.screen.detailsupplier.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailCallback
import com.project.learningkitsupplier.module.supplierdetail.SupplierDetailFilterOption
import com.project.learningkitsupplier.navigation.SUPPLIER_ID
import com.project.learningkitsupplier.ui.screen.detailsupplier.uistate.SupplierDetailUiState
import com.project.learningkitsupplier.util.generatedOptionDataString
import com.project.learningkitsupplier.util.getGroups
import com.project.learningkitsupplier.util.getPic
import com.project.learningkitsupplier.util.getTransaction
import com.project.libs.base.Result
import com.project.libs.data.model.SupplierDetailEntity
import com.project.libs.data.source.network.model.request.DeleteSupplierBodyRequest
import com.project.libs.domain.supplier.DeleteSupplierUseCase
import com.project.libs.domain.supplier.GetSupplierByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierDetailViewModel @Inject constructor(
    private val deleteSupplierUseCase: DeleteSupplierUseCase,
    private val getSupplierByIdUseCase: GetSupplierByIdUseCase,
    savedStateHandle: SavedStateHandle

): ViewModel() {
    private val _uiState = MutableStateFlow(SupplierDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val supplierId: String = savedStateHandle[SUPPLIER_ID] ?: ""

    fun init(){
        _uiState.value = _uiState.value.copy(
            supplierId = "",
        )

        if (supplierId.isNotBlank()){
            getSupplierId(supplierId)
        }

        getFilterOption()
        getGroupOption()
    }
    
    fun getCallback(): SupplierDetailCallback { 
        return SupplierDetailCallback(
            deleteSupplier = ::deleteSupplier,
            resetMessageState = ::resetMessageState,
        )
    }

    private fun getSupplierId (id: String) {
        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

        getSupplierByIdUseCase(id).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            supplierDetailEntity = SupplierDetailEntity(
                                companyName = result.data.companyName,
                                item = result.data.item.map { item ->
                                    SupplierDetailEntity.Item(
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
                                picEmail = result.data.picEmail,
                                status = result.data.status,
                                modifiedBy = result.data.modifiedBy,
                                createdAt = result.data.createdAt,
                                updatedAt = result.data.updatedAt,
                            ),
                            supplierId = id
                        )
                    }
                    _uiState.value = _uiState.value.copy(isLoadingOverlay = false)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoadingOverlay = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    
    private fun deleteSupplier(supplierId: List<String>) {
        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)
        
        val body = DeleteSupplierBodyRequest(
            supplierID = supplierId
        )

        Log.d("TAG", "deleteSupplier: ")
        deleteSupplierUseCase(body).onEach {result ->
            _uiState.update { currData -> 
                currData.copy(
                    isLoadingOverlay = false,
                    deleteState = result is Result.Success
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun getFilterOption(){
        _uiState.value = _uiState.value.copy(
            filterOption = SupplierDetailFilterOption(
                transactionOption = generatedOptionDataString(getTransaction()),
                picNameOption = generatedOptionDataString(getPic())
            )
        )
    }

    private fun getGroupOption() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingGroup = true)
            delay(1000)

            _uiState.update { currData ->
                currData.copy(
                    isLoadingGroup = false,
                    filterOption = currData.filterOption.copy(groupOption = getGroups())
                )
            }
        }
    }
    
    private fun resetMessageState(){
        _uiState.value = _uiState.value.copy(
            deleteState = null
        )
    }
}