package com.example.ant.page.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.view.InputView
import com.example.ant.common.view.LoadingView
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.grey75757514
import com.example.ant.ui.theme.white16

@Composable
fun LoginPage(loginOrRegis: Boolean, navController: NavHostController, loginVM: LoginVM) {
    val scaffoldState = rememberScaffoldState()

    val loginState by loginVM.loginState.collectAsState()

    var isLoginOrRegis by remember { mutableStateOf(loginOrRegis) }

    loginVM.callBack = {
        println("login success")
        navController.popBackStack()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "",
                backType = AppBarBackType.CLOSE,
                elevation = 0.dp,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Shopify",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isLoginOrRegis) {
                    loginState.loginInputList.forEach { items ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            items.forEach { item ->
                                InputView(
                                    item = item, modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 5.dp)
                                )
                            }
                        }
                    }
                } else {
                    loginState.regisInputList.forEach { items ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            items.forEach { item ->
                                InputView(
                                    item = item, modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp, 30.dp, 16.dp, 10.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Black)
                        .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                        .clickable {
                            if (isLoginOrRegis) {
                                loginVM.loginClick()
                            } else {
                                loginVM.regisClick()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isLoginOrRegis) "Login" else "Create account",
                        textAlign = TextAlign.Center,
                        style = white16
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp, 8.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.White)
                        .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                        .clickable {
                            isLoginOrRegis = !isLoginOrRegis
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isLoginOrRegis) "Create a new account" else "Back to login",
                        textAlign = TextAlign.Center,
                        style = black16
                    )
                }


                Text(
                    text = "Continue as guest",
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 30.dp)
                        .clickable {
                            loginVM.queryCustomer()
                        },
                    style = grey75757514
                )
            }

            if (loginState.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingView(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}