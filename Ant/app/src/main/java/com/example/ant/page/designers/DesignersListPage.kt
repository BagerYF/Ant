package com.example.ant.page.designers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.ListItem
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black12
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.blackBold16
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ant.ui.theme.grey75757516

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DesignersListPage(navController: NavHostController, vm: DesignersListVM = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit, block = {

    })

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = "Designers A-Z", backType = AppBarBackType.BACK, navController = navController)
        },
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    vm.searchText = it
                    vm.searchData()
                },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Greyeeeeee)
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
            Row(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                        .width(screenWidth - 20.dp), state = listState
                ) {
                    for (item in vm.showData) {
                        item {
                            Text(
                                text = item.key,
                                style = blackBold16,
                                modifier = Modifier
                                    .padding(16.dp, 10.dp)
                            )
                        }
                        items(item.value) { sItem ->
                            ListItem(text = sItem.name)
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .width(20.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(vm.group) { item ->
                        Text(text = item, style = black12, modifier = Modifier.clickable {
                            coroutineScope.launch {
                                var index = 0
                                for (s in vm.showData) {
                                    if (s.key == item) {
                                        break
                                    } else {
                                        index += s.value.size + 1
                                    }
                                }
                                listState.animateScrollToItem(index = index)
                            }
                        })
                    }
                }
            }

        }
    }
}