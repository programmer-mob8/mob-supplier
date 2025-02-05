package com.project.learningkitsupplier.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.learningkitsupplier.ui.screen.changelog.view.ChangelogListScreen
import com.project.learningkitsupplier.ui.screen.createsupplierscreen.view.CreateSupplierScreen
import com.project.learningkitsupplier.ui.screen.detailsupplier.view.SupplierDetailScreen
import com.project.learningkitsupplier.ui.screen.supplierlistscreen.view.SupplierListScreen
import com.tagsamurai.tscomponents.snackbar.Snackbar
import com.tagsamurai.tscomponents.snackbar.showSnackbar
import com.tagsamurai.tscomponents.theme.theme

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun showSnackBar(message: String, isSuccess: Boolean){
        snackbarHostState.showSnackbar(scope = scope, message = message, isSuccess = isSuccess)
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),

        snackbarHost = {
            Snackbar(
                snackbarHostState = snackbarHostState
            )
        },
        containerColor = theme.bodyBackground
        ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.SupplierListScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = NavigationRoute.SupplierListScreen.route) {
                SupplierListScreen(
                    navController = navController,
                    onShowSnackBar = { message, isSuccess ->
                        showSnackBar(message, isSuccess)
                    }
                )
            }

            composable(route = NavigationRoute.ChangelogListScreen.route) {
                ChangelogListScreen(
                    onNavigateUp = { navController.navigateUp()}
                )
            }
            
            composable(route = NavigationRoute.CreateSupplierScreen.route) {
                CreateSupplierScreen(
                    onNavigateUp = { navController.navigateUp()},
                    onShowSnackBar = { message, isSuccess ->
                        showSnackBar(message, isSuccess)
                    }
                )
            }

            composable(
                NavigationRoute.DetailSupplierScreen.route,
                arguments = listOf(
                    navArgument(SUPPLIER_ID) {
                        type = NavType.StringType
                    }
                )
            ) {
                SupplierDetailScreen(
                    navController = navController,
                    onShowSnackBar = { message, isSuccess ->
                        showSnackBar(message, isSuccess)
                    },
                    onNavigateUp = { navController.navigateUp() }
                )
            }

            composable(
                route = NavigationRoute.EditSupplierScreen.route,
                arguments = listOf(
                    navArgument(SUPPLIER_ID) {
                        type = NavType.StringType
                    }
                )
            ) {
                CreateSupplierScreen(
                    onNavigateUp = { navController.navigateUp() },
                    onShowSnackBar = {message, isSuccess ->
                        showSnackBar(message, isSuccess)
                    },
                )
            }
        }
    }
}