package com.example.onlinebuying.View.CustomerPage

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
        
        Column(
            modifier = Modifier
                .background(
                    color = Color.White
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            var painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = product.value?.image_url)
                    .apply {
                        crossfade(true)
                        placeholder(R.drawable.place_holder)
                    }.build()
            )
            
            var image_scale by remember { mutableStateOf(1f) }
            var image_vertical_padding by remember { mutableStateOf(20.0) }
            
            Card(
                modifier = Modifier
                    .padding(vertical = image_vertical_padding.dp)
                    .scale(image_scale)
                    .combinedClickable(
                        onClick = {
            
                        },
                        onDoubleClick = {
                            when(image_scale)
                            {
                                1f ->
                                {
                                    image_scale *= 1.25f
                                    image_vertical_padding *= 2
                                }
                
                                else ->
                                {
                                    image_scale /= 1.25f
                                    image_vertical_padding /= 2
                                }
                            }
                        }
                    ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(width = 1.dp,color = Color.Black)
            ) {
                Image(
                    modifier = Modifier
                        .size(width = 225.dp, height = 250.dp),
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = "product.name",
                    
                    )
            }
            
            
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = Orange,
                ),
                border = BorderStroke(color = Color.Black, width = 1.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 7.dp
                )
            )
            {
                
                ProductInfoColumn(
                    title = "Ürün adı",
                    text = if(product.value != null) product.value!!.name else ""
                )
                
                ProductInfoColumn(
                    title = "Ürün açıklaması",
                    text =  if(product.value != null) product.value!!.descripton else ""
                )
                
                ProductInfoColumn(
                    title = "Ürün Fiyatı",
                    text = if(product.value != null) product.value!!.price.toString() else ""
                )
                
                
                CustomButton(
                    modifier = Modifier
                        .padding(50.dp),
                    shape = RoundedCornerShape(7.dp),
                    text = "Sipariş ver",
                    fontSize = 16,
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
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 24.sp
            )
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 22.sp,
                color = Color.White
            ),
            textAlign = TextAlign.Justify
        )
    }
}