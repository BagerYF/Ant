package com.example.ant.page.profile.order

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.CustomerQuery
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.LoadingView
import com.example.ant.common.view.TopAppBar
import com.example.ant.config.Constants
import com.example.ant.page.login.LoginVM
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.grey75757512
import com.example.ant.ui.theme.grey75757514
import com.example.ant.ui.theme.grey9e9e9e14
import com.example.ant.ui.theme.grey9e9e9e14u
import com.example.ant.ui.theme.underline

@Composable
fun OrderDetailPage(item: CustomerQuery.Edge1, navController: NavHostController, loginVM: LoginVM) {
    val scaffoldState = rememberScaffoldState()

    val loginState by loginVM.loginState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "#${item.node.orderNumber}",
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
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Subtotal",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = "${item.node.subtotalPriceV2?.currencyCode} ${item.node.subtotalPriceV2?.amount}",
                                style = black14,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Shipping",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = "${item.node.totalShippingPriceV2.currencyCode} ${item.node.totalShippingPriceV2.amount}",
                                style = black14,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Taxes",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = "${item.node.totalTaxV2?.currencyCode} ${item.node.totalTaxV2?.amount}",
                                style = black14,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${item.node.totalPriceV2.currencyCode} ${item.node.totalPriceV2.amount}",
                                style = black16,
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(1.dp)
                                .background(color = Greyeeeeee)
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Order Number",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = "${item.node.orderNumber}",
                                style = black14,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Order Date",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = item.node.processedAt.toString().replace("T", " ")
                                    .replace("W", ""),
                                style = black14,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(1.dp)
                                .background(color = Greyeeeeee)
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Shipping",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = "International Priority Express Shipping",
                                style = black14,
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(1.dp)
                                .background(color = Greyeeeeee)
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Contact",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = "${item.node.email}",
                                style = black14,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Shipping",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(110.dp)
                            )
                            Text(
                                text = "${item.node.shippingAddress?.firstName} ${item.node.shippingAddress?.lastName} \n${item.node.shippingAddress?.address1} ${item.node.shippingAddress?.address2} ${item.node.shippingAddress?.city} \n${item.node.shippingAddress?.province} ${item.node.shippingAddress?.zip} ${item.node.shippingAddress?.country}",
                                style = black14,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(1.dp)
                                .background(color = Greyeeeeee)
                        )
                    }
                }
                items(item.node.lineItems.edges) { sItem ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            AsyncImage(
                                model = "${sItem.node.variant?.image?.url ?: Constants.ImagePlaceHolder}",
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .width(104.dp)
                                    .height(156.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = sItem.node.variant?.product?.productType ?: "",
                                    style = black14,
                                )
                                Text(
                                    text = sItem.node.title,
                                    style = black14,
                                )
                                Text(
                                    text = "Size ${sItem.node.variant?.title ?: ""}",
                                    style = grey75757514,
                                )
                                Text(
                                    text = "Qty ${sItem.node.quantity}",
                                    style = grey75757514,
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "",
                                style = grey9e9e9e14,
                                modifier = Modifier.width(114.dp)
                            )
                            Text(
                                text = "Price",
                                style = grey75757514,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${item.node.originalTotalPrice.currencyCode} ${item.node.originalTotalPrice.amount}",
                                style = black14,
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(1.dp)
                                .background(color = Greyeeeeee)
                        )
                    }
                }
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painterResource(id = R.drawable.icon_order_problem),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .size(18.dp)
                        )
                        Text(
                            text = "Help and Contact",
                            style = black14,
                        )
                        Text(
                            text = "Questions about your \norder? Don't hesitate to ask",
                            style = grey75757512,
                            maxLines = 2,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Contact Shopify",
                            style = grey9e9e9e14u,
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                                .height(1.dp)
                                .background(color = Greyeeeeee)
                        )
                    }
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "Return Information",
                            style = black14,
                        )
                        Text(
                            text = "The eligible return period for this item(s) has expired. For more information, see our Return Policy.",
                            style = grey75757512,
                            maxLines = 2,
                        )
                        Text(
                            text = "Return Policy",
                            style = grey9e9e9e14u,
                        )
                    }
                }
            }

        }
    }
}