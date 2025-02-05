package com.project.learningkitsupplier.ui.screen.supplierlistscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.learningkitsupplier.module.supplierlist.SupplierListCallback
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterData
import com.project.learningkitsupplier.module.supplierlist.SupplierListFilterOption
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.uistate.SupplierListUiState
import com.project.libs.base.Result
import com.project.libs.data.model.SupplierEntity
import com.project.libs.data.source.network.model.request.DeleteSupplierBodyRequest
import com.project.libs.data.source.network.model.request.EditSupplierStatusBodyRequest
import com.project.libs.domain.supplier.DeleteSupplierUseCase
import com.project.libs.domain.supplier.EditSupplierStatusUseCase
import com.project.libs.domain.supplier.GetFilterListOptionUseCase
import com.project.libs.domain.supplier.SupplierUseCase
import com.tagsamurai.tscomponents.button.OptionData
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
class SupplierListViewModel @Inject constructor(
    private val supplierUseCase: SupplierUseCase,
    private val getFilterListOptionUseCase: GetFilterListOptionUseCase,
    private val editSupplierStatusUseCase: EditSupplierStatusUseCase,
    private val deleteSupplierUseCase: DeleteSupplierUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SupplierListUiState())
    val uiState = _uiState.asStateFlow()

    fun init() {
        initSupplier()
        getFilterOption()
    }

    fun getCallback(): SupplierListCallback {
        return SupplierListCallback(
            onRefresh = ::onRefresh,
            onSearch = ::search,
            onFilter = ::updateFilter,
            onToggleSelectAll = ::toggleSelectAll,
            onUpdateSelectedItem = ::updateItemSelected,
            onStatusUpdate = ::updateStatusSupplier,
            onUpdateSupplier = ::onUpdateSuppliers,
            onClearSelectedItem = ::clearSelectedItems,
            deleteSupplier = ::deleteSupplier,
            onResetMessageState = ::resetMessageState
        )
    }

    private fun onRefresh() {
        _uiState.value = _uiState.value.copy(
            filterData = SupplierListFilterData(),
            searchQuery = ""
        )

        init()
    }

    fun initSupplier() {

        _uiState.value = _uiState.value.copy(isLoading = true)

        supplierUseCase(_uiState.value.queryParams).onEach { result ->
            when (result) {
                is Result.Success -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        supplierDefault = result.data,
                        supplier = result.data
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }

            }
        }.launchIn(viewModelScope)
    }

    private fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        initSupplier()
    }

    private fun toggleSelectAll() {
        _uiState.update { currData ->
            currData.copy(
                itemSelected = if (currData.isAllSelected) {
                    emptyList()
                } else {
                    currData.supplier
                },
                isAllSelected = !currData.isAllSelected
            )
        }
    }

    private fun updateItemSelected(supplier: SupplierEntity) {
        val selectedItems = _uiState.value.itemSelected.toMutableList()
        _uiState.value = _uiState.value.copy(
            itemSelected = if (selectedItems.contains(supplier)) {
                selectedItems.apply {
                    remove(supplier)
                }
            } else {
                selectedItems.apply { add(supplier) }
            }
        )
    }

    private fun getFilterOption(){
        getFilterListOptionUseCase().onEach { result ->
            if (result is Result.Success){
                _uiState.value = _uiState.value.copy(
                    filterOption = SupplierListFilterOption(
                        companyNameOption = result.data.supplierOption.map { OptionData(it.label, it.value)},
                        cityOption = result.data.cityOption.map{ OptionData(it.label, it.value)},
                        statusOption = result.data.statusOption.map{ OptionData(it.label, it.value)},
                        itemNameOption = result.data.itemNameOption.map { OptionData(it.label, it.value )},
                        modifiedByOption = result.data.modifiedByOption.map { OptionData(it.label, it.value )},
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateFilter(data: SupplierListFilterData){
        _uiState.value = _uiState.value.copy(filterData = data)

        initSupplier()
    }

    private fun onUpdateSuppliers(data: SupplierEntity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val suppliers = _uiState.value.supplier.toMutableList()

            if (data.id.isBlank()){
                suppliers.add(index = 0, element = data)
            } else {
                suppliers.indexOfFirst { it.id == data.id }.apply { suppliers[this] = data }
            }

            delay(2000)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                supplier = suppliers,
                supplierDefault = suppliers,
            )
        }
    }

    private fun updateStatusSupplier(supplierId: List<String>, newStatus: Boolean) {
        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

        val body = EditSupplierStatusBodyRequest(supplierId, newStatus)
        editSupplierStatusUseCase(body).onEach { result ->
            when(result){
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingOverlay = false,
                        isActive = newStatus,
                        statusState = true,
                        supplier = _uiState.value.supplier.map {
                            if(supplierId.contains(it.id)) it.copy(status = newStatus) else it
                        }
                    )

                    getFilterOption()
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingOverlay = false,
                        statusState = false,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun clearSelectedItems(){
        _uiState.value = _uiState.value.copy(itemSelected = emptyList())
    }

    private fun deleteSupplier(supplierIds: List<String>) {
        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

        val body = DeleteSupplierBodyRequest(
            supplierID = supplierIds
        )

        val domain = deleteSupplierUseCase(body)

        domain.onEach { result ->

            if (result is Result.Success) {
                delay(2000)
                initSupplier()
            }

            _uiState.update { currData ->
                currData.copy(
                    isLoadingOverlay = false,
                    deleteState = result is Result.Success
                )
            }

            getFilterOption()

        }.launchIn(viewModelScope)
    }

    private fun resetMessageState() {
        _uiState.value = _uiState.value.copy(
            deleteState = null,
            statusState = null
        )
    }
}
