package com.example.onlinebuying.View.LoginPage

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.onlinebuying.ui.theme.Orange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.onlinebuying.Activity.CustomerActivity
import com.example.onlinebuying.Activity.SellerActivity
import com.example.onlinebuying.Model.Pages
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.FirstPageViewModel
import com.example.onlinebuying.ViewModelFactory.FirstPageViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun FirstPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(composition = composition)

    val context = LocalContext.current as Activity
    var scope = rememberCoroutineScope()
    val viewModel : FirstPageViewModel = viewModel(factory = FirstPageViewModelFactory(firebaseRepository,context))

    var page = viewModel.page.collectAsState()
    
    when(page.value){
        Pages.SellerPage.name -> {
            context.startActivity(Intent(context,SellerActivity::class.java))
            context.finish()
            viewModel.setPage(null)
        }
        Pages.CustomerPage.name -> {
            context.startActivity(Intent(context,CustomerActivity::class.java))
            context.finish()
            viewModel.setPage(null)
        }
        null -> {
        
        }
        else -> {
            viewModel.setPage(null)
            navController.navigate(page.value!!)
    
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Orange,
                        Color.White,
                        Color.White
                    )
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ){
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 50.dp,
                    end = 50.dp,
                    top = 100.dp
                ),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun FirstPagePreview(){
    //FirstPage()
}