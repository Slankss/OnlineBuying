package com.example.onlinebuying.Widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.onlinebuying.R
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip

@Composable
fun LoadingDialog(){

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

    Dialog(
        onDismissRequest = {

        }
    ) {
        Surface(
            modifier = Modifier
                .background(
                    color = Color.White
                )
                .clip(RoundedCornerShape(24.dp))
        )
        {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                LottieAnimation(
                    modifier = Modifier
                        .size(width = 300.dp, height = 250.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,

                    )
                Text(
                    text = "İşlem devam ediyor...",
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }

        }
    }

}

@Composable
fun SuccessDialog(text : String,onComplete : () -> Unit){

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val progress by animateLottieCompositionAsState(composition = composition)

    var dialogVisibility by remember { mutableStateOf(true) }

    if(dialogVisibility){
        Dialog(
            onDismissRequest = {

            }
        ) {
            Surface(
                modifier = Modifier
                    .background(
                        color = Color.White
                    )
                    .clip(RoundedCornerShape(24.dp)),

            )
            {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .size(width = 300.dp, height = 250.dp),
                        composition = composition,
                        progress = progress
                    )

                    Text(
                        text = text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }

            }
        }

    }

    if (progress == 1.0f) {
        dialogVisibility = false
        onComplete()
    }

}

@Composable
fun FailedDialog(text : String,onComplete : () -> Unit){

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.failed))
    val progress by animateLottieCompositionAsState(composition = composition)

    var dialogVisibility by remember { mutableStateOf(true) }

    if(dialogVisibility){
        Dialog(
            onDismissRequest = {

            }
        ) {
            Surface(
                modifier = Modifier
                    .background(
                        color = Color.White
                    )
                    .clip(RoundedCornerShape(24.dp))
            )
            {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .size(width = 300.dp, height = 250.dp),
                        composition = composition,
                        progress = progress
                    )

                    Text(
                        text = text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }

            }
        }

    }

    if (progress == 1.0f) {
        dialogVisibility = false
        onComplete()
    }

}

