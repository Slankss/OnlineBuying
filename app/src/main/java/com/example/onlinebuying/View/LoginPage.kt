package com.example.onlinebuying.View

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.onlinebuying.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.Widgets.CustomSnackkBar
import com.example.onlinebuying.Widgets.PasswordOutlinedTextField
import com.example.onlinebuying.Widgets.UsernameOutlinedTextField
import com.example.onlinebuying.ui.theme.Navy
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Red
import com.example.onlinebuying.ui.theme.Teal
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // import androidx.compose.runtime.getValue
    // import androidx.compose.runtime.setValue
    // bunları importlaman lazım aşağıdaki gibi kullanman için
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameErrorState by remember { mutableStateOf(false) }
    var passwordErrorState by remember { mutableStateOf(false) }

    var passwordVisibility by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState){ snackBarData ->
                CustomSnackkBar(
                    message = snackBarData.visuals.message,
                    containerColor = Teal,
                    icon = null
                )
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Orange
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White,
                                Color.White,
                                Orange
                            )
                        )
                    )
                    .fillMaxWidth()
                    .weight(5f)
            ){
                Image(
                    painterResource(
                        id = R.drawable.social_engagement
                    ),
                    contentDescription = "Image",
                    modifier = Modifier
                        .align(Alignment.Center),
                )

            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(5f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 7.dp,
                ),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 25.dp, end = 25.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    UsernameOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = username,
                        labelText = "Kullanıcı adı",
                        errorState = usernameErrorState,
                        iconClick =
                        {
                            username = ""
                        },
                        onValueChange = {
                            usernameErrorState = false
                            username = it
                        }
                    )

                    PasswordOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = password,
                        labelText = "Parola",
                        errorState = passwordErrorState,
                        passwordVisibility = passwordVisibility,
                        iconClick =
                        {
                            passwordVisibility = !passwordVisibility
                        },
                        onValueChange = {
                            passwordErrorState = false
                            password = it
                        }
                    )

                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        text = "Giriş Yap",
                        fontSize = 18,
                        textColor = Color.White,
                        containerColor = Red,
                        iconId = null,
                        onClick = {
                            check(username.trim(),password.trim()){ isUsernameEmpty,isPasswordEmpty ->
                                usernameErrorState = isUsernameEmpty
                                passwordErrorState = isPasswordEmpty

                                if(!isUsernameEmpty && !isPasswordEmpty){
                                    // login işlemleri yapılabilir
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message =  "Başarıyla giriş yaptınız"
                                        )
                                    }
                                }
                            }
                        }
                    )

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Hesabın yok mu?",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "Kayıt ol",
                            color = Red,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {

                                }

                        )
                    }
                }
            }
        }

    }

}

fun check(username  :String,password : String,
          errorListener : (Boolean,Boolean) -> Unit)
{

    var isUsernameEmpty = false
    var isPasswordEmpty = false

    if(username.isBlank()){
        isUsernameEmpty = true
    }
    if(password.isBlank()){
        isPasswordEmpty = true
    }
    errorListener(isUsernameEmpty,isPasswordEmpty)

}
