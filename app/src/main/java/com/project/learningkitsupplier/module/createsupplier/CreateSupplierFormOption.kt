package com.project.learningkitsupplier.module.createsupplier

import com.tagsamurai.tscomponents.button.OptionData

data class CreateSupplierFormOption(
    val itemName: List<OptionData<String>> = emptyList(),
    val sku: List<OptionData<String>> = emptyList(),
    val country: List<OptionData<String>> = emptyList(),
    val state: List<OptionData<String>> = emptyList(),
    val city: List<OptionData<String>> = emptyList()

)