package com.project.learningkitsupplier.module.supplierdetail

import com.tagsamurai.tscomponents.button.OptionData
import com.tagsamurai.tscomponents.model.TreeNode

data class SupplierDetailFilterOption(
    val transactionOption: List<OptionData<String>> = emptyList(),
    val groupOption: List<TreeNode<String>> = emptyList(),
    val picNameOption: List<OptionData<String>> = emptyList()
)