package com.project.learningkitsupplier.navigation

const val SUPPLIER_ID = "supplierId"

sealed class NavigationRoute(val route: String) {

    data object SupplierListScreen: NavigationRoute(route = "suppler_list_screen")
    data object ChangelogListScreen: NavigationRoute(route = "changelog_list_screen")
    data object CreateSupplierScreen: NavigationRoute(route = "create_supplier")

    data object DetailSupplierScreen: NavigationRoute(route = "detail_supplier_screen/{$SUPPLIER_ID}") {
        fun navigateDetail(supplierId: String): String {
            return "detail_supplier_screen/$supplierId"
        }
    }
    data object EditSupplierScreen: NavigationRoute(route = "edit_supplier/{$SUPPLIER_ID}") {
        fun navigate(supplierId: String): String {
            return "edit_supplier/$supplierId"
        }
    }
}