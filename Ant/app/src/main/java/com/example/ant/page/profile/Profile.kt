package com.example.ant.page.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.ListItem
import com.example.ant.common.view.TopAppBar
import com.example.ant.page.cart.CartVM
import com.example.ant.page.login.LoginVM
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.grey75757514
import com.example.ant.ui.theme.grey75757516
import com.example.ant.ui.theme.white16

@Composable
fun ProfilePage(navController: NavHostController, loginVM: LoginVM, cartVM: CartVM) {
    val scaffoldState = rememberScaffoldState()

    val loginState by loginVM.loginState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Profile",
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
            LazyColumn() {
                item {
                    if (loginState.isLoginOrLogout) {
                        Column {
                            Text(
                                text = "Welcome ${loginState.customer?.displayName}",
                                textAlign = TextAlign.Center,
                                style = black16,
                                modifier = Modifier.padding(start = 16.dp, top = 30.dp)
                            )
                            Text(
                                text = "Sign out",
                                textAlign = TextAlign.Center,
                                style = grey75757514,
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp,
                                        top = 10.dp,
                                        bottom = 20.dp
                                    )
                                    .clickable {
                                        loginVM.logoutClick()
                                    }
                            )
                        }
                    } else {
                        LoginView(navController = navController)
                    }
                }

                if (loginState.isLoginOrLogout) {
                    item {
                        ListItem(text = "My Orders", click = {
                            navController.navigate("order_list")
                        })
                        ListItem(text = "Save Address", click = {
                            navController.navigate("address_list")
                        })
                    }
                }

                item {
                    Text(
                        text = "Setting",
                        modifier = Modifier
                            .padding(16.dp, 40.dp, 0.dp, 9.dp)
                            .height(24.dp),
                        style = black14
                    )
                }
                item {
                    ListItem(text = "Region", click = {
                        navController.navigate("region")
                    })
                    ListItem(text = "Notifications", click = {
                        navController.navigate("notifications")
                    })
                }
                item {
                    Text(
                        text = "Support",
                        modifier = Modifier
                            .padding(16.dp, 40.dp, 0.dp, 9.dp)
                            .height(24.dp),
                        style = black14
                    )
                }
                item {
                    ListItem(text = "About", click = {
                        navController.navigate("about")
                    })
                    ListItem(text = "Help and Contacts", click = {
                        navController.navigate("help_and_contact")
                    })
                    ListItem(text = "FAQ", click = {
                        navController.navigate("faq")
                    })
                    ListItem(text = "Return and Refunds", click = {
                        navController.navigate("return_and_refunds")
                    })
                }

                if (loginState.isLoginOrLogout) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 30.dp)
                                    .width(108.dp)
                                    .height(32.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RectangleShape
                                    )
                                    .clickable {
                                        loginVM.logoutClick()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Log out",
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Terms and Conditions",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 30.dp)
                            .clickable {
                                navController.navigate("terms_and_conditions")
                            }
                    )
                }

                item {
                    Text(
                        text = "Privacy Policy",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 10.dp)
                            .clickable {
                                navController.navigate("privacy_policy")
                            }
                    )
                }

                item {
                    Text(
                        text = "App Version 1.0.0",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        style = grey75757514,
                        modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginView(navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Login",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 30.dp)
        )
        Text(
            text = "Login to manage your orders and \n fast-track checkout",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 20.dp)
        )
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Box(
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 30.dp)
                    .weight(1f)
                    .height(40.dp)
                    .background(Color.Black)
                    .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                    .clickable {
                        navController.navigate("login/${true}")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Login",
                    textAlign = TextAlign.Center,
                    style = white16
                )
            }
            Box(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .weight(1f)
                    .height(40.dp)
                    .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                    .clickable {
                        navController.navigate("login/${false}")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Register",
                    textAlign = TextAlign.Center,
                    style = black16
                )
            }
        }
    }
}