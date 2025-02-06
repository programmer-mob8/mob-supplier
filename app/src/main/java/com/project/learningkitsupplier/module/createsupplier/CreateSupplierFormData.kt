package com.project.learningkitsupplier.module.createsupplier

data class CreateSupplierFormData(
    val companyName: String = "",
    val items: List<Item> = listOf(Item(id = "1")),
    val country: String? = null,
    val state: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val companyLocation: String? = null,
    val companyPhoneNumber: String? = null,
    val picPhoneNumber: String? = null,
    val picName: String? = null,
    val picEmail: String? = null,

    ) {

    data class Item(
        val id: String = "1",
        val supplierId: String = "",
        val itemName: String = "",
        val sku: List<String> = emptyList(),
        val isSkuEnabled: Boolean = false
    )
}