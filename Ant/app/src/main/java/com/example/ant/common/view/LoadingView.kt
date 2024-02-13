package com.example.ant.common.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ant.ui.theme.Black
import com.example.ant.ui.theme.Greyeeeeee

@Composable
fun LoadingView(modifier: Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            strokeWidth = 2.dp,
            modifier = Modifier.width(38.dp),
            color = Black,
        )
    }
}
