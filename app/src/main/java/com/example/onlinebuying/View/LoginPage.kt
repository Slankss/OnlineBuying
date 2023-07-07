package com.example.onlinebuying.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.onlinebuying.Repository.FirebaseRepository

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
    ){

    Scaffold(

    ) {

        Column {
            Text(text = "LOGIN")
        }

    }



}