package com.example.ant.page.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.config.Constants
import com.example.ant.model.home.AllHomeModel
import com.example.ant.model.home.HomeModel
import com.example.ant.model.home.kHomeMaps
import com.example.ant.page.cart.CartVM
import com.example.ant.page.login.LoginVM
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.black20
import com.example.ant.ui.theme.grey9e9e9e14u
import com.example.ant.ui.theme.white16
import com.example.type.Cart
import com.google.gson.Gson

@Composable
fun HomePage(navController: NavHostController, loginVM: LoginVM, cartVM: CartVM) {
    val scaffoldState = rememberScaffoldState()

    val allData: AllHomeModel = Gson().fromJson(kHomeMaps, AllHomeModel::class.java)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Shopify",
                backType = AppBarBackType.NONE,
                showBag = true,
                navController = navController,
                cartVM = cartVM
            )
        },
    )
    {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(allData) { items ->
                    if (items.name == "PopularSection" || items.name == "PrimarySection") {
                        PopularSection(navController = navController, section = items)
                    } else if (items.name == "FirstSection") {
                        NormalSection(navController = navController, section = items)
                    } else if (items.name == "NewSeasonSection") {
                        NewSeasonSection(navController = navController, section = items)
                    } else if (items.name == "NewArrivalSection") {
                        NewArrivalSection(navController = navController, section = items)
                    } else {
                        Text(text = items.name)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NormalSection(navController: NavHostController, section: HomeModel) {

    val pagerState = rememberPagerState(pageCount = {
        section.items.count()
    })

    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(315.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
    ) { page ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable {
                navController.navigate("product_list")
            }
        ) {
            val item = section.items[page]
            AsyncImage(
                model = item.absoluteMobileImageUrl ?: Constants.BigImagePlaceHolder,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(305.dp)
                    .height(374.dp)
            )
            Text(
                text = item.text ?: "",
                style = black16,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Text(
                text = "Shop Now",
                style = grey9e9e9e14u,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
fun PopularSection(navController: NavHostController, section: HomeModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 40.dp)
    ) {
        for (item in section.items) {
            Column(modifier = Modifier.clickable {
                navController.navigate("product_list")
            }) {
                AsyncImage(
                    model = item.absoluteMobileImageUrl ?: Constants.BigImagePlaceHolder,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
                Text(
                    text = item.brand ?: "",
                    style = black20,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                Text(
                    text = item.productName ?: "",
                    style = black16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                Text(
                    text = "Shop Now",
                    style = grey9e9e9e14u,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 32.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewSeasonSection(navController: NavHostController, section: HomeModel) {

    val pagerState = rememberPagerState(pageCount = {
        section.items.count()
    })

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = section.title ?: "",
            style = black20,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 16.dp, end = 10.dp)
        )
        Text(
            text = section.subTitle ?: "",
            style = black16,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 16.dp, end = 10.dp, bottom = 30.dp)
        )
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(315.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
                .clickable {
                    navController.navigate("product_list")
                }
            ) {
                val item = section.items[page]
                AsyncImage(
                    model = item.absoluteMobileImageUrl ?: Constants.BigImagePlaceHolder,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(305.dp)
                        .height(374.dp)
                )
                Text(
                    text = item.text ?: "",
                    style = black16,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(16.dp, 16.dp)
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Black)
                .border(width = 1.dp, color = Color.Black, shape = RectangleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Shop Now",
                textAlign = TextAlign.Center,
                style = white16
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewArrivalSection(navController: NavHostController, section: HomeModel) {

    val pagerState = rememberPagerState(pageCount = {
        section.items.count()
    })

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = section.title ?: "",
            style = black20,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 16.dp, end = 10.dp)
        )
        Text(
            text = section.subTitle ?: "",
            style = black16,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 16.dp, end = 10.dp, bottom = 30.dp)
        )
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(315.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
                .clickable {
                    navController.navigate("product_list")
                }
            ) {
                val item = section.items[page]
                AsyncImage(
                    model = item.absoluteMobileImageUrl ?: Constants.BigImagePlaceHolder,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(305.dp)
                        .height(374.dp)
                )
            }
        }
    }
}