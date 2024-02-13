package com.example.ant.page.profile.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.CustomerQuery
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.LoadingView
import com.example.ant.common.view.TopAppBar
import com.example.ant.page.login.LoginVM
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black14
import com.google.gson.Gson
import java.util.Base64

@Composable
fun OrderListPage(navController: NavHostController, loginVM: LoginVM) {
    val scaffoldState = rememberScaffoldState()

    val loginState by loginVM.loginState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "My Orders",
                backType = AppBarBackType.BACK,
                navController = navController
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val items = loginState.customer?.orders?.edges ?: listOf()
                items(items) { item ->
                    OrderItem(item = item, navController = navController, loginVM = loginVM)
                }
            }

            if (loginState.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingView(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun OrderItem(item: CustomerQuery.Edge1, navController: NavHostController, loginVM: LoginVM) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                val paramsJson = Base64
                    .getUrlEncoder()
                    .encodeToString(Gson().toJson(item).toByteArray())
                navController.navigate("order_detail/$paramsJson")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 10.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Order#",
                style = black14,
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
            Text(
                text = "${item.node.orderNumber}",
                style = black14,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = item.node.processedAt.toString().replace("T", " ").replace("Z", ""),
                style = black14,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
            Icon(
                painterResource(id = R.drawable.arrow),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )
        }

        AsyncImage(
            model = "${item.node.lineItems.edges.first().node.variant?.image?.url}",
            contentDescription = null,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 16.dp)
                .size(72.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Greyeeeeee)
        )

    }
}