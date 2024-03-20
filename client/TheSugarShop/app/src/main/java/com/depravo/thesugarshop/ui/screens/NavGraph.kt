package com.depravo.thesugarshop.ui.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.depravo.thesugarshop.util.NavigationRoutes


@Composable
fun NavGraph(navHostController: NavHostController, context: Context) {
    NavHost(
        navController = navHostController,
        startDestination = NavigationRoutes.Unauthenticated.NavigationRoute.route
    ) {
        unauthenticatedGraph(navController = navHostController, context)

        authenticatedGraph(navController = navHostController, context)
    }
}

fun NavGraphBuilder.unauthenticatedGraph(navController: NavHostController, context: Context) {

    navigation(
        route = NavigationRoutes.Unauthenticated.NavigationRoute.route,
        startDestination = NavigationRoutes.Unauthenticated.Login.route
    ) {

        composable(route = NavigationRoutes.Unauthenticated.Login.route) {
            LoginScreen(
                onNavigateToRegistration = {
                    navController.navigate(route = NavigationRoutes.Unauthenticated.Registration.route)
                },
                onNavigateToAuthenticatedRoute = {
                    navController.navigate(route = NavigationRoutes.Authenticated.NavigationRoute.route) {
                        popUpTo(route = NavigationRoutes.Unauthenticated.NavigationRoute.route) {
                            inclusive = true
                        }
                    }
                },
                context
            )
        }

        composable(route = NavigationRoutes.Unauthenticated.Registration.route) {
            RegistrationScreen(onNavigateToAuthenticatedRoute = {
                navController.navigate(route = NavigationRoutes.Authenticated.NavigationRoute.route) {
                    popUpTo(route = NavigationRoutes.Unauthenticated.NavigationRoute.route) {
                        inclusive = true
                    }
                }
            }, context)
        }
    }
}

fun NavGraphBuilder.authenticatedGraph(navController: NavHostController, context: Context) {
    navigation(
        route = NavigationRoutes.Authenticated.NavigationRoute.route,
        startDestination = NavigationRoutes.Authenticated.Home.route
    ) {
        // Dashboard
        composable(route = NavigationRoutes.Authenticated.Home.route) {
            HomeScreen(onNavigateToConfection = { confection_id ->
                navController.navigate(
                    route = NavigationRoutes.Authenticated.Confection.getFullRoute(
                        confection_id
                    )
                ) {
                    popUpTo(route = NavigationRoutes.Authenticated.Home.route) {
                    }
                }
            })
        }

        composable(route = NavigationRoutes.Authenticated.User.route) {
            UserScreen(
                onNavigateToConfection = { confection_id ->
                    navController.navigate(
                        route = NavigationRoutes.Authenticated.Confection.getFullRoute(
                            confection_id
                        )
                    ) {
                        popUpTo(route = NavigationRoutes.Authenticated.Home.route) {
                        }
                    }
                },
                context
            )
        }

        composable(
            route = NavigationRoutes.Authenticated.Confection.route,
            arguments = listOf(
                navArgument(name = "confection_id") {
                    type = NavType.IntType
                })
        ) { navBackStackEntry ->
            val confection_id = navBackStackEntry.arguments?.getInt("confection_id")
            if (confection_id != null) {
                ConfectionScreen(confection_id = confection_id)
            }
        }
    }
}