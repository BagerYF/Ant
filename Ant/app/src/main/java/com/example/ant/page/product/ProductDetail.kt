package com.example.ant.page.product

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import coil.compose.AsyncImage
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.view.LoadingView
import com.example.ant.common.view.TopAppBar
import com.example.ant.common.view.wheel_picker.FVerticalWheelPicker
import com.example.ant.common.view.wheel_picker.rememberFWheelPickerState
import com.example.ant.config.Constants
import com.example.ant.model.wishlist.WishlistModel
import com.example.ant.page.cart.CartVM
import com.example.ant.page.search.CategoryListView
import com.example.ant.page.wishlist.WishlistVM
import com.example.ant.ui.theme.Greybdbdbd
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.black20
import com.example.ant.ui.theme.black24
import com.example.ant.ui.theme.blackBold16
import com.example.ant.ui.theme.grey75757516
import com.example.ant.ui.theme.grey9e9e9e14
import com.example.ant.ui.theme.red14
import com.example.ant.ui.theme.white16
import kotlinx.coroutines.launch
import java.util.Base64

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProductDetailPage(
    navController: NavHostController,
    vm: ProductDetailVM = hiltViewModel(),
    cartVM: CartVM,
    wishlistVM: WishlistVM
) {
    val scaffoldState = rememberScaffoldState()

    val productDetailState by vm.productDetailState.collectAsState()

    val cartState by cartVM.cartState.collectAsState()

    val wishlistState by wishlistVM.wishlistState.collectAsState()

    val pagerState =
        rememberPagerState(pageCount = { productDetailState.product?.images?.edges?.count() ?: 0 })

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    val scope = rememberCoroutineScope()
    val pickerState = rememberFWheelPickerState()
    var showSize by remember {
        mutableStateOf(false)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "",
                backType = AppBarBackType.BACK,
                showBag = true,
                navController = navController,
                cartVM = cartVM
            )
        },
    )
    {

        if (productDetailState.product != null) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {

                Column {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        item {
                            Box {
                                HorizontalPager(state = pagerState) { page ->
                                    AsyncImage(
                                        model = productDetailState.product!!.images.edges[page].node.url,
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(380.dp)
                                            .background(Color.White)
                                    )
                                }
                                Row(
                                    Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(pagerState.pageCount) { iteration ->
                                        val color =
                                            if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp, vertical = 10.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                                .size(8.dp)
                                        )
                                    }
                                }
                            }

                        }
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 16.dp
                                )
                            ) {
                                Text(
                                    text = productDetailState.product!!.vendor,
                                    style = black24
                                )
                                Text(
                                    text = productDetailState.product!!.title,
                                    style = black16,
                                    maxLines = 2
                                )
                                Row {
                                    Text(
                                        text = "$${productDetailState.product!!.variants.edges[productDetailState.variantIndex].node.compareAtPrice?.amount}",
                                        style = black14
                                    )
                                    Text(
                                        text = "$${productDetailState.product!!.variants.edges[productDetailState.variantIndex].node.price.amount}",
                                        style = red14,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 10.dp)
                                    )

                                    val wishlistModel = WishlistModel(
                                        id = productDetailState.product!!.id,
                                        title = productDetailState.product!!.title,
                                        vendor = productDetailState.product!!.vendor,
                                        price = "${productDetailState.product!!.variants.edges.first().node.price.amount}",
                                        image = "${productDetailState.product!!.images.edges.first().node.url}"
                                    )

                                    Row(modifier = Modifier.clickable {
                                        wishlistVM.addWishlist(
                                            productDetailState.product!!,
                                            !wishlistState.wishlist.contains(wishlistModel)
                                        )
                                    }) {
                                        Text(
                                            text = if (wishlistState.wishlist.contains(wishlistModel)
                                            ) "Added to Wishlist" else "Add to Wishlist",
                                            style = black14,
                                            modifier = Modifier.padding(end = 10.dp)
                                        )
                                        Image(
                                            painter = painterResource(
                                                id = if (wishlistState.wishlist.contains(
                                                        wishlistModel
                                                    )
                                                ) R.drawable.star_select else R.drawable.star
                                            ),
                                            contentDescription = "",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 16.dp
                                )
                            ) {
                                Row {
                                    Image(
                                        painter = painterResource(id = R.drawable.size),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Size guide",
                                        style = black14,
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .padding(start = 10.dp)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RectangleShape
                                        )
                                        .padding(horizontal = 10.dp)
                                        .height(40.dp)
                                        .clickable {
                                            showSize = true
                                            scope.launch {
                                                state.show()
                                            }
                                        }
                                ) {
                                    Text(text = "Size", style = black16)
                                    Text(
                                        text = productDetailState.product!!.variants.edges[productDetailState.variantIndex].node.title,
                                        style = grey9e9e9e14,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.arrow_down),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 16.dp
                                )
                            ) {
                                Text(text = "Description", style = black14)
                                Text(
                                    text = productDetailState.product!!.description,
                                    style = black14.copy(lineHeight = 20.sp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(color = Greyeeeeee)
                                )
                                Text(
                                    text = """Shipping and Returns
                                
Delivery Destinations
Shopify ships globally to a large number of countries. For more information on delivery, visit our orders & shipping page.

Returns

You can purchase in confidence and send the items back to us if they are not right for you. If you would like to initiate a return, please go to your account at the top right corner where it says your name. Click \"Create Return\" next to the order your would like to return and follow the prompts.
            """, style = black14.copy(lineHeight = 20.sp)
                                )

                            }
                        }
                        item {
                            if (productDetailState.recommendList != null) {
                                LazyHorizontalStaggeredGrid(
                                    rows = StaggeredGridCells.Fixed(1),
                                    horizontalItemSpacing = 10.dp,
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .height(350.dp)
                                ) {
                                    items(productDetailState.recommendList!!) { item ->
                                        Column(modifier = Modifier
                                            .width((screenWidth - 30.dp) / 2)
                                            .clickable {
                                                val id = Base64
                                                    .getUrlEncoder()
                                                    .encodeToString(item.id.toByteArray())
                                                navController.navigate("product_detail/${id}")
                                            }) {
                                            AsyncImage(
                                                model = if (item.images.edges.isNotEmpty()) item.images.edges[0].node.url else Constants.BigImagePlaceHolder,
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(220.dp)
                                                    .background(Color.White)
                                            )
                                            Text(
                                                text = item.title,
                                                style = blackBold16,
                                                maxLines = 1,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .padding(top = 16.dp)
                                                    .fillMaxWidth()
                                            )
                                            Text(
                                                text = item.vendor,
                                                style = black14,
                                                maxLines = 2,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .padding(top = 10.dp)
                                                    .fillMaxWidth()
                                            )
                                            Text(
                                                text = "$ ${item.variants.edges[0].node.price.amount}",
                                                style = blackBold16,
                                                maxLines = 1,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .padding(top = 16.dp)
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
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
                                val variantId =
                                    productDetailState.product!!.variants.edges[productDetailState.variantIndex].node.id
                                scope.launch {
                                    isLoading = true
                                    val result = cartVM.addProductsToCart(variantId)
                                    isLoading = false
                                    if (result) {
                                        val sr = snackbarHostState
                                            .showSnackbar(
                                                message = "Already added to bag!",
                                                actionLabel = "",
                                                duration = SnackbarDuration.Short
                                            )
                                        when (sr) {
                                            SnackbarResult.ActionPerformed -> {
                                                println("performed")
                                            }

                                            SnackbarResult.Dismissed -> {
                                                println("dismiss")
                                            }

                                            else -> {}
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add to bag",
                            textAlign = TextAlign.Center,
                            style = white16
                        )
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoadingView(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

        }
    }

    if (showSize) {
        ModalBottomSheetLayout(
            sheetState = state,
            sheetContent = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "Please select your sizes",
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
                        count = productDetailState.product!!.variants.edges.count(),
                        unfocusedCount = 2,
                        debug = false,
                        state = pickerState
                    ) { index ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            Text(
                                text = productDetailState.product!!.variants.edges[index].node.title,
                                style = black14,
                            )
                            var text = ""
                            if (productDetailState.product!!.variants.edges[index].node.quantityAvailable!! <= 0) {
                                text = "Out of stock"
                            } else if (productDetailState.product!!.variants.edges[index].node.quantityAvailable!! < 5) {
                                text = "Low stock"
                            }
                            Text(
                                text = text,
                                style = grey9e9e9e14,
                            )
                        }
                    }

                    var enable = true
                    if (pickerState.currentIndex > -1) {
                        enable =
                            productDetailState.product!!.variants.edges[pickerState.currentIndex].node.quantityAvailable!! > 0
                    }

                    Box(
                        modifier = Modifier
                            .padding(16.dp, 16.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(if (enable) Color.Black else Greybdbdbd)
                            .clickable {
                                scope.launch {
                                    state.hide()
                                }
                                vm.sizeClick(index = pickerState.currentIndex)
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = if (enable) "Select" else "Out of stock",
                            textAlign = TextAlign.Center,
                            style = white16
                        )
                    }
                }
            }
        ) {}
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter //Change to your desired position
    ) {
        SnackbarHost(hostState = snackbarHostState) {
            Snackbar(snackbarData = it, backgroundColor = Color.Black)
        }
    }
}
