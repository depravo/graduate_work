package com.depravo.thesugarshop.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.depravo.thesugarshop.data.FullConfectionModel
import com.depravo.thesugarshop.data.ReviewModel
import com.depravo.thesugarshop.network.ApiManager


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ConfectionScreen(confection_id: Int) {
    val apiService = ApiManager()
    var product by remember {
        mutableStateOf(
            FullConfectionModel(
                0,
                "",
                "",
                "",
                0.0f,
                0.0f,
                arrayListOf()
            )
        )
    }
    apiService.getProductInfo(confection_id) {
        if (it != null)
            product = it
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo1_0),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(3.dp)),
                contentScale = ContentScale.Fit,
            )
            Text(
                text = product.product_name,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = product.rate.toString(),
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 10.dp, end = 10.dp)
            )
        }
        AsyncImage(
            model = "${product.img_resource}",
            contentDescription = "cake_img",
            modifier = Modifier
                .padding(10.dp)
                .size(300.dp)
                .clip(RoundedCornerShape(15.dp))
        )
        Text(
            text = product.description,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 8.dp, top = 10.dp, end = 8.dp)
        )
        /*LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(product.review) { _, item ->
                ReviewItem(item)
            }
        }*/
    }
}

@Composable
fun ReviewItem(review: ReviewModel) {
    Text(text = review.user_fname + review.user_lname, modifier = Modifier.fillMaxWidth())
    Row(
        modifier = Modifier.fillMaxWidth().padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = review.comment)
        Text(text = review.rate.toString())
    }
}