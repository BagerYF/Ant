package com.example.ant.page.profile.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.white16

@Composable
fun NotificationsPage(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    var checked by remember { mutableStateOf(true) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = "Notifications", backType = AppBarBackType.BACK, navController = navController)
        },
    )
    {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Receive push notifications from us to get latest offers and promotion.",
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    style = black16
                )

                Row(
                    modifier = Modifier
                        .padding(vertical = 6.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Push Notifications",
                        modifier = Modifier.weight(1F),
                        style = black16
                    )

                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = Color.Black,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.Gray,
                        )
                    )
                }

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