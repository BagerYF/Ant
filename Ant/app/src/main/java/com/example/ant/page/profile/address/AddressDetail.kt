package com.example.ant.page.profile.address

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.CustomerQuery
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.tools.readJSONFromAssets
import com.example.ant.common.view.InputView
import com.example.ant.common.view.TopAppBar
import com.example.ant.page.login.LoginVM
import com.example.ant.ui.theme.black12
import com.example.ant.ui.theme.white16
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddressDetailPage(
    address: CustomerQuery.Edge? = null,
    navController: NavHostController,
    loginVM: LoginVM,
    vm: AddressVM = AddressVM(
        address = address,
        countryData = readJSONFromAssets(
            context = LocalContext.current,
            path = "country_data.json"
        ), dataStoreManager = DataStoreManager(context = LocalContext.current)
    )
) {
    val scaffoldState = rememberScaffoldState()

    val addressState by vm.addressState.collectAsState()

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    vm.backCallBack = {
        loginVM.queryCustomer()
        navController.popBackStack()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Add address",
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
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    item {
                        Text(
                            text = "*Required Fields",
                            style = black12,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(addressState.addressInputList) { items ->
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
                                        item = item, modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 5.dp)
                                    )
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
                            vm.submitClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (address != null) "Update address" else "Add address",
                        textAlign = TextAlign.Center,
                        style = white16
                    )
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            CountryListPage(
                vm = CountryListVM(countryData = addressState.countryData),
                navController = navController,
                callBack = { tempCountry ->
                    scope.launch {
                        state.hide()
                    }
                    vm.selectCountry(tempCountry)
                }, backClick = {
                    scope.launch {
                        state.hide()
                    }
                })
        }
    ) {}
}