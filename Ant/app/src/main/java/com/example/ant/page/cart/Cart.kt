package com.example.ant.page.cart

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black12
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.white16
import com.example.ant.R
import com.example.ant.common.view.LoadingView
import com.example.ant.common.view.wheel_picker.FVerticalWheelPicker
import com.example.ant.common.view.wheel_picker.rememberFWheelPickerState
import com.example.ant.config.Constants
import com.example.ant.ui.theme.grey75757512
import com.example.ant.ui.theme.grey75757512u
import com.example.ant.ui.theme.grey75757514
import com.example.ant.ui.theme.grey9e9e9e14
import com.example.ant.ui.theme.grey9e9e9e14u
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Base64

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartPage(navController: NavHostController, cartVM: CartVM) {
    val scaffoldState = rememberScaffoldState()

    val cartState by cartVM.cartState.collectAsState()

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    val scope = rememberCoroutineScope()

    val qtyData = listOf<String>(
        "1",
        "2",
        "3",
        "4",
        "5",
    )

    val pickerState = rememberFWheelPickerState()

    var isLoading by remember {
        mutableStateOf(false)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Shopping Bag",
                backType = AppBarBackType.CLOSE,
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
            if (cartState.cart != null && cartState.cart?.lines?.edges?.isNotEmpty() == true) {
                Column {
                    println(cartState.cart)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartState.cart?.lines?.edges ?: listOf()) { sItem ->
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.Top,
                                                    modifier = Modifier.padding(top = 8.dp)
                                                ) {
                                                    AsyncImage(
                                                        model = "${sItem.node.merchandise.onProductVariant?.image?.url ?: Constants.ImagePlaceHolder}",
                                                        contentScale = ContentScale.Fit,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .width(104.dp)
                                            .height(156.dp)
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = sItem.node.merchandise.onProductVariant?.product?.productType
                                                    ?: "",
                                                style = black14,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Image(
                                                painter = painterResource(id = R.drawable.cart_delete_product),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .clickable {
                                                        scope.launch {
                                                            isLoading = true
                                                            val result =
                                                                cartVM.removeProductFromCart(sItem.node.id)
                                                            isLoading = false
                                                        }
                                                    }
                                            )
                                        }

                                        Text(
                                            text = sItem.node.merchandise.onProductVariant?.product!!.title,
                                            style = black14,
                                        )
                                        Text(
                                            text = sItem.node.merchandise.onProductVariant.title,
                                            style = grey75757514,
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .width(120.dp)
                                                .height(34.dp)
                                                .padding(0.dp)
                                                .background(color = Greyeeeeee)
                                                .padding(horizontal = 10.dp)
                                                .clickable {
                                                    cartVM.updateQtyVariantId(sItem.node.id)
                                                    scope.launch {
                                                        state.show()
                                                    }
                                                }
                                        ) {
                                            Text(
                                                text = "Qty ${sItem.node.quantity}",
                                                style = grey75757514,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Image(
                                                painter = painterResource(id = R.drawable.arrow_down),
                                                contentDescription = "",
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }

                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Move to wishlist",
                                        style = grey75757512,
                                        modifier = Modifier.width(114.dp)
                                    )
                                    Text(
                                        text = "Price",
                                        style = grey75757514,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${sItem.node.cost.totalAmount.currencyCode} ${sItem.node.cost.totalAmount.amount}",
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
                            Row(modifier = Modifier.padding(vertical = 40.dp)) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.cart_icon_review),
                                        contentDescription = "",
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(text = "Reviews", style = black14)
                                    Text(
                                        text = "See what our customers \n have to say about \n shopping with us",
                                        style = black12,
                                        textAlign = TextAlign.Center,
                                    )
                                    Text(text = "See Our Reviews", style = grey75757512u)
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.cart_icon_free_return),
                                        contentDescription = "",
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(text = "Easy returns", style = black14)
                                    Text(
                                        text = "Shop in confidence with \n a quick and easy return \n process",
                                        style = black12,
                                        textAlign = TextAlign.Center,
                                    )
                                    Text(text = "Return Policy", style = grey75757512u)
                                }
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(color = Greyeeeeee)
                        )
                        Text(
                            text = "Subtotal ${cartState.cart!!.cost.totalAmount.currencyCode} ${cartState.cart!!.cost.totalAmount.amount}",
                            style = black14,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = "Shipping and taxes calculated at checkout",
                            style = black12,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                        Row {
                            Box(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .weight(1f)
                                    .height(40.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RectangleShape
                                    )
                                    .clickable {
//
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.product_detail_apple_pay),
                                    contentDescription = "",
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                            Box(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .weight(1f)
                                    .height(40.dp)
                                    .background(Color.Black)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RectangleShape
                                    )
                                    .clickable {
                                        val cartString = Gson().toJson(cartState.cart)
                                        val cartEncoder = Base64
                                            .getUrlEncoder()
                                            .encodeToString(cartString.toByteArray())

                                        navController.navigate("checkout/$cartEncoder")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Checkout",
                                    textAlign = TextAlign.Center,
                                    style = white16
                                )
                            }
                        }
                    }
                }
            } else {
                Text(text = "Your shopping bag is currently empty", style = black16, modifier = Modifier.align(
                    Alignment.Center))
            }
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingView(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Please select an enquiry type",
                        style = black14,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painterResource(id = R.drawable.nav_close),
                        contentDescription = "",
                        modifier = Modifier.size(14.dp)
                    )
                }
                FVerticalWheelPicker(
                    modifier = Modifier.fillMaxWidth(),
                    count = qtyData.count(),
                    unfocusedCount = 2,
                    debug = true,
                    state = pickerState
                ) { index ->
                    Text(text = qtyData[index], style = black14)
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp, 16.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Black)
                        .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                        .clickable {
                            println(pickerState.currentIndex)
                            val qty = qtyData[pickerState.currentIndex]
                            scope.launch {
                                state.hide()
                                isLoading = true
                                val result = cartVM.updateProductQuantityInCart(qty.toInt())
                                isLoading = false
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Select",
                        textAlign = TextAlign.Center,
                        style = white16
                    )
                }
            }
        }
    ) {}
}