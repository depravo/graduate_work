package com.depravo.thesugarshop.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.depravo.thesugarshop.R
import com.depravo.thesugarshop.util.NavigationRoutes


@Composable
fun DrawerHeader(navController: NavHostController) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(id = R.drawable.logo1_0),
            contentDescription = "logo",
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp)
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    navController.navigate(route = NavigationRoutes.Authenticated.Home.route)
                }
        )
    }
}

@Composable
fun DrawerBody(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
    ) {
        items(7) {
            when (it) {
                0 -> Text("My profile", modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(route = NavigationRoutes.Authenticated.User.route) {
                            popUpTo(route = NavigationRoutes.Authenticated.Home.route) {
                            }
                        }
                    }
                    .padding(10.dp))

                1 -> Text("Cart", modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("") }
                    .padding(10.dp))

                2 -> Text("Favorite", modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("") }
                    .padding(10.dp))

                3 -> Text("Orders history", modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("") }
                    .padding(10.dp))

                4 -> Text("Settings", modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("") }
                    .padding(10.dp))

                5 -> Text("Log out", modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(route = NavigationRoutes.Unauthenticated.Login.route) }
                    .padding(10.dp))
            }
        }
    }
}