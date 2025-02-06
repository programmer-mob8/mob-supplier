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
import com.tagsamurai.tscomponents.utils.ExportUtil
import com.tagsamurai.tscomponents.utils.Utils.toDateFormatter
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
class ChangelogViewModel @Inject constructor(
    private val changelogUseCase: GetChangelogUseCase,
    private val changelogOptionUseCase: getChangelogOptionUseCase,
    private val exportUtil: ExportUtil
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChangelogUiState())
    val uiState = _uiState.asStateFlow()

    fun init() {
        initChangelog()
        getFilterOption()
    }

    fun getCallback(): ChangelogListCallback {
        return ChangelogListCallback(
            onRefresh = ::onRefresh,
            onSearch = ::search,
            onFilter = ::updateFilter,
            onUpdateChangelog = ::onUpdateChangelog,
            onResetMessageState = ::resetMessageState
        )
    }

    private fun onRefresh() {
        _uiState.value = _uiState.value.copy(
            filterData = ChangelogFilterData(),
            searchQuery = ""
        )

        init()
    }

    fun initChangelog() {
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

    private fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        initChangelog()
    }

    private fun getFilterOption() {
        _uiState.value = _uiState.value.copy(isLoadingGroup = true)
        changelogOptionUseCase().onEach { result ->
            if (result is Result.Success) {
                _uiState.value = _uiState.value.copy(
                    filterOption = ChangelogFilterOption(
                        actionOption = result.data.actionOption.map {
                            OptionData(
                                it.label,
                                it.value
                            )
                        },
                        fieldOption = result.data.fieldOption.map {
                            OptionData(
                                it.label,
                                it.value
                            )
                        },
                        modifiedBy = result.data.modifiedByOption.map {
                            OptionData(
                                it.label,
                                it.value
                            )
                        }
                    ),
                )
                _uiState.value = _uiState.value.copy(isLoadingGroup = false)
            }
        }.launchIn(viewModelScope)
    }

    private fun updateFilter(data: ChangelogFilterData) {
        _uiState.value = _uiState.value.copy(filterData = data)

        initChangelog()
    }

    private fun onUpdateChangelog(data: ChangelogEntity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val changelogs = _uiState.value.changelog.toMutableList()

            if (data.id.isBlank()) {
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

    private suspend fun parseDownloadContent(
        data: List<ChangelogEntity>
    ): List<Map<String, String>> {
        return data.map { d ->
            with(d) {
                val date = timeStamp.toDateFormatter()

                mapOf(
                    "action" to action,
                    "field" to field,
                    "oldValue" to oldValue,
                    "newValue" to newValue,
                    "modifiedBy" to modifiedBy,
                    "objectName" to objectName,
                    "date" to date
                )
            }
        }
    }

    fun downloadList(fileName: String) {
        _uiState.update {
            it.copy(
                isLoadingOverlay = true,
                downloadState = null
            )
        }

        changelogUseCase(_uiState.value.queryParams).onEach { result ->
            when (result) {
                is Result.Success -> {
                    viewModelScope.launch {
                        val downloadContent = parseDownloadContent(result.data)

                        exportUtil.exportToExcel(
                            fileName, downloadContent
                        )

                        _uiState.update {
                            it.copy(
                                isLoadingOverlay = false,
                                downloadState = true
                            )
                        }
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            downloadState = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun resetMessageState() {
        _uiState.value = _uiState.value.copy(
            downloadState = null
        )
    }
}