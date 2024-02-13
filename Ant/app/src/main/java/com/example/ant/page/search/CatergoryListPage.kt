package com.example.ant.page.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.ListItem
import com.example.ant.common.view.TopAppBar
import com.example.ant.model.category.CategoryModel
import com.google.gson.Gson

@Composable
fun CategoryListPage(data: CategoryModel, navController: NavHostController) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = data.name, backType = AppBarBackType.BACK, navController = navController)
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            items(data.children) { item ->
                ListItem(text = item.name, click = {
                    navController.navigate("product_list")
                })
            }
        }
    }

}