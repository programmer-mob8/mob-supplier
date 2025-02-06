package com.project.learningkitsupplier.module.createsupplier

data class CreateSupplierFormError(
    val companyName: String? = null,
    val itemName: String? = null,
    val sku: String?  = null,
    val country: String? = null,
    val state: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val companyLocation: String? = null,
    val companyPhoneNumber: String? = null,
    val picName: String? = null,
    val picPhoneNumber: String? = null,
    val picEmail: String? = null
) {
    fun hasError(): Boolean {
        return companyName != null || itemName != null || sku != null || picEmail != null
    }
}