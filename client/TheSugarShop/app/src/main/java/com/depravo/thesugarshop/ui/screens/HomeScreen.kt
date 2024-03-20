package com.depravo.thesugarshop.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import coil.compose.AsyncImage
import com.depravo.thesugarshop.R
import com.depravo.thesugarshop.data.CategoryModel
import com.depravo.thesugarshop.data.ConfectionModel
import com.depravo.thesugarshop.network.ApiManager
import com.depravo.thesugarshop.ui.theme.SelectedTab
import com.depravo.thesugarshop.ui.theme.SimpleTab
import com.depravo.thesugarshop.ui.theme.TabBack


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onNavigateToConfection: (confection_id: Int) -> Unit) {
    val search = remember {
        mutableStateOf("")
    }
    val apiService = ApiManager()
    val confectionList = remember { mutableStateListOf<ConfectionModel>() }
    apiService.getCatalog {
        if (it != null) {
            Log.d("MYTAG", it.toString())
            confectionList.clear()
            confectionList.addAll(it)
        }
    }
    val categoriesList = remember { mutableStateListOf<CategoryModel>() }
    apiService.getCategories {
        if (it != null) {
            Log.d("MYTAG", it.toString())
            categoriesList.clear()
            categoriesList.addAll(it)
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
    SearchBar(
        modifier = Modifier
            .alpha(0.9f)
            .padding(15.dp)
            .clip(RoundedCornerShape(20.dp)),
        query = search.value,
        onQueryChange = { newQuery -> search.value = newQuery },
        onSearch = {
            /*apiService.getConfectionByName(search.value) {
                if (it != null) {
                    confectionList.clear()
                    confectionList.addAll(it)
                }
            }*/
        },
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        trailingIcon = {},
        active = true,
        onActiveChange = {},
        tonalElevation = 0.dp
    ) {
        ConfectionList(confectionList.toList(), onNavigateToConfection)
        //CategoriesTabLayout(categoriesList, confectionList, onNavigateToConfection)
    }
}

@ExperimentalFoundationApi
@Composable
fun CategoriesTabLayout(
    categoriesList: MutableList<CategoryModel>,
    list: MutableList<ConfectionModel>,
    onNavigateToConfection: (confection_id: Int) -> Unit
) {
    val tabList = "Всё" + categoriesList.map { it.category_name }
    val apiService = ApiManager()

    var tabIndex by remember { mutableStateOf(0) }
    val corountineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .clip(RoundedCornerShape(20.dp)),

        ) {

        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            containerColor = TabBack,
            indicator = {},
            divider = {}
        ) {
            tabList.forEachIndexed { index, text ->
                val selected = tabIndex == index
                Tab(
                    selected = selected,
                    onClick = {
                        tabIndex = index
                    },
                    text = { Text(text = text as String, color = Color.White) },
                    modifier = if (selected) Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(SelectedTab)
                    else Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(SimpleTab)
                )
            }
        }
    }

    Log.d("MYTAG", "made tabs")
    when (tabIndex) {
        0 -> ConfectionList(list.toList(), onNavigateToConfection)
        in 1..10 -> {
            apiService.getCategoryProducts(categoriesList[tabIndex + 1] as String) {
                if (it != null) {
                    list.clear()
                    list.addAll(it)
                }
            }
            ConfectionList(list, onNavigateToConfection)
        }
    }
}

@Composable
fun ConfectionList(
    list: List<ConfectionModel>,
    onNavigateToConfection: (confection_id: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        itemsIndexed(list) { _, item ->
            ConfectionItem(item, onNavigateToConfection)
        }
    }
}

@Composable
fun ConfectionItem(item: ConfectionModel, onNavigateToConfection: (confection_id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable {onNavigateToConfection(item.product_id) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.img_resource,
                contentDescription = "cake_img",
                modifier = Modifier
                    .padding(start = 3.dp, end = 3.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Column(
                modifier = Modifier
                    .width(245.dp)
                    .padding(start = 8.dp, top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = item.product_name
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = item.description
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.price.toString(),
                modifier = Modifier.padding(end = 3.dp)
            )

        }
    }
}