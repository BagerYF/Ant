package com.example.ant.page.search

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.view.ListItem
import com.example.ant.common.view.TopAppBar
import com.example.ant.model.category.CategoryModel
import com.example.ant.model.category.kSuggestedMaps
import com.example.ant.page.cart.CartVM
import com.example.ant.ui.theme.Grey757575
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.grey75757516
import com.example.ant.ui.theme.white16
import com.google.gson.Gson
import kotlinx.coroutines.launch

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchPage(navController: NavHostController, cartVM: CartVM, vm: SearchVM = hiltViewModel()) {

    val scaffoldState = rememberScaffoldState()

    var searchText by remember { mutableStateOf("") }
    var hasFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var currentPage by remember { mutableStateOf("Women") }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val pageTitles = listOf<String>("Women", "Men", "Kids")
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            currentPage = pageTitles[page]
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (!hasFocus) {
                TopAppBar(
                    text = "Search",
                    backType = AppBarBackType.NONE,
                    showBag = true,
                    navController = navController,
                    cartVM = cartVM
                )
            }
        },
    )
    {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Row {
                BasicTextField(
                    value = searchText,
                    onValueChange = { text ->
                        searchText = text
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Greyeeeeee)
                        .focusRequester(focusRequester)
                        .onFocusChanged { f ->
                            hasFocus = f.isFocused
                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        vm.search(text = searchText)
                        searchText = ""
                    })
                ) { innerTextField ->
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = searchText,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = remember { MutableInteractionSource() },
                        contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.search),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        },
                        placeholder = { Text(text = "Search", style = grey75757516) },
                    )
                }

                if (hasFocus) {
                    Text(
                        text = "Cancel",
                        textAlign = TextAlign.Center,
                        style = black16,
                        modifier = Modifier
                            .padding(0.dp, 16.dp, 16.dp, 16.dp)
                            .width(60.dp)
                            .height(40.dp)
                            .size(40.dp)
                            .wrapContentHeight()
                            .clickable {
                                focusManager.clearFocus()
                                searchText = ""
//                                vm.clearHistory()
                            }
                    )
                }
            }

            if (!hasFocus) {

                Text(text = "Department", style = black14, modifier = Modifier.padding(16.dp, 0.dp))

                Row(modifier = Modifier.padding(16.dp)) {
                    for (item in pageTitles) {
                        if (item == currentPage) {
                            Text(
                                text = item,
                                style = white16,
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(end = 10.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.Black)
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        } else {
                            Text(
                                text = item,
                                style = grey75757516,
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(end = 10.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Grey757575,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(Color.White)
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .clickable {
                                        currentPage = item
                                        coroutineScope.launch {
                                            // Call scroll to on pagerState
                                            pagerState.scrollToPage(pageTitles.indexOf(item))
                                        }
                                    }
                            )
                        }
                    }
                }

                HorizontalPager(state = pagerState) { page ->
                    CategoryListView(data = vm.allList[page], navController = navController)
                }
            }

            if (hasFocus) {

                if (vm.historyList.isEmpty()) {
                    Text(
                        text = "Suggested",
                        style = black14,
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )

                    LazyColumn() {
                        items(kSuggestedMaps) { item ->
                            ListItem(text = item, rightArrow = false, click = {
                                navController.navigate("product_list")
                            })
                        }
                    }
                } else {
                    Text(
                        text = "Search History",
                        style = black14,
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )

                    LazyColumn() {
                        items(vm.historyList) { item ->
                            ListItem(text = item, rightArrow = false, click = {
                                navController.navigate("product_list")
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryListView(data: CategoryModel, navController: NavHostController) {
    LazyColumn() {
        items(data.children) { item ->
            ListItem(text = item.name, click = {
                val paramsJson = Gson().toJson(item)
                navController.navigate("category_list/$paramsJson")
            })
        }
    }
}