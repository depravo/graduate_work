package com.depravo.thesugarshop.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.depravo.thesugarshop.R
import com.depravo.thesugarshop.data.UserModel
import com.depravo.thesugarshop.network.ApiManager
import com.depravo.thesugarshop.util.DataStoreManager


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserScreen(onNavigateToConfection: (confection_id: Int) -> Unit, context: Context) {
    val dataStoreManager = DataStoreManager(context)
    val apiService = ApiManager()
    val user_id = dataStoreManager.getUserId().collectAsState(initial = 1)
    val user = remember { mutableStateOf(UserModel("", "", "", "")) }
    apiService.getUserInfo(user_id.value) {
        if (it != null) {
            user.value = it
        }
    }

    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = "background_image",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.5f),
        contentScale = ContentScale.FillBounds
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo1_0),
            contentDescription = "logo",
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 20.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Name: " + user.value.user_fname + ' ' + user.value.user_lname,
            fontSize = 20.sp,
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp
            )
        )
        Text(
            text = "Email: " + user.value.email,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )
        Text(
            text = "Phone: " + user.value.phone_number,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )
    }
}