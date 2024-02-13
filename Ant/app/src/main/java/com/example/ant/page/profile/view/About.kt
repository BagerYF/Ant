package com.example.ant.page.profile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.black20
import com.example.ant.ui.theme.grey75757514


@Composable
fun AboutPage(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = "Profile", backType = AppBarBackType.BACK, navController = navController)
        },
    )
    {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()

            ) {
                item {
                    Text(
                        text = "The Best of Luxury Fashion",
                        textAlign = TextAlign.Center,
                        style = black20,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
                item {
                    Image(
                        painterResource(id = R.drawable.about),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth().background(Color.Green)
                    )
                }
                item {
                    Text(
                        text = """
Shopify brings to you the best of luxury fashion. Featuring an extensive range of over 500 brands, including womenswear, menswear and kidswear from iconic fashion houses, such as Prada, Gucci, Saint Laurent, Balenciaga and Valentino.

Every aspect of the user experience is optimized, starting with a curated selection of world-renowned brands delivered with best-in-class technology. Payment is easy and secure, with free express shipping to over X countries and free returns on all orders so you can shop with confidence.
                     """,
                        textAlign = TextAlign.Left,
                        style = black20,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}