package com.project.learningkitsupplier.module.changelog

data class ChangelogFilterData(
    val action: List<String> = emptyList(),
    val date: List<Long> = emptyList(),
    val field: List<String> = emptyList(),
    val modifiedBy: List<String> = emptyList()
)