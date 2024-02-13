package com.example.ant.page.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ant.R
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.tools.readJSONFromAssets
import com.example.ant.common.view.InputView
import com.example.ant.common.view.LoadingView
import com.example.ant.model.input.InputModel
import com.example.ant.page.cart.CartVM
import com.example.ant.page.profile.address.CountryListPage
import com.example.ant.page.profile.address.CountryListVM
import com.example.ant.ui.theme.Black
import com.example.ant.ui.theme.Grey9e9e9e
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black12
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.blackBold12
import com.example.ant.ui.theme.blackBold14
import com.example.ant.ui.theme.blackBold16
import com.example.ant.ui.theme.grey75757512
import com.example.ant.ui.theme.grey9e9e9e14
import com.example.ant.ui.theme.white16
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckoutPage(
    navController: NavHostController,
    cartVM: CartVM,
    vm: CheckoutVM = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val pageTitles = listOf<String>("Address", "Delivery", "Payment")

    val checkoutState by vm.checkoutState.collectAsState()

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = Color.White, elevation = 1.dp
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    if (checkoutState.currentPageIndex != 3) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                painterResource(id = R.drawable.backarrow),
                                contentDescription = "",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }

                    if (checkoutState.currentPageIndex == -1) {
                        Text(
                            text = "Checkout",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1F)
                        )
                    } else if (checkoutState.currentPageIndex == 3) {
                        Text(
                            text = "Order Complete",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1F)
                        )
                    } else {
                        Row {
                            pageTitles.forEachIndexed { index, item ->
                                Text(text = item,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (checkoutState.currentPageIndex >= index) Black else Grey9e9e9e,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .clickable {
                                            if (checkoutState.currentPageIndex > index) {
                                                checkoutState.currentPageIndex = index
                                                println(item)
                                            }
                                        })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            if (checkoutState.checkout != null) {
                if (checkoutState.currentPageIndex == 0) {
                    Address(vm = vm, state = state)
                } else if (checkoutState.currentPageIndex == 1) {
                    Delivery(vm = vm)
                } else if (checkoutState.currentPageIndex == 2) {
                    Payment(vm = vm)
                } else if (checkoutState.currentPageIndex == 3) {
                    Complete(navController = navController, vm = vm)
                }
            }

            if (checkoutState.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingView(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

    ModalBottomSheetLayout(sheetState = state, sheetContent = {
        CountryListPage(vm = CountryListVM(countryData = checkoutState.countryData),
            navController = navController,
            callBack = { tempCountry ->
                scope.launch {
                    state.hide()
                }
                vm.selectCountry(tempCountry)
            },
            backClick = {
                scope.launch {
                    state.hide()
                }
            })
    }) {}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Address(vm: CheckoutVM, state: ModalBottomSheetState) {

    val checkoutState by vm.checkoutState.collectAsState()

    val scope = rememberCoroutineScope()

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                Text(
                    text = "*Required Fields", style = black12, modifier = Modifier.padding(16.dp)
                )
            }

            items(checkoutState.addressInputList) { items ->
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items.forEach { item ->
                            if (item.show) {
                                if (item.title == "Country") {
                                    item.callBack = {
                                        vm.updateCountryData(vm.countryList)
                                        scope.launch {
                                            state.show()
                                        }
                                    }
                                } else if (item.title == "State") {
                                    item.callBack = {
                                        vm.updateCountryData(vm.selectCountry.provinces!!)
                                        scope.launch {
                                            state.show()
                                        }
                                    }
                                }
                                InputView(
                                    item = item,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 5.dp)
                                )
                            }
                        }
                    }
                    if (items.first().title == "Email") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.select_y),
                                contentDescription = "",
                                modifier = Modifier.size(14.dp)
                            )

                            Text(
                                text = "Subscribe to our newsletter",
                                style = black14,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    } else if (items.first().title == "Phone") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.select_y),
                                contentDescription = "",
                                modifier = Modifier.size(14.dp)
                            )

                            Text(
                                text = "Save address for next purchase",
                                style = black14,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        BottomView(vm = vm)
    }
}

@Composable
fun Delivery(vm: CheckoutVM) {
    val checkoutState by vm.checkoutState.collectAsState()

    val scope = rememberCoroutineScope()

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row {
                        Text(
                            text = "Contact", style = blackBold12, modifier = Modifier.width(60.dp)
                        )

                        Text(
                            text = checkoutState.addressInputList[2][0].text,
                            style = black12,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Row {
                        Text(
                            text = "Address", style = blackBold12, modifier = Modifier.width(60.dp)
                        )

                        Text(
                            text = "${checkoutState.addressInputList[4][0].text} " +
                                    "${checkoutState.addressInputList[5][0].text} " +
                                    "${checkoutState.addressInputList[6][0].text} " +
                                    "${checkoutState.addressInputList[7][1].text} " +
                                    "${checkoutState.addressInputList[3][0].text} ",
                            style = black12,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp)
                            .height(1.dp)
                            .background(color = Greyeeeeee)
                    )

                    Text(
                        text = "*Shipping Methods",
                        style = blackBold12,
                    )
                }

            }

            itemsIndexed(checkoutState.shippingRates) { index, item ->
                Row(modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .border(1.dp, Greyeeeeee, RectangleShape)
                    .padding(16.dp)
                    .clickable {
                        vm.updateShippingLine(index)
                    }) {
                    Image(
                        painter = painterResource(id = if (checkoutState.shippingLine != null && checkoutState.shippingLine!!.title == item.title) R.drawable.selected else R.drawable.unselected),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(16.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = item.title,
                            style = black16,
                            modifier = Modifier.padding(start = 10.dp)
                        )

                        Text(
                            text = "${item.priceV2.currencyCode} ${item.priceV2.amount}",
                            style = black12,
                            modifier = Modifier.padding(start = 10.dp)
                        )

                    }
                }
            }

        }

        BottomView(vm = vm)
    }
}

@Composable
fun Payment(vm: CheckoutVM) {
    val checkoutState by vm.checkoutState.collectAsState()

    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row {
                        Text(
                            text = "Contact", style = blackBold12, modifier = Modifier.width(60.dp)
                        )

                        Text(
                            text = checkoutState.addressInputList[2][0].text,
                            style = black12,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Row {
                        Text(
                            text = "Address", style = blackBold12, modifier = Modifier.width(60.dp)
                        )

                        Text(
                            text = "${checkoutState.addressInputList[4][0].text} " +
                                    "${checkoutState.addressInputList[5][0].text} " +
                                    "${checkoutState.addressInputList[6][0].text} " +
                                    "${checkoutState.addressInputList[7][1].text} " +
                                    "${checkoutState.addressInputList[3][0].text} ",
                            style = black12,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Row {
                        Text(
                            text = "Delivery", style = blackBold12, modifier = Modifier.width(60.dp)
                        )

                        Text(
                            text = checkoutState.shippingLine?.title ?: "",
                            style = black12,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp)
                            .height(1.dp)
                            .background(color = Greyeeeeee)
                    )

                    Text(
                        text = "Payment",
                        style = blackBold12,
                    )

                    Text(
                        text = "All transactions are secure and encrypted.",
                        style = black12,
                    )
                }

            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .border(1.dp, Greyeeeeee, RectangleShape)
                        .padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Credit or Debit Card",
                            style = black16,
                        )

                        Image(
                            painter = painterResource(id = R.drawable.pay_type),
                            contentDescription = "",
                            modifier = Modifier
                                .width(122.dp)
                                .height(24.dp)
                        )
                    }

                    InputView(
                        item = InputModel(
                            title = "Card Number",
                            text = "",
                            hideTitle = true,
                        ), modifier = Modifier.fillMaxWidth()
                    )

                    InputView(
                        item = InputModel(
                            title = "MM / YY",
                            text = "",
                            hideTitle = true,
                        ), modifier = Modifier.width(180.dp)
                    )

                    InputView(
                        item = InputModel(
                            title = "Name on card",
                            text = "",
                            hideTitle = true,
                        ), modifier = Modifier.fillMaxWidth()
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputView(
                            item = InputModel(
                                title = "Security code",
                                text = "",
                                hideTitle = true,
                            ), modifier = Modifier.width(180.dp)
                        )

                        Image(
                            painter = painterResource(id = R.drawable.credit),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .width(50.dp)
                                .height(36.dp)
                        )
                    }

                    Text(
                        text = "The security number is the three digits on the back of the card in the signature box.",
                        style = black14,
                    )
                }


            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Billing Address",
                        style = blackBold12,
                    )
                    Text(
                        text = "Same as shipping address",
                        style = grey75757512,
                    )
                    Text(
                        text = "Shipping address",
                        style = black14,
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp)
                            .height(1.dp)
                            .background(color = Greyeeeeee)
                    )
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.pen),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Edit",
                            style = black14,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Add code",
                        style = blackBold12,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputView(
                            item = checkoutState.creditInputModel,
                            modifier = Modifier.width(screenWidth - 110.dp)
                        )

                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .border(1.dp, Black, RectangleShape)
                                .width(100.dp)
                                .height(40.dp)
                                .clickable {
                                    vm.applyDiscountCode()
                                }
                        ) {
                            Text(
                                text = "Apply", style = black16, modifier = Modifier
                                    .align(
                                        Alignment.Center
                                    )
                            )
                        }
                    }

                    if (checkoutState.discountCode.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .border(1.dp, Black, RectangleShape)
                                .height(40.dp)
                                .padding(horizontal = 10.dp)
                                .clickable {
                                    vm.removeDiscountCode()
                                }
                        ) {
                            Text(
                                text = "${checkoutState.discountCode} ×",
                                style = black16,
                                modifier = Modifier
                                    .align(
                                        Alignment.Center
                                    )
                            )
                        }
                    }
                }
            }
        }

        BottomView(vm = vm, buttonTitle = "Place order")
    }
}

@Composable
fun Complete(navController: NavHostController, vm: CheckoutVM) {

    val checkoutState by vm.checkoutState.collectAsState()

    Column {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(painter = painterResource(id = R.drawable.success), contentDescription = "", modifier = Modifier.size(50.dp))
            Text(text = "Your order is complete", style = blackBold16, modifier = Modifier.padding(top = 32.dp))
            Text(
                text = "A confirmation email will be sent to \n ${checkoutState.addressInputList[2][0].text}",
                textAlign = TextAlign.Center,
                style = grey9e9e9e14,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(16.dp, 16.dp)
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Black)
                .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                .clickable {
                    navController.popBackStack(route = "home", inclusive = false)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Continue Shopping",
                textAlign = TextAlign.Center,
                style = white16
            )
        }
    }
}

@Composable
fun BottomView(vm: CheckoutVM, buttonTitle: String = "Next") {

    val checkoutState by vm.checkoutState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp)
                .height(1.dp)
                .background(color = Greyeeeeee)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Subtotal ${checkoutState.checkout?.subtotalPriceV2?.currencyCode} ${checkoutState.checkout?.subtotalPriceV2?.amount}",
                style = black14
            )
            Text(
                text = "Total ${checkoutState.checkout?.totalPriceV2?.currencyCode} ${checkoutState.checkout?.totalPriceV2?.amount}",
                style = blackBold14
            )
        }
        if (checkoutState.shippingLine != null) {
            Text(
                text = "Shipping ${checkoutState.shippingLine?.priceV2?.currencyCode} ${checkoutState.shippingLine?.priceV2?.amount}",
                style = black14
            )
        } else {
            Text(
                text = "Shipping ${checkoutState.checkout?.totalTaxV2?.currencyCode} 0.0",
                style = black14
            )
        }

        Text(
            text = "Tax ${checkoutState.checkout?.totalTaxV2?.currencyCode} ${checkoutState.checkout?.totalTaxV2?.amount}",
            style = black14
        )
        val lineItemsSubtotalPrice: Double =
            checkoutState.checkout?.lineItemsSubtotalPrice?.amount.toString().toDouble()
        val subtotalPriceV2: Double =
            checkoutState.checkout?.subtotalPriceV2?.amount.toString().toDouble()
        val discountValue = String.format("%.2f", lineItemsSubtotalPrice - subtotalPriceV2)
        Text(
            text = "Discount ${checkoutState.checkout?.totalTaxV2?.currencyCode} $discountValue",
            style = black14
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Black)
                .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                .clickable {
                    vm.submitClick()
                }, contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonTitle, textAlign = TextAlign.Center, style = white16
            )
        }
    }
}