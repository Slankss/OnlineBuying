package com.example.onlinebuying.View

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onlinebuying.Model.Pages
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.RegisterViewModel
import com.example.onlinebuying.ViewModelFactory.RegisterViewModelFactory
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.Widgets.PasswordOutlinedTextField
import com.example.onlinebuying.Widgets.UsernameOutlinedTextField
import com.example.onlinebuying.ui.theme.Navy
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // import androidx.compose.runtime.getValue
    // import androidx.compose.runtime.setValue
    // bunları importlaman lazım aşağıdaki gibi kullanman için
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }

    var emailErrorState by remember { mutableStateOf(false) }
    var passwordErrorState by remember { mutableStateOf(false) }
    var passwordAgainErrorState by remember { mutableStateOf(false) }


    var registerViewModel : RegisterViewModel = viewModel(factory = RegisterViewModelFactory(firebaseRepository))

    Scaffold(

    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.White,
                            Orange
                        )
                    )
                )
            ,
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
                .weight(6f)
                .padding(start = 25.dp, end = 25.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Icon(
                            painterResource(id = R.drawable.ic_back),
                            contentDescription = "Geri dön",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(42.dp)
                                .align(Alignment.CenterStart)
                                .clickable {
                                    navController.navigate(Pages.LoginPage.name)
                                }
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text =  "Hesap Oluştur",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }


                    UsernameOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = email,
                        labelText = "Kullanıcı adı",
                        errorState = emailErrorState,
                        iconClick =
                        {
                            email = ""
                        },
                        onValueChange = {
                            email = it
                        }
                    )

                    PasswordOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = password,
                        labelText = "Parola",
                        errorState = passwordErrorState,
                        passwordVisibility = null,
                        iconClick =
                        {

                        },
                        onValueChange = {
                            password = it
                        }
                    )
                    PasswordOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = passwordAgain,
                        labelText = "Parola tekrar",
                        errorState = passwordAgainErrorState,
                        passwordVisibility = null,
                        iconClick =
                        {

                        },
                        onValueChange = {
                            passwordAgain = it
                        }
                    )

                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        text = "Kayıt Ol",
                        fontSize = 18,
                        textColor = Color.White,
                        containerColor = Red,
                        iconId = null,
                        onClick = {
                            var result = checkRegister(email.trim(),password.trim(),passwordAgain.trim())

                            emailErrorState = email.isBlank()
                            passwordErrorState = password.isBlank()
                            passwordAgainErrorState = passwordAgain.isBlank()

                            if(result){

                            }

                        }
                    )

                }


            Box(
                modifier = Modifier
                    .weight(4f)
            ){
                Image(
                    painterResource(id = R.drawable.user_research),
                    contentDescription = "Image",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }

        }
    }

}

fun checkRegister(
    email  :String,password : String,
    passwordAgain  :String) : Boolean
{

    if(email.isBlank())
        return false
    if(password.isBlank())
        return false
    if(passwordAgain.isBlank())
        return false
    if(password.isNotBlank() && passwordAgain.isNotBlank() && password == passwordAgain)
        return false

    return true
}
