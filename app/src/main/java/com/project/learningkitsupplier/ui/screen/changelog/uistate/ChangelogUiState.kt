package com.project.learningkitsupplier.ui.screen.changelog.uistate

import com.project.learningkitsupplier.module.changelog.ChangelogFilterData
import com.project.learningkitsupplier.module.changelog.ChangelogFilterOption
import com.project.libs.data.model.ChangelogEntity
import com.project.libs.data.source.network.model.request.GetChangelogQueryParamsRequest
import com.tagsamurai.tscomponents.utils.Utils

data class ChangelogUiState (
   var showSearch: Boolean = false,
   var showDownloadDialog: Boolean = false,
   val searchQuery: String = "",
   val filterOption: ChangelogFilterOption = ChangelogFilterOption(),
   val filterData: ChangelogFilterData = ChangelogFilterData(),
   val changelogDefault: List<ChangelogEntity> = emptyList(),
   val changelog: List<ChangelogEntity> = emptyList(),
   val isLoadingOverlay: Boolean = false,
   val isLoading: Boolean = false
) {
   val queryParams
      get() = GetChangelogQueryParamsRequest(
          search = searchQuery.ifBlank { null },
          date = Utils.toJsonIfNotEmpty(filterData.date),
          action = Utils.toJsonIfNotEmpty(filterData.action),
          field = Utils.toJsonIfNotEmpty(filterData.field),
          modifiedBy = Utils.toJsonIfNotEmpty(filterData.modifiedBy),
      )
}