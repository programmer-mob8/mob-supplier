package com.project.learningkitsupplier.module.supplierdetail

import com.tagsamurai.tscomponents.model.TreeNode

data class SupplierDetailFilterData(
    val transactionSelected: List<String> = emptyList(),
    val picSelected: List<String> = emptyList(),
    val groupSelected: List<TreeNode<String>> = emptyList()
)