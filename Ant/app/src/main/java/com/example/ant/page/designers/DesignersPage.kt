package com.example.ant.page.designers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.common.view.ListItem
import com.example.ant.model.designer.kDesigersMaps
import com.example.ant.page.cart.CartVM
import com.example.ant.ui.theme.black14

@Composable
fun DesignersPage(navController: NavHostController, cartVM: CartVM) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Designers",
                backType = AppBarBackType.NONE,
                showBag = true,
                navController = navController,
                cartVM = cartVM
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn() {
                item {
                    ListItem(text = "Designer A-Z", click = {
                        navController.navigate("designers_list")
                    })
                }
                item {
                    Text(
                        text = "Featured Designers",
                        modifier = Modifier
                            .padding(16.dp, 40.dp, 0.dp, 9.dp)
                            .height(24.dp),
                        style = black14
                    )
                }
                items(kDesigersMaps) { item ->
                    ListItem(text = item, click = {
                        navController.navigate("product_list")
                    })
                }
            }
        }
    }
}

