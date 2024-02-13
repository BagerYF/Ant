package com.example.ant.page.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.CollectionQuery
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.config.Constants
import com.example.ant.model.product.ProductSortModel
import com.example.ant.page.cart.CartVM
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.blackBold16
import com.example.ant.ui.theme.grey9e9e9e14
import com.example.type.ProductCollectionSortKeys
import kotlinx.coroutines.launch
import java.util.Base64

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductListPage(
    navController: NavHostController,
    vm: ProductListVM = hiltViewModel(),
    cartVM: CartVM
) {
    val scaffoldState = rememberScaffoldState()

    val filterState by vm.filterState.collectAsState()

    var productList: LazyPagingItems<CollectionQuery.Edge> = vm.products.collectAsLazyPagingItems()

    var showSortOrFilter by remember {
        mutableStateOf(false)
    } // true: sort , false: filter

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    var selectSortModel by remember {
        mutableStateOf(
            ProductSortModel(
                "Most Relevant",
                false,
                ProductCollectionSortKeys.RELEVANCE
            )
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Products",
                backType = AppBarBackType.BACK,
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.White)
                            .padding(vertical = 10.dp)
                            .clickable {
                                showSortOrFilter = false
                                scope.launch {
                                    state.show()
                                }
                            }) {
                        Icon(
                            painterResource(id = R.drawable.product_refine),
                            contentDescription = "",
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Refine",
                            style = black14,
                            modifier = Modifier
                                .padding(horizontal = 6.dp, vertical = 0.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.White)
                            .padding(vertical = 10.dp)
                            .clickable {
                                showSortOrFilter = true
                                scope.launch {
                                    state.show()
                                }
                            }) {
                        Icon(
                            painterResource(id = R.drawable.product_down_arrow),
                            contentDescription = "",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Sort:",
                            style = black14,
                            modifier = Modifier
                                .padding(horizontal = 6.dp, vertical = 0.dp)
                        )
                        Text(
                            text = selectSortModel.name,
                            style = grey9e9e9e14,
                        )
                    }
                }
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    items(productList.itemCount) { index ->
                        val item = productList[index]!!.node
                        Column(modifier = Modifier.clickable {
                            println(item.id)
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

                    productList.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                //You can add modifier to manage load state when first time response page is loading
                            }

                            loadState.append is LoadState.Loading -> {
                                //You can add modifier to manage load state when next response page is loading
                            }

                            loadState.append is LoadState.Error -> {
                                //You can use modifier to show error message
                            }
                        }
                    }
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            if (showSortOrFilter) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "Sort by",
                            style = black14,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painterResource(id = R.drawable.nav_close),
                            contentDescription = "",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    for (item in vm.sortList) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clickable {
                                    selectSortModel = item
                                    vm.selectSortModel = item
                                    productList.refresh()
                                    scope.launch {
                                        state.hide()
                                    }
                                }
                        ) {
                            Image(
                                painter = painterResource(id = if (item.name == selectSortModel.name) R.drawable.check_y else R.drawable.check_n_),
                                contentDescription = ""
                            )
                            Text(
                                text = item.name,
                                style = black14,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp)
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(630.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "Filter",
                            style = black14,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                vm.clearFilter()
                                productList.refresh()
                            }) {
                            Text(
                                text = "Clear",
                                style = black14,
                            )
                            Icon(
                                painterResource(id = R.drawable.nav_close),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(14.dp)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color = Greyeeeeee)
                    )
                    Row {
                        LazyColumn(modifier = Modifier.width(100.dp)) {
                            itemsIndexed(filterState.allFilters) { index, item ->
                                Column(modifier = Modifier
                                    .padding(start = 10.dp)
                                    .clickable {
                                        vm.leftFilterClick(index)
                                    }) {
                                    Text(
                                        item.label,
                                        style = if (filterState.subFiltersIndex == index) black14 else grey9e9e9e14,
                                        maxLines = 1,
                                        modifier = Modifier
                                            .height(40.dp)
                                            .padding(top = 10.dp)
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(color = Greyeeeeee)
                                    )
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(color = Greyeeeeee)
                        )
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            itemsIndexed(filterState.subFilters) { index, item ->
                                Column(modifier = Modifier
                                    .padding(start = 10.dp)
                                    .clickable {
                                        vm.rightFilterClick(
                                            index = index,
                                            selected = !vm.checkFilterIsExist(item.input as String)
                                        )
                                        productList.refresh()
                                    }) {
                                    Text(
                                        "${item.label}(${item.count})",
                                        style = if (vm.checkFilterIsExist(item.input as String)) black14 else grey9e9e9e14,
                                        modifier = Modifier
                                            .height(40.dp)
                                            .padding(top = 10.dp)
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(color = Greyeeeeee)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {}
}