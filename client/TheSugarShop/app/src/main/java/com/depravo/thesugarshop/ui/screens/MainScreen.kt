package com.depravo.thesugarshop.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.depravo.thesugarshop.util.NavigationRoutes


@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(context: Context) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            /*val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            if((currentDestination?.route != NavigationRoutes.Unauthenticated.Login.route) &&
                (currentDestination?.route != NavigationRoutes.Unauthenticated.Registration.route)) {*/
                ModalDrawerSheet(modifier = Modifier.width(200.dp)) {
                    DrawerHeader(navController)
                    DrawerBody(navController)
                }

        }) {

        Scaffold(snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.White,
                    contentColor = Color.Red,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        }
        ) {
            NavGraph(navHostController = navController, context)
        }
    }
}