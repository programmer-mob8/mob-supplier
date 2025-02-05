package com.project.learningkitsupplier.module.supplierlist

import com.tagsamurai.tscomponents.button.OptionData


data class SupplierListFilterOption(
    val cityOption: List<OptionData<String>> = emptyList(),
    val companyNameOption: List<OptionData<String>> = emptyList(),
    val itemNameOption: List<OptionData<String>> = emptyList(),
    val statusOption: List<OptionData<Boolean>> = emptyList(),
    val modifiedByOption: List<OptionData<String>> = emptyList(),
    val lastModifiedOption: List<OptionData<Long>> = emptyList()
)