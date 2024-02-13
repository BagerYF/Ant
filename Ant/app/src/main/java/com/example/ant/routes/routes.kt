package com.example.ant.routes

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.CustomerQuery
import com.example.ant.model.category.CategoryModel
import com.example.ant.model.country.AllCountryModel
import com.example.ant.page.cart.CartPage
import com.example.ant.page.cart.CartVM
import com.example.ant.page.checkout.CheckoutPage
import com.example.ant.page.designers.DesignersListPage
import com.example.ant.page.designers.DesignersPage
import com.example.ant.page.home.HomePage
import com.example.ant.page.login.LoginPage
import com.example.ant.page.login.LoginVM
import com.example.ant.page.product.ProductDetailPage
import com.example.ant.page.product.ProductListPage
import com.example.ant.page.profile.ProfilePage
import com.example.ant.page.profile.address.AddressDetailPage
import com.example.ant.page.profile.address.AddressListPage
import com.example.ant.page.profile.address.CountryListPage
import com.example.ant.page.profile.address.CountryListVM
import com.example.ant.page.profile.helpandcontact.HelpAndContactPage
import com.example.ant.page.profile.notifications.NotificationsPage
import com.example.ant.page.profile.order.OrderDetailPage
import com.example.ant.page.profile.order.OrderListPage
import com.example.ant.page.profile.region.RegionPage
import com.example.ant.page.profile.view.AboutPage
import com.example.ant.page.profile.view.FAQPage
import com.example.ant.page.profile.view.PrivacyPolicyPage
import com.example.ant.page.profile.view.ReturnAndRefundsPage
import com.example.ant.page.profile.view.TermsAndConditonsPage
import com.example.ant.page.search.CategoryListPage
import com.example.ant.page.search.SearchPage
import com.example.ant.page.wishlist.WishlistPage
import com.example.ant.page.wishlist.WishlistVM
import com.google.gson.Gson
import java.util.Base64

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(navController: NavHostController, bottomBarState: MutableState<Boolean>) {

    val loginVM: LoginVM = hiltViewModel()

    val cartVM: CartVM = hiltViewModel()
    val wishlistVM: WishlistVM = hiltViewModel()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomePage(navController = navController, loginVM = loginVM, cartVM = cartVM)
        }
        composable("designers") {
            DesignersPage(navController = navController, cartVM = cartVM)
        }
        composable("search") {
            SearchPage(navController = navController, cartVM = cartVM)
        }
        composable("wishlist") {
            WishlistPage(navController = navController, wishlistVM = wishlistVM, cartVM = cartVM)
        }
        composable("profile") {
            ProfilePage(navController = navController, loginVM = loginVM, cartVM = cartVM)
        }
        composable(
            "order_list",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                null
            },
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            OrderListPage(navController = navController, loginVM = loginVM)
        }
        composable(
            "order_detail/{params}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            val paramsJson = it.arguments?.getString("params")
            val paramsStr = String(Base64.getUrlDecoder().decode(paramsJson))
            val item = Gson().fromJson(paramsStr, CustomerQuery.Edge1::class.java)
            OrderDetailPage(item = item, navController = navController, loginVM = loginVM)
        }
        composable(
            "address_list",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                null
            },
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            AddressListPage(navController = navController, loginVM = loginVM)
        }
        composable(
            "address_detail/{params}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                null
            },
        ) {
            val paramsJson = it.arguments?.getString("params")
            if (paramsJson != "{}") {
                var address = Gson().fromJson(paramsJson, CustomerQuery.Edge::class.java)
                val id = String(Base64.getUrlDecoder().decode(address.node.id))
                address = address.copy(node = address.node.copy(id = id))
                AddressDetailPage(
                    address = address,
                    navController = navController,
                    loginVM = loginVM
                )
            } else {
                AddressDetailPage(navController = navController, loginVM = loginVM)
            }
        }
        composable("login/{params}", enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(500)
            )
        },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(500)
                )
            }) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            val params = it.arguments?.getString("params")
            LoginPage(
                loginOrRegis = params.toBoolean(),
                navController = navController,
                loginVM = loginVM
            )
        }
        composable(
            "designers_list",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            DesignersListPage(navController = navController)
        }
        composable(
            "category_list/{params}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            val paramsJson = it.arguments?.getString("params")
            val category = Gson().fromJson(paramsJson, CategoryModel::class.java)
            CategoryListPage(data = category, navController = navController)
        }
        composable(
            "about",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            AboutPage(navController = navController)
        }
        composable(
            "faq",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            FAQPage(navController = navController)
        }
        composable(
            "privacy_policy",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            PrivacyPolicyPage(navController = navController)
        }
        composable(
            "return_and_refunds",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            ReturnAndRefundsPage(navController = navController)
        }
        composable(
            "terms_and_conditions",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            TermsAndConditonsPage(navController = navController)
        }
        composable(
            "region",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            RegionPage(navController = navController)
        }
        composable(
            "notifications",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            NotificationsPage(navController = navController)
        }
        composable(
            "country_list/{params}",
        ) {
            val paramsJson = it.arguments?.getString("params")
            val country = Gson().fromJson(paramsJson, AllCountryModel::class.java)
            CountryListPage(
                vm = CountryListVM(countryData = country),
                navController = navController
            )
        }
        composable(
            "help_and_contact",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            HelpAndContactPage(navController = navController)
        }
        composable(
            "product_list",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                null
            },
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            ProductListPage(navController = navController, cartVM = cartVM)
        }
        composable(
            "product_detail/{params}",
            arguments = listOf(
                navArgument("params") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                null
            },
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            ProductDetailPage(
                navController = navController,
                cartVM = cartVM,
                wishlistVM = wishlistVM
            )
        }
        composable(
            "cart",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(500)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            CartPage(
                navController = navController,
                cartVM = cartVM
            )
        }
        composable(
            "checkout/{params}",
            arguments = listOf(
                navArgument("params") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                null
            },
        ) {
            LaunchedEffect(Unit) {
                bottomBarState.value = false
            }
            CheckoutPage(navController = navController, cartVM = cartVM)
        }
    }
}