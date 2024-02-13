package com.example.ant.page.profile.address

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.CustomerQuery
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.LoadingView
import com.example.ant.common.view.TopAppBar
import com.example.ant.page.login.LoginVM
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.white16
import com.google.gson.Gson
import java.util.Base64

@Composable
fun AddressListPage(navController: NavHostController, loginVM: LoginVM) {
    val scaffoldState = rememberScaffoldState()

    val loginState by loginVM.loginState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Address",
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
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val items = loginState.customer?.addresses?.edges ?: listOf()
                    items(items) { item ->
                        AddressItem(item = item, navController = navController, loginVM = loginVM)
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp, 16.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Black)
                        .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                        .clickable {
                            navController.navigate("address_detail/{}")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add address",
                        textAlign = TextAlign.Center,
                        style = white16
                    )
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
fun AddressItem(item: CustomerQuery.Edge, navController: NavHostController, loginVM: LoginVM) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "${item.node.firstName} ${item.node.lastName}",
            style = black14,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Text(
            text = "${item.node.address1} ${item.node.address2} ${item.node.city}",
            style = black14,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Text(
            text = "${item.node.province} ${item.node.zip} ${item.node.country}",
            style = black14,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .background(Color.White)
                .padding(vertical = 10.dp)
                .clickable {
                    val id = Base64
                        .getUrlEncoder()
                        .encodeToString(item.node.id.toByteArray())
                    val paramsJson: String =
                        Gson().toJson(item.copy(node = item.node.copy(id = id)))
                    navController.navigate("address_detail/$paramsJson")
                }) {
                Icon(
                    painterResource(id = R.drawable.pen),
                    contentDescription = "",
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Edit",
                    style = black14,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 0.dp)
                )
            }

            Row(modifier = Modifier
                .padding(start = 4.dp, end = 16.dp)
                .background(Color.White)
                .padding(vertical = 10.dp)
                .clickable {
                    loginVM.deleteAddress(id = item.node.id)
                }) {
                Icon(
                    painterResource(id = R.drawable.address_del),
                    contentDescription = "",
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Delete",
                    style = black14,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 0.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Greyeeeeee)
        )

    }
}
