package com.project.learningkitsupplier.ui.screen.changelog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.learningkitsupplier.module.changelog.ChangelogFilterData
import com.project.learningkitsupplier.module.changelog.ChangelogFilterOption
import com.project.learningkitsupplier.module.changelog.ChangelogListCallback
import com.project.learningkitsupplier.ui.screen.changelog.uistate.ChangelogUiState
import com.project.libs.base.Result
import com.project.libs.data.model.ChangelogEntity
import com.project.libs.domain.supplier.GetChangelogUseCase
import com.project.libs.domain.supplier.getChangelogOptionUseCase
import com.tagsamurai.tscomponents.button.OptionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangelogViewModel @Inject constructor(
    private val changelogUseCase: GetChangelogUseCase,
    private val changelogOptionUseCase: getChangelogOptionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ChangelogUiState())
    val uiState = _uiState.asStateFlow()

    fun init(){
        initChangelog()
        getFilterOption()
    }

    fun getCallback(): ChangelogListCallback {
        return ChangelogListCallback(
            onRefresh = :: onRefresh,
            onSearch = ::search,
            onFilter = ::updateFilter,
            onUpdateChangelog = ::onUpdateChangelog
        )
    }

    private fun onRefresh(){
        _uiState.value = _uiState.value.copy(
            filterData = ChangelogFilterData(),
            searchQuery = ""
        )

        init()
    }

    fun initChangelog(){
        _uiState.value = _uiState.value.copy(isLoading = true)

        changelogUseCase(_uiState.value.queryParams).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        changelogDefault = result.data,
                        changelog = result.data
                    )

                    getFilterOption()
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun search(query: String){
        _uiState.value = _uiState.value.copy(searchQuery = query)

        initChangelog()
    }

    private fun getFilterOption(){
        changelogOptionUseCase().onEach { result ->
            if(result is Result.Success){
                _uiState.value = _uiState.value.copy(
                    filterOption = ChangelogFilterOption(
                        actionOption = result.data.actionOption.map {OptionData(it.label, it.value)},
                        fieldOption = result.data.fieldOption.map {OptionData(it.label, it.value)},
                        modifiedBy = result.data.modifiedByOption.map {OptionData(it.label, it.value)}
                    ),
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateFilter(data: ChangelogFilterData){
        _uiState.value = _uiState.value.copy(filterData = data)

        initChangelog()
    }

    private fun onUpdateChangelog(data: ChangelogEntity){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val changelogs = _uiState.value.changelog.toMutableList()

            if(data.id.isBlank()){
                changelogs.add(index = 0, element = data)
            } else {
                changelogs.indexOfFirst { it.id == data.id }.apply { changelogs[this] = data }
            }

            delay(1000)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                changelog = changelogs,
                changelogDefault = changelogs
            )
        }
    }
}