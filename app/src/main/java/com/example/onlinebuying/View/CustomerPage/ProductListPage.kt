package com.example.onlinebuying.View.CustomerPage

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.onlinebuying.Model.CustomerPages
import com.example.onlinebuying.Model.Ordered
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Model.SellerPages
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.MyProductsPageViewModel
import com.example.onlinebuying.ViewModel.ProductListPageViewModel
import com.example.onlinebuying.ViewModel.ProductProcess
import com.example.onlinebuying.ViewModelFactory.MyProductsPageViewModelFactory
import com.example.onlinebuying.ViewModelFactory.ProductDetailPageViewModelFactory
import com.example.onlinebuying.ViewModelFactory.ProductListViewModelFactory
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Teal

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListPage(
        navController: NavController,
        firebaseRepository: FirebaseRepository
){
    
    val scope = rememberCoroutineScope()
    var snackbarHostState = remember { SnackbarHostState() }
    
    var productListViewModel : ProductListPageViewModel = viewModel(factory = ProductListViewModelFactory(firebaseRepository))
    
    
    var productState = productListViewModel.productProcess.collectAsState()
    
    LaunchedEffect(key1 = true){
        productListViewModel.getProductListFromFirebase()
    }
    
    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                ),
        ) {
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                
                when(productState.value){
                    is ProductProcess.Failed -> FailedScreen()
                    is ProductProcess.Success ->
                    {
                        ProductList(
                            productList = (productState.value as ProductProcess.Success)
                                .productList, navController)
                        
                    }
                    else -> LoadingScreen()
                }
            }
        }
    }
}

@Composable
fun ProductList(productList : ArrayList<Product>, navController: NavController){
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    )
    {
        items(productList){ product ->
            
            ProductItem(product = product, navController)
        }
        
    }
    
}

@Composable
fun ProductItem(product: Product, navController: NavController){
    
    
    
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                navController.navigate("${CustomerPages.AddOrderPage.route}/${product.id}")
            }
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = if(product.id!! % 2 == 0) Orange else Teal,
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        var painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = product.image_url)
                .apply {
                    crossfade(true)
                    placeholder(R.drawable.place_holder)
                }.build()
        )
        
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp),
            contentScale = ContentScale.Crop,
            painter = painter,
            contentDescription = product.name,
            
            )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if(product.id!! % 2 == 0) Orange else Teal
                )
        )
        {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        color = Color.White
                    ),
                )
                
                var priceText = product.price.toString().replace(".",",")
                Text(
                    text = "${priceText} TL",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Thin,
                        color = Color.White
                    ),
                )
            }
            
        }
        
    }
    
}

@Composable
fun LoadingScreen() {
    
    Box(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
    
}


@Composable
fun FailedScreen(){
    
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "Ürün bulunamadı",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Thin
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_warning),
                contentDescription = "no data")
        }
    }
    
}