package com.example.onlinebuying.View.LoginPage

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onlinebuying.Model.Pages
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.LoginViewModel
import com.example.onlinebuying.ViewModel.RegisterViewModel
import com.example.onlinebuying.ViewModelFactory.RegisterViewModelFactory
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.Widgets.CustomSnackkBar
import com.example.onlinebuying.Widgets.FailedDialog
import com.example.onlinebuying.Widgets.LoadingDialog
import com.example.onlinebuying.Widgets.PasswordOutlinedTextField
import com.example.onlinebuying.Widgets.SuccessDialog
import com.example.onlinebuying.Widgets.UsernameOutlinedTextField
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Red
import com.example.onlinebuying.ui.theme.Teal
import com.google.rpc.context.AttributeContext.Auth
import kotlinx.coroutines.launch

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
    var context = LocalContext.current as Activity

    var emailErrorState by remember { mutableStateOf(false) }
    var passwordErrorState by remember { mutableStateOf(false) }
    var passwordAgainErrorState by remember { mutableStateOf(false) }


    var registerViewModel : RegisterViewModel = viewModel(factory = RegisterViewModelFactory(firebaseRepository))

    var process = registerViewModel.processOf.collectAsState()
    
    when(process.value){
        is AuthProcessOf.Success -> {
            SuccessDialog(text = "Kayıt olundu") {
                registerViewModel.setProcess(AuthProcessOf.NotStarted)
                navController.navigate(Pages.CreateProfilePage.name)
            }
        }
        is AuthProcessOf.Error -> {
            var errorMessage = (process.value as AuthProcessOf.Error).errorMessage
            FailedDialog(text = errorMessage) {
                registerViewModel.setProcess(AuthProcessOf.NotStarted)
            }
        }
        is AuthProcessOf.Loading -> {
            LoadingDialog()
        }
        else -> {
        
        }
    }
    
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
                .padding(
                    start = 25.dp,
                    end = 25.dp
                ),
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
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }


                    UsernameOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = email,
                        labelText = "Email",
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
                        passwordVisibility = false,
                        isThereTrailingIcon = false,
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
                        passwordVisibility = false,
                        isThereTrailingIcon = false,
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
                                registerViewModel.register(email, password){
                                    when(it){
                                        is AuthProcessOf.Success -> navController.navigate(Pages.CreateProfilePage.name)
                                        is AuthProcessOf.Error -> {
                                            var errorMessage = it.errorMessage
                                            scope.launch {
                                                snackbarHostState.showSnackbar(message = errorMessage)
                                            }
                                        }
                                        else -> {
                                        
                                        }
                                    }
                                }
                            }
                            else{
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = "Girdiğiniz değerleri kontrol edin")
                                }
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
    if(password.isNotBlank() && passwordAgain.isNotBlank() && password != passwordAgain)
        return false

    return true
}
