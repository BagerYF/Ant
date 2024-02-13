package com.example.ant.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.ui.theme.Black
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import com.example.ant.page.cart.CartVM
import com.example.ant.ui.theme.black10
import com.example.ant.ui.theme.black12

@Composable
fun TopAppBar(
    text: String,
    showBag: Boolean = false,
    backType: AppBarBackType = AppBarBackType.BACK,
    elevation: Dp = 1.dp,
    navController: NavHostController? = null,
    cartVM: CartVM? = null,
    backClick: (() -> Unit)? = null
) {
    TopAppBar(backgroundColor = Color.White, elevation = elevation) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (backType != AppBarBackType.NONE) {
                IconButton(
                    onClick = {
                        if (backClick != null) {
                            backClick()
                        } else {
                            navController?.popBackStack()
                        }
                    }
                ) {
                    Icon(
                        painterResource(id = ((if (backType == AppBarBackType.BACK) (R.drawable.backarrow) else (R.drawable.nav_close)) as Int)),
                        contentDescription = "",
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1F)
            )
            if (showBag && cartVM != null) {
                val cartState by cartVM.cartState.collectAsState()

                Box {
                    IconButton(
                        onClick = {
                            navController?.navigate("cart")
                        }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.nav_bag),
                            contentDescription = "",
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }
                    Text(
                        text = cartState.qty,
                        style = black10,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 14.dp)
                            .background(Color.White)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}