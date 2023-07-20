package com.example.onlinebuying.View.SellerPage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract.Profile
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onlinebuying.Activity.LoginActivity
import com.example.onlinebuying.Model.SellerPages
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.ProfilePageViewModel
import com.example.onlinebuying.ViewModelFactory.ProfilePageViewModelFactory
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.ui.theme.Red
import com.example.onlinebuying.ui.theme.Silver

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){
    
    var context = LocalContext.current as Activity
    var profileViewModel : ProfilePageViewModel = viewModel(factory = ProfilePageViewModelFactory(firebaseRepository))
    
    LaunchedEffect(
        key1 = true,
    ){
        profileViewModel.getUser()
    }
    
    var user = profileViewModel.user.collectAsState()

    Scaffold(
        modifier = Modifier
            .background(
                color = Color.White
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Silver
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(25.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    user.value?.let {
                        ProfileColumn(
                            label = "Ad",
                            text = it.name
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        ProfileColumn(
                            label = "Soyad",
                            text = it.surname
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        ProfileColumn(
                            label = "Email",
                            text = it.email
                        )
                        Spacer(modifier = Modifier.height(25.dp))
        
                        ProfileColumn(
                            label = "Adres",
                            text = it.address
                        )
        
                        Spacer(modifier = Modifier.height(25.dp))
        
                        CustomButton(
                            modifier = Modifier.padding(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            text = "Çıkış Yap",
                            fontSize = 16,
                            textColor = Color.White,
                            containerColor = Red,
                            iconId =  null
                        ) {
                            firebaseRepository.auth.signOut()
                            context.startActivity(Intent(context,LoginActivity::class.java))
                            context.finish()
                        }
                    }
                }
                
            }
           
            
        }
    }

}

@Composable
fun ProfileColumn(label : String,text : String){
    
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Text(
            text = "$label :",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                color = Red
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp,
                color = Color.White
            )
        )
    }
}