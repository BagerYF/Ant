package com.example.ant.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ant.R
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.red16

@Composable
fun ListItem(
    text: String,
    leftImg: String? = null,
    rightArrow: Boolean = true,
    bottomLine: Boolean = true,
    click: (() -> Unit)? = null
) {
    Box() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    if (click != null) {
                        click()
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .weight(1F)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leftImg != null) {
                    if (leftImg.isNotEmpty()) {
                        AsyncImage(
                            model = leftImg,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(28.dp)
                        )
                    }
                }

                if (text == "Sale") {
                    Text(text = text, modifier = Modifier.weight(1F), style = red16)
                } else {
                    Text(text = text, modifier = Modifier.weight(1F), style = black16)
                }

                if (rightArrow) {
                    Icon(
                        painterResource(id = R.drawable.arrow),
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }
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