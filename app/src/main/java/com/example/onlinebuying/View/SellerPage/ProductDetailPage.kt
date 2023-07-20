package com.example.onlinebuying.View.SellerPage

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Teal
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onlinebuying.Model.SellerPages
import com.example.onlinebuying.ViewModel.AddProcess
import com.example.onlinebuying.ViewModel.ProductDetailPageViewModel
import com.example.onlinebuying.ViewModelFactory.ProductDetailPageViewModelFactory
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.Widgets.FailedDialog
import com.example.onlinebuying.Widgets.LoadingDialog
import com.example.onlinebuying.Widgets.SuccessDialog
import com.example.onlinebuying.ui.theme.Navy
import com.example.onlinebuying.ui.theme.Red
import com.example.onlinebuying.ui.theme.Silver

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailPage(
        navController : NavController,
        firebaseRepository: FirebaseRepository,
        product_id : Int?
){
    
    val scope = rememberCoroutineScope()
    
    var productDetailViewModel : ProductDetailPageViewModel = viewModel(
        factory = ProductDetailPageViewModelFactory(firebaseRepository)
    )
    
    
    var isEdit by remember { mutableStateOf(false) }
    
    var product = productDetailViewModel.product.collectAsState()
    var p_name by remember { mutableStateOf("") }
    var p_description by remember { mutableStateOf("") }
    var p_stock by remember { mutableStateOf("") }
    var p_price by remember { mutableStateOf("") }
    var p_image_url by remember { mutableStateOf("") }
    
    LaunchedEffect(
        key1 = true
    ){
        if(product_id != null){
            productDetailViewModel.getProduct(product_id)
        }
    }
    
    var process = productDetailViewModel.process.collectAsState()
    
    when(process.value){
        is AddProcess.Loading -> LoadingDialog()
        is AddProcess.Success -> SuccessDialog(text = "Ürün başarıyla güncellendi")
        {
            isEdit = false
            productDetailViewModel.setProcess(AddProcess.NotStarted)
        }
        is AddProcess.Error -> FailedDialog(text = "Ürün düzenlenirken bir sorun oluştu"){
            productDetailViewModel.setProcess(AddProcess.NotStarted)
        }
        else -> {
        
        }
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
                    containerColor = if(!isEdit) Orange else Red,
                ),
                border = BorderStroke(color = Color.Black, width = 1.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 7.dp
                )
            )
            {
                if(isEdit){
    
                    var maxPrice = 10000000 // 10 milyon
                    var maxStock = 1000
                    
                    ProductEditColumn(
                        title = "Ürün adı",
                        text = p_name,
                        keyboardType = KeyboardType.Text
                    ){
                        if(it.length <= 100)
                            p_name = it
                    }
    
                    ProductEditColumn(
                        title = "Ürün açıklaması",
                        text = p_description,
                        keyboardType = KeyboardType.Text
                    ){
                        if(it.length <= 250)
                            p_description = it
                    }
    
                    ProductEditColumn(
                        title = "Ürün Stok Miktarı",
                        text = p_stock,
                        keyboardType = KeyboardType.Decimal
                    ){
                        if(it.isBlank()){
                            p_stock = it
                        }
                        else {
                            val stock_double = it.trim().toDouble()
                            if(stock_double <= maxStock)
                                p_stock = it
                        }
                    }
    
                    ProductEditColumn(
                        title = "Ürün Fiyatı",
                        text = p_price,
                        keyboardType = KeyboardType.Decimal
                    ){
                        if(it.isBlank()){
                            p_price = it
                        }
                        else {
                            val price_double = it.trim().toDouble()
                            if(price_double <= maxPrice)
                                p_price = it
                        }
                    }
                }
                else{
                    ProductInfoColumn(
                        title = "Ürün adı",
                        text = if(product.value != null) product.value!!.name else ""
                    )
    
                    ProductInfoColumn(
                        title = "Ürün açıklaması",
                        text =  if(product.value != null) product.value!!.descripton else ""
                    )
    
                    ProductInfoColumn(
                        title = "Ürün Stok Miktarı",
                        text = if(product.value != null) product.value!!.stock.toString() else ""
                    )
    
                    ProductInfoColumn(
                        title = "Ürün Fiyatı",
                        text = if(product.value != null) product.value!!.price.toString() else ""
                    )
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                    ,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
    
                    CustomButton(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(7.dp),
                        text = if(isEdit) "Kaydet" else "Düzenle",
                        fontSize = 16,
                        textColor = Color.White,
                        containerColor = Navy,
                        iconId = null
                    )
                    {
                        if(isEdit){
                            // düzenleme işlemleri
                            product.value?.let {
                                productDetailViewModel.setProcess(AddProcess.Loading)
                                productDetailViewModel.updateProduct(
                                    it.id!!,p_name,p_description,p_stock.toDouble(),
                                    p_price.toDouble()
                                ){ success ->
                                    if(success){
                                        productDetailViewModel.setProcess(AddProcess.Success)
                                    }
                                    else{
                                        productDetailViewModel.setProcess(AddProcess.Error)
                                    }
                                }
                
                            }
                        }
                        else{
                            // düzenleme moduna geç
                            product.value?.let {
                                p_name = it.name
                                p_description = it.descripton
                                p_stock = it.stock.toString()
                                p_price = it.price.toString()
                                p_image_url = it.image_url!!
                                isEdit = !isEdit
                            }
                        }
                    }
    
                    CustomButton(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(7.dp),
                        text = if(isEdit) "İptal" else "Sil",
                        fontSize = 16,
                        textColor = Color.Black,
                        containerColor = Silver,
                        iconId = null
                    )
                    {
                        if(isEdit){
                            isEdit = !isEdit
                        }
                        else{
                            productDetailViewModel.deleteProduct(product.value!!.id!!){
                                navController.navigate(SellerPages.MyProductsPage.route) {
                                    popUpTo(SellerPages.ProductDetailPage.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditColumn(
        title: String,
        text : String,
        keyboardType : KeyboardType,
        onValueChange : (String) ->Unit
){
    
    var focusManager = LocalFocusManager.current
    
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
        TextField(
            modifier = Modifier
                .padding(vertical = 10.dp),
            value = text,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 22.sp,
                color = Color.White
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Teal,
            ),
            onValueChange = {
                onValueChange(it)
            }
        )
    }
}
