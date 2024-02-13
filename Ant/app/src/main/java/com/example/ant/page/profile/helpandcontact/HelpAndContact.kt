package com.example.ant.page.profile.helpandcontact

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.InputView
import com.example.ant.common.view.TopAppBar
import com.example.ant.common.view.wheel_picker.FVerticalWheelPicker
import com.example.ant.common.view.wheel_picker.logMsg
import com.example.ant.common.view.wheel_picker.rememberFWheelPickerState
import com.example.ant.model.input.InputModel
import com.example.ant.ui.theme.black14
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.blackBold16
import com.example.ant.ui.theme.grey9e9e9e14u
import com.example.ant.ui.theme.white16
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HelpAndContactPage(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    var related by remember {
        mutableStateOf(true)
    }

    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    val scope = rememberCoroutineScope()

    val typeData = listOf<String>(
        "Trouble placing an order",
        "Product information",
        "Status of my order",
        "Delivery tracking",
        "Product I received",
        "Returns",
        "Refunds",
        "Change my address"
    )

    val pickerState = rememberFWheelPickerState()

    var type by remember {
        mutableStateOf("")
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                text = "Help and Contact",
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
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = """To submit an inquiry simply complete the contact form below and tap ‘Send’.  We aim to get back to you in one business day.

For information relating to common questions and inquiries please see the links below:
    """,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                                style = black16
                            )
                            Text(
                                text = """Frequently Asked Questions
Orders and Shipping
Returns and Refunds""",
                                modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                                style = grey9e9e9e14u
                            )

                            Text(
                                text = "Contact Form",
                                style = blackBold16,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    item {
                        InputView(
                            item = InputModel(title = "Name"), modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp, horizontal = 16.dp)
                        )
                    }
                    item {
                        InputView(
                            item = InputModel(title = "Email"), modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp, horizontal = 16.dp)
                        )
                    }
                    item {
                        InputView(
                            item = InputModel(
                                title = "Phone",
                                optional = true,
                                required = false
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp, horizontal = 16.dp)
                        )
                    }
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Is this Enquiry related to an existing order?*",
                                style = black16
                            )
                            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        related = !related
                                    }) {
                                    Icon(
                                        painterResource(id = if (related) R.drawable.selected else R.drawable.unselected),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Yes",
                                        style = black14,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        related = !related
                                    }) {
                                    Icon(
                                        painterResource(id = if (!related) R.drawable.selected else R.drawable.unselected),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "No",
                                        style = black14,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        InputView(
                            item = InputModel(
                                title = "Enquiry Type",
                                text = type,
                                placeHolder = "Tell us about your enquiry",
                                rightImg = R.drawable.arrow_down,
                                callBack = {
                                    scope.launch {
                                        state.show()
                                    }
                                }), modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp, horizontal = 16.dp)
                        )
                    }

                    item {
                        InputView(
                            item = InputModel(
                                title = "Message",
                                placeHolder = "Type your message here"
                            ), modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp, horizontal = 16.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp, 16.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Black)
                        .border(width = 1.dp, color = Color.Black, shape = RectangleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Send",
                        textAlign = TextAlign.Center,
                        style = white16
                    )
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
                            count = typeData.count(),
                            unfocusedCount = 2,
                            debug = true,
                            state = pickerState
                        ) { index ->
                            Text(text = typeData[index], style = black14)
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
                                    type = typeData[pickerState.currentIndex]
                                    scope.launch {
                                        state.hide()
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
    }
}


