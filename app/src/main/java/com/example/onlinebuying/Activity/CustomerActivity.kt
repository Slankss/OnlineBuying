package com.example.onlinebuying.Activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.onlinebuying.Model.CustomerPages
import com.example.onlinebuying.Model.SellerPages
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.View.CustomerPage.AddOrderPage
import com.example.onlinebuying.View.CustomerPage.MyOrdersPage
import com.example.onlinebuying.View.CustomerPage.ProductListPage
import com.example.onlinebuying.View.SellerPage.AddProductPage
import com.example.onlinebuying.View.SellerPage.MyProductsPage
import com.example.onlinebuying.View.SellerPage.OrdersPage
import com.example.onlinebuying.View.SellerPage.ProductDetailPage
import com.example.onlinebuying.View.SellerPage.ProductList
import com.example.onlinebuying.View.SellerPage.ProfilePage
import com.example.onlinebuying.ui.theme.OnlineBuyingTheme
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Teal

class CustomerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineBuyingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CustomerrPage()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomerrPage() {
    
    
    var firebaseRepository = FirebaseRepository()
    var navController = rememberNavController()
    
    var items = listOf<CustomerPages>(
        CustomerPages.ProductListPage,
        CustomerPages.MyOrdersPage,
        CustomerPages.ProfilePage,
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
            startDestination = CustomerPages.ProductListPage.route)
        {
            composable(CustomerPages.ProductListPage.route){
                ProductListPage(navController = navController,firebaseRepository = firebaseRepository)
            }
            composable(CustomerPages.MyOrdersPage.route){
                MyOrdersPage(navController = navController,firebaseRepository = firebaseRepository)
            }
            
          
            
            composable(
                route = "${CustomerPages.AddOrderPage.route}/{product_id}",
                arguments = listOf(
                    navArgument("product_id"){
                        type = NavType.IntType
                    }
                )
            )
            {
                
                AddOrderPage(
                    navController = navController,
                    firebaseRepository = firebaseRepository,
                    it.arguments?.getInt("product_id")
                )
            }
            
            composable(CustomerPages.ProfilePage.route){
                ProfilePage(navController = navController, firebaseRepository = firebaseRepository)
            }
            
        }
    }
    
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    OnlineBuyingTheme {
    
    }
}