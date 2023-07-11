package com.example.onlinebuying.View

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.onlinebuying.Model.Pages
import com.example.onlinebuying.Model.StoreData
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.ui.theme.Navy
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Red
import kotlinx.coroutines.launch
import javax.security.auth.login.LoginException

@Composable
fun TrailerPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
    ){

    var context = LocalContext.current as Activity
    val scope = rememberCoroutineScope()

    val storeData = StoreData(context)

    BackHandler {
        context.finish()
    }

    Column(
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
            )
        ,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.web_shopping),
            contentDescription = "Web Shopping image"
        )


        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            text = stringResource(id = R.string.trailer_text),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Thin,
            textAlign = TextAlign.Center,
            color = Color.Black,
            lineHeight = 40.sp,
            fontSize = 24.sp
        )


        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(7.dp),
            text = "Devam",
            fontSize = 18,
            textColor = Color.White,
            containerColor = Navy,
            iconId = null
        ) {
            scope.launch {
                storeData.saveData(false)
            }
            navController.navigate(Pages.LoginPage.name)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TrailerPagePreview(){
    //TrailerPage(navController = rememberNavController())
}