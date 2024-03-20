package com.depravo.thesugarshop.util

sealed class NavigationRoutes {
    // Unauthenticated Routes
    sealed class Unauthenticated(val route: String) : NavigationRoutes() {
        object NavigationRoute : Unauthenticated(route = "unauthenticated")
        object Login : Unauthenticated(route = "login")
        object Registration : Unauthenticated(route = "registration")
    }

    // Authenticated Routes
    sealed class Authenticated(val route: String) : NavigationRoutes() {
        object NavigationRoute : Authenticated(route = "authenticated")
        object Home : Authenticated(route = "home")
        object User : Authenticated(route = "user")
        object Confection : Authenticated(route = "confection" + "/{confection_id}") {
            fun getFullRoute(confection_id: Int): String {
                return "confection" + "/$confection_id"
            }
        }
    }
}