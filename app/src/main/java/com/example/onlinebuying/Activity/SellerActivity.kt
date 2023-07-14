package com.example.onlinebuying.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.onlinebuying.theme.OnlineBuyingTheme
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.onlinebuying.Model.SellerPages
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.View.AddProductPage
import com.example.onlinebuying.View.MyProductsPage
import com.example.onlinebuying.View.OrdersPage
import com.example.onlinebuying.View.ProductDetailPage
import com.example.onlinebuying.View.ProfilePage
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Teal

class SellerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineBuyingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SellerPage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SellerPage() {


    var firebaseRepository = FirebaseRepository()
    var navController = rememberNavController()

    var items = listOf(
        SellerPages.MyProductsPage,
        SellerPages.AddProductPage,
        SellerPages.OrdersPage
    )


    Scaffold(
        bottomBar =
        {
            BottomNavigation(
                modifier = Modifier
                    .background(
                        color = Color.White
                    )
                    .defaultMinSize(minHeight = 75.dp)
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    )
                    .clip(RoundedCornerShape(90.dp)),
                backgroundColor = Teal
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach{ page ->
                    var isSelected = currentRoute == page.route
                    BottomNavigationItem(
                        modifier = Modifier
                            .background(
                                color = if(isSelected) Orange else Teal
                            ),
                        selected = isSelected,
                        label =
                            {
                            Text(
                                text =  page.label,
                                color = when(isSelected){
                                    true -> Color.White
                                    false -> Color.Black
                                }
                            )
                            },
                        icon =
                            {
                            Icon(
                                modifier = Modifier
                                    .padding(bottom = 7.dp)
                                    .size(28.dp),
                                painter = painterResource(id = page.iconId!!),
                                contentDescription = null,
                                tint = when(isSelected){
                                    true -> Color.White
                                    false -> Color.Black
                                }
                            )
                            },
                        onClick =
                        {
                            navController.navigate(page.route){
                                launchSingleTop = true
                                restoreState = true
                            }

                        }
                    )



                }

            }
        }
    )
    { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = SellerPages.MyProductsPage.route)
        {
            composable(SellerPages.MyProductsPage.route){
                MyProductsPage(navController = navController,firebaseRepository = firebaseRepository)
            }
            composable(SellerPages.AddProductPage.route){
                AddProductPage(navController = navController,firebaseRepository = firebaseRepository)
            }
            composable(SellerPages.OrdersPage.route){
                OrdersPage(navController = navController,firebaseRepository = firebaseRepository)
            }
            composable(SellerPages.ProfilePage.route){
                ProfilePage(navController = navController,firebaseRepository = firebaseRepository)
            }
            
            composable(
                route = "${SellerPages.ProductDetailPage.route}/{product_id}",
                arguments = listOf(
                    navArgument("product_id"){
                        type = NavType.IntType
                    }
                )
                )
            {
                
                ProductDetailPage(
                    navContreller = navController,
                    firebaseRepository = firebaseRepository,
                    it.arguments?.getInt("product_id")
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    OnlineBuyingTheme {
    }
}