package com.depravo.thesugarshop.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.depravo.thesugarshop.R
import com.depravo.thesugarshop.data.RegisterCredentials
import com.depravo.thesugarshop.network.ApiManager
import com.depravo.thesugarshop.util.DataStoreManager
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegistrationScreen(onNavigateToAuthenticatedRoute: () -> Unit, context: Context) {
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val apiService = ApiManager()
    val dataStoreManager = DataStoreManager(context)
    val coroutineScope = rememberCoroutineScope()

    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = "background_image",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.5f),
        contentScale = ContentScale.FillBounds
    )
    Scaffold(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .alpha(0.5f)
            .clip(RoundedCornerShape(10.dp)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Join us",
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text("Login") },
                value = login.value,
                onValueChange = {
                    login.value = it
                },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text("Password") },
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text("Email") },
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text("Name") },
                value = name.value,
                onValueChange = {
                    name.value = it
                },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                val reg_cred = RegisterCredentials(login.value, password.value, email.value, name.value, "")
                apiService.register(reg_cred) {
                    if(it != null) {
                        coroutineScope.launch {
                            dataStoreManager.saveUserId(it.userId)
                        }
                        onNavigateToAuthenticatedRoute.invoke()
                    }
                }
            }) {
                Text(text = "Register")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "tip: don’t eat cakes after 6:00 pm",
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center,

                )
        }
    }
}