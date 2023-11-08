package com.example.onlinebuying.View.CustomerPage

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.onlinebuying.Model.Order
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.View.SellerPage.ProductEditColumn
import com.example.onlinebuying.ViewModel.AddOrderPageViewModel
import com.example.onlinebuying.ViewModel.AddProcess
import com.example.onlinebuying.ViewModel.ProductDetailPageViewModel
import com.example.onlinebuying.ViewModelFactory.AddOrderViewModelFactory
import com.example.onlinebuying.ViewModelFactory.ProductDetailPageViewModelFactory
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.Widgets.FailedDialog
import com.example.onlinebuying.Widgets.LoadingDialog
import com.example.onlinebuying.Widgets.SuccessDialog
import com.example.onlinebuying.ui.theme.Navy
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Red
import com.example.onlinebuying.ui.theme.Silver
import com.example.onlinebuying.ui.theme.SpecialGreen
import java.sql.Timestamp
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun AddOrderPage(
        navController : NavController,
        firebaseRepository: FirebaseRepository,
        product_id : Int?
){
    
    val scope = rememberCoroutineScope()
    
    var addOrderPageViewModel : AddOrderPageViewModel = viewModel(
        factory = AddOrderViewModelFactory(firebaseRepository)
    )
    
    var product = addOrderPageViewModel.product.collectAsState()
    
    LaunchedEffect(
        key1 = true
    ){
        if(product_id != null){
            addOrderPageViewModel.getProduct(product_id)
        }
    }
    
    var process = addOrderPageViewModel.process.collectAsState()
    
    when(process.value){
        is AddProcess.Loading -> LoadingDialog()
        is AddProcess.Success -> SuccessDialog(text = "Sipariş başarıyla verildi")
        {
            addOrderPageViewModel.setProcess(AddProcess.NotStarted)
        }
        is AddProcess.Error -> FailedDialog(text = "Sipariş oluşturulurken bir sorun oluştu"){
            addOrderPageViewModel.setProcess(AddProcess.NotStarted)
        }
        else -> {
        
        }
    }
    
    var alertDialogVisibility by remember { mutableStateOf(false) }
    
    if(alertDialogVisibility){
        AlertDialog(
            onDismissRequest = {
                alertDialogVisibility = false
            },
            title = {
                Text("Sipariş Oluştur")
            },
            text = {
                Text("Sipariş oluşturmak istiyor musunuz?")
            },
            dismissButton = {
                Text(
                    modifier = Modifier
                        .clickable {
                            product.value?.let {
                                var instant = Instant.now()
                                var date = Timestamp.from(instant)
                                var order = Order(null,it.id!!,it.name,it.seller_email!!,null,date,
                                    false,null,null,null)
                                alertDialogVisibility = false
                                addOrderPageViewModel.addOrder(order)
                            }
                        },
                    text =  "Evet"
                )
            },
            confirmButton = {
                Text(
                    modifier = Modifier
                        .clickable {
                            alertDialogVisibility = false
                        },
                    text =  "Hayır"
                )
            }
        )
    }
    
    Scaffold(
    
    ) {
        
        Box(
            modifier = Modifier
                .background(
                    color = Color.White
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

        ) {
            
            var painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = product.value?.image_url)
                    .apply {
                        crossfade(true)
                        placeholder(R.drawable.place_holder)
                    }.build()
            )

            var image_size by remember { mutableStateOf(150.dp) }
            var image_scale by remember { mutableStateOf(1f) }
            var image_vertical_padding by remember { mutableStateOf(20.0) }



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
                    .shadow(
                        elevation = 15.dp,
                        ambientColor = DefaultShadowColor,
                        spotColor = DefaultShadowColor
                    )
                    .align(Alignment.Center)
                    .combinedClickable(
                        onClick = {

                        },
                        onDoubleClick = {
                            when (image_scale)
                            {
                                1f ->
                                {
                                    image_scale *= 1.25f
                                    image_size = 300.dp
                                    image_vertical_padding *= 2
                                }

                                else ->
                                {
                                    image_size = 150.dp
                                    image_scale /= 1.25f
                                    image_vertical_padding /= 2
                                }
                            }
                        }
                    ),

                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(15.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
                {

                    Image(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = SpecialGreen,
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = 10.dp,
                                ambientColor = DefaultShadowColor,
                                spotColor = DefaultShadowColor
                            )
                            .size(image_size),
                        painter = painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = "product.name",

                        )

                    Text(
                        text = if(product.value != null) product.value!!.id.toString() else "",
                        modifier = Modifier
                            .padding(top = 5.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        modifier = Modifier
                            .padding( top = 5.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Text(
                            modifier = Modifier
                                .weight(6f),
                            text = product.value?.name ?: "",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.Black,
                                textAlign = TextAlign.Start
                            )
                        )
                        Text(
                            modifier = Modifier
                                .weight(4f),
                            text =
                            if(product.value != null)
                                "₺"+product.value!!.price.toString()
                                else "",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = SpecialGreen,
                                textAlign = TextAlign.End
                            )
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp),
                        text = "Ürün Açıklaması",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black
                        )
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        text = product.value?.descripton ?: "",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.Black
                        )
                    )


                    CustomButton(
                        modifier = Modifier
                            .padding( top = 20.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(7.dp),
                        text = "Sipariş ver",
                        fontSize = 14,
                        textColor = Color.White,
                        containerColor = Navy,
                        iconId = null
                    ){
                        alertDialogVisibility = true
                    }
                }
            }
            
            

        }
    }
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfoColumn(
        title: String,
        text : String
){
    
    Column(
        modifier = Modifier
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp
            )
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                color = Color.Black
            ),
            textAlign = TextAlign.Justify
        )
    }
}