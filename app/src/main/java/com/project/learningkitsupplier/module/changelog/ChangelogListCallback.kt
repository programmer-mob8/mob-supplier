package com.project.learningkitsupplier.module.changelog

import com.project.libs.data.model.ChangelogEntity

data class ChangelogListCallback(
    val onRefresh: () -> Unit,
    val onSearch: (String) -> Unit,
    val onFilter: (ChangelogFilterData) -> Unit = {},
    val onUpdateChangelog: (ChangelogEntity) -> Unit = {}
)