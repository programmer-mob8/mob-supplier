package com.project.learningkitsupplier.module.changelog

import com.tagsamurai.tscomponents.button.OptionData

data class ChangelogFilterOption(
    val actionOption: List<OptionData<String>> = emptyList(),
    val fieldOption: List<OptionData<String>> = emptyList(),
    val modifiedBy: List<OptionData<String>> = emptyList(),
    val date: List<OptionData<Long>> = emptyList()
)