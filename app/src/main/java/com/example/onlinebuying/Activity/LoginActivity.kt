package com.example.onlinebuying.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onlinebuying.Model.Pages
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.View.LoginPage.CreateProfilePage
import com.example.onlinebuying.View.LoginPage.FirstPage
import com.example.onlinebuying.View.LoginPage.LoginPage
import com.example.onlinebuying.View.LoginPage.RegisterPage
import com.example.onlinebuying.View.LoginPage.TrailerPage
import com.example.onlinebuying.ui.theme.OnlineBuyingTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineBuyingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {

    var navController = rememberNavController()
    var firebaseRepository = FirebaseRepository()

    NavHost(
        navController = navController,
        startDestination = Pages.FirstPage.name){


        composable(Pages.FirstPage.name){
            FirstPage(navController,firebaseRepository)
        }

        composable(Pages.TrailerPage.name){
            TrailerPage(navController,firebaseRepository)
        }

        composable(Pages.LoginPage.name){
            LoginPage(navController,firebaseRepository)
        }

        composable(Pages.RegisterPage.name){
            RegisterPage(navController,firebaseRepository)
        }


        composable(Pages.CreateProfilePage.name){
            CreateProfilePage(navController,firebaseRepository)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OnlineBuyingTheme {
       App()
    }
}