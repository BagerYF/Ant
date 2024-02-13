package com.example.ant.page.wishlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import coil.compose.AsyncImage
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.config.Constants
import com.example.ant.page.cart.CartVM
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.blackBold16
import kotlinx.coroutines.launch
import java.util.Base64

@Composable
fun WishlistPage(navController: NavHostController, wishlistVM: WishlistVM, cartVM: CartVM) {
    val scaffoldState = rememberScaffoldState()

    val wishlistState by wishlistVM.wishlistState.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Wishlist",
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
        ) {
            if (wishlistState.wishlist.isNotEmpty()) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    items(wishlistState.wishlist) { item ->
                        Column(modifier = Modifier.clickable {
                            println(item.id)
                            val id = Base64
                                .getUrlEncoder()
                                .encodeToString(item.id.toByteArray())
                            navController.navigate("product_detail/${id}")
                        }) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, bottom = 6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.cart_delete_product),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable {
                                            wishlistVM.removeWishlist(item)
                                        }
                                )
                            }
                            AsyncImage(
                                model = if (item.image.isNotEmpty()) item.image else Constants.BigImagePlaceHolder,
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
                                text = "$ ${item.price}",
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
            } else {
                Text(
                    text = "Your wishlist is currently empty",
                    style = black16,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }
    }
}