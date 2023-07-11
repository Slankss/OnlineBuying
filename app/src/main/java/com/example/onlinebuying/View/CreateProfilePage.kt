package com.example.onlinebuying.View

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.Widgets.UsernameOutlinedTextField
import com.example.onlinebuying.ui.theme.Orange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onlinebuying.Activity.CustomerActivity
import com.example.onlinebuying.Activity.SellerActivity
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.ViewModel.ProfilePageViewModel
import com.example.onlinebuying.ViewModelFactory.ProfilePageViewModelFactory
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.ui.theme.Teal
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun ProfilPagePreview(){
    //ProfilePage(navController = rememberNavController(), firebaseRepository = FirebaseRepository())
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateProfilePage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){


    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var is_seller by remember { mutableStateOf(false) }

    var nameErrorState by remember { mutableStateOf(false) }
    var surnameErrorState by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()
    var snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current as Activity

    var profileViewModel : ProfilePageViewModel = viewModel(factory = ProfilePageViewModelFactory(firebaseRepository))


    Scaffold(

    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Orange,
                            Color.White,
                            Color.White,
                            Color.White,
                            Color.White,
                            Orange
                        )
                    )
                ),
                verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text =  "Profil bilgileriniz",
                fontSize = 28.sp,
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
            Input(
                labelText = "Adınız",
                inputText = name,
                errorState = nameErrorState,
                iconClick = { name = "" },
                onValueChange = {
                    name = it
                }
            )
            Input(
                labelText = "Soyadınız",
                inputText = surname,
                errorState = surnameErrorState,
                iconClick = { surname = "" },
                onValueChange = {
                    surname = it
                }
            )

            Input(
                labelText = "Telefon numaranız(İsteğe bağlı)",
                inputText = phone,
                errorState = false,
                iconClick = {
                     phone = ""
                },
                onValueChange = {
                    phone = it
                }
                )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 35.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    modifier = Modifier,
                    text = "Satıcı Hesabı :",
                    color = Color.Black,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium
                )

                Checkbox(
                    checked = is_seller,
                    onCheckedChange = {
                        is_seller = it
                    }
                )

            }

            CustomButton(
                modifier = Modifier
                    .padding(horizontal = 100.dp, vertical = 25.dp),
                shape = RoundedCornerShape(14.dp),
                text = "Kaydet",
                fontSize = 18,
                textColor = Color.White,
                containerColor = Teal,
                iconId = null
            ) {

                var nameStr = name.trim()
                var surnameStr = surname.trim()
                var phoneStr = phone.trim()

                nameErrorState = name.isBlank()
                surnameErrorState = surname.isBlank()

                if(!nameErrorState && !surnameErrorState){
                   profileViewModel.createProfile(
                       name = nameStr,
                       surname = surnameStr,
                       is_seller = is_seller,
                       phone = phoneStr
                   ){ process ->
                       when(process){
                           is AuthProcessOf.Success -> {
                               var user = process.user

                               if(user != null){
                                   context.startActivity(Intent(
                                       context,
                                       when(user.seller_account){
                                           true -> SellerActivity::class.java
                                           false -> CustomerActivity::class.java
                                       }
                                   ))
                                   context.finish()
                               }

                           }
                           is AuthProcessOf.Error -> {
                               val errorMessage = process.errorMessage
                               scope.launch {
                                   snackbarHostState.showSnackbar(message =  errorMessage)
                               }
                           }
                       }
                   }
                }


            }


        }

    }


}

@Composable
fun Input(
    labelText : String,
    inputText : String,
    errorState : Boolean,
    iconClick :() -> Unit,
    onValueChange : (String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 7.dp, start = 7.dp),
            text = labelText,
            color = Color.Black,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium
        )
        UsernameOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            text = inputText,
            labelText = "",
            errorState = errorState,
            iconClick = {
              iconClick()
            },
            onValueChange ={
            onValueChange(it)
            }
            )
    }
}

