package com.example.ant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ant.ui.theme.AntTheme
import com.example.ant.routes.AppNavigation
import com.example.ant.ui.theme.Black
import com.example.ant.ui.theme.Greybdbdbd
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainUI()
                }
            }
        }
    }
}

@Composable
fun MainUI() {
    val navController = rememberNavController()
    BottomTab(navController = navController)
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomTab(navController: NavHostController) {
    val selectedItem = remember {
        mutableStateOf("Home")
    }
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        "home" -> {
            bottomBarState.value = true
            selectedItem.value = "Home"
        }

        "designers" -> {
            bottomBarState.value = true
            selectedItem.value = "Designers"
        }

        "search" -> {
            bottomBarState.value = true
            selectedItem.value = "Search"
        }

        "wishlist" -> {
            bottomBarState.value = true
            selectedItem.value = "Wishlist"
        }

        "profile" -> {
            bottomBarState.value = true
            selectedItem.value = "Profile"
        }
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomAppBar(
                navController = navController,
                bottomBarState = bottomBarState,
                selectedItem = selectedItem
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
            AppNavigation(navController = navController, bottomBarState = bottomBarState)
        }
    }
}


//@Preview(showBackground = true)
@ExperimentalAnimationApi
@Composable
fun BottomAppBar(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    selectedItem: MutableState<String>
) {
    // items list
    val bottomMenuItemsList = prepareBottomMenu()

    val contextForToast = LocalContext.current.applicationContext

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BottomNavigation(
            modifier = Modifier
                .background(color = Color.White)
        ) {
            // this is a row scope
            // all items are added horizontally

            bottomMenuItemsList.forEach { menuItem ->
                // adding each item
                BottomNavigationItem(
                    selected = (selectedItem.value == menuItem.label),
                    onClick = {
                        selectedItem.value = menuItem.label
                        navController.navigate(menuItem.route)
                    },
                    icon = {
                        if (selectedItem.value == menuItem.label) {
                            Icon(
                                painterResource(id = menuItem.iconSelect),
                                tint = Black,
                                contentDescription = menuItem.label,
                                modifier = Modifier.size(size = 20.dp)
                            )
                        } else {
                            Icon(
                                painterResource(id = menuItem.icon),
                                tint = Greybdbdbd,
                                contentDescription = menuItem.label,
                                modifier = Modifier.size(size = 20.dp)
                            )
                        }
                    },
                    label = {
                        if (selectedItem.value == menuItem.label) {
                            Text(text = menuItem.label, fontSize = 8.sp, color = Black)
                        } else {
                            Text(text = menuItem.label, fontSize = 8.sp, color = Greybdbdbd)
                        }
                    },
                    enabled = true,
                    modifier = Modifier.background(color = Color.White)
                )
            }
        }
    }
}

private fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

    // add menu items
    bottomMenuItemsList.add(
        BottomMenuItem(
            label = "Home",
            icon = R.drawable.tab_home,
            iconSelect = R.drawable.tab_home_s,
            route = "home"
        )
    )
    bottomMenuItemsList.add(
        BottomMenuItem(
            label = "Designers",
            icon = R.drawable.tab_designers,
            iconSelect = R.drawable.tab_designers_s,
            route = "designers"
        )
    )
    bottomMenuItemsList.add(
        BottomMenuItem(
            label = "Search",
            icon = R.drawable.tab_search,
            iconSelect = R.drawable.tab_search_s,
            route = "search"
        )
    )
    bottomMenuItemsList.add(
        BottomMenuItem(
            label = "Wishlist",
            icon = R.drawable.tab_wishlist,
            iconSelect = R.drawable.tab_wishlist_s,
            route = "wishlist"
        )
    )
    bottomMenuItemsList.add(
        BottomMenuItem(
            label = "Profile",
            icon = R.drawable.tab_profile,
            iconSelect = R.drawable.tab_profile_s,
            route = "profile"
        )
    )

    return bottomMenuItemsList
}

data class BottomMenuItem(val label: String, val icon: Int, var iconSelect: Int, var route: String)
