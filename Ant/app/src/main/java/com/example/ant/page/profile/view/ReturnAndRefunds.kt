package com.example.ant.page.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.black20

@Composable
fun ReturnAndRefundsPage(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = "Return And Refunds", backType = AppBarBackType.BACK, navController = navController)
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
                modifier = Modifier.fillMaxWidth()

            ) {
                item {
                    Text(
                        text = "Returns",
                        textAlign = TextAlign.Left,
                        style = black20,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Text(
                        text = """You can purchase in confidence and send the items back to us if they are not right for you. If you would like to initiate a return, please go to your account at the top right corner where it says your name. Click "Create Return" next to the order you would like to return and follow the prompts. If you checked out as a guest, please contact customer service at customerservice@Shopify.com. In the event you need change the pickup time you will need to liaise directly with Shopify's preferred courier.

For orders placed after 23 February 2021, returns are free. Should you wish to return your product, all taxes and duties will be refunded and no return fee will be incurred. However, any original shipping charges (if applicable) will not be refunded.

For orders placed before 23 February 2021, taxes and duties will not be refunded and these orders will be subject to a return fee of $25.

Returns must arrive at our location within 14 days from the date the parcel is received using the label provided by us. Items received after this period are accepted only at the discretion of Shopify. Please note that orders can only be cancelled within the first 24 hours of making a purchase provided that the item/s have not shipped. Any cancellations initiated after the 24 hour period has expired will incur a $ 25 cancellation fee and any order which contains items that have been shipped may not be cancelled.""",
                        textAlign = TextAlign.Left,
                        style = black20,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
                    )
                }
                item {
                    Text(
                        text = "Refunds",
                        textAlign = TextAlign.Left,
                        style = black20,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Text(
                        text = """Once we receive the item, we will refund you to your original payment method. Please note that refunds may take up to 10 working days to process due to varying processing times between payment providers.""",
                        textAlign = TextAlign.Left,
                        style = black20,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}