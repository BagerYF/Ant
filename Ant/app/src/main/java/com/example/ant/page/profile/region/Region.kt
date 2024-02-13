package com.example.ant.page.profile.region

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
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.white16

@Composable
fun RegionPage(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = "Region", backType = AppBarBackType.BACK, navController = navController)
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
                    text = "Select your preferred region.",
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    style = black16
                )
                Text(
                    text = "Region",
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                    style = black16
                )


                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https://d1mp1ehq6zpjr9.cloudfront.net/static/images/flags/CN.png",
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(28.dp)
                    )

                    Text(
                        text = "China, USD \$",
                        modifier = Modifier.weight(1F),
                        style = black16
                    )

                    Icon(
                        painterResource(id = R.drawable.arrow_down),
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Greyeeeeee)
                )
                
                Box(modifier = Modifier.weight(1f))

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
                        text = "Update Region",
                        textAlign = TextAlign.Center,
                        style = white16
                    )
                }

            }
        }
    }
}