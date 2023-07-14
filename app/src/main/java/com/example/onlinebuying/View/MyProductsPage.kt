package com.example.onlinebuying.View

import android.annotation.SuppressLint
import android.provider.SyncStateContract.Columns
import android.widget.Space
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.onlinebuying.Repository.FirebaseRepository
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.onlinebuying.Model.Order
import com.example.onlinebuying.Model.Ordered
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Model.SellerPages
import com.example.onlinebuying.ViewModel.MyProductsPageViewModel
import com.example.onlinebuying.ViewModel.ProductProcess
import com.example.onlinebuying.ViewModelFactory.MyProductsPageViewModelFactory
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Teal
import com.example.onlinebuying.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProductsPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){

    val scope = rememberCoroutineScope()
    var snackbarHostState = remember { SnackbarHostState() }

    var myProductsViewModel : MyProductsPageViewModel = viewModel(factory = MyProductsPageViewModelFactory(firebaseRepository))

    var orderByField by remember { mutableStateOf<Ordered>(Ordered.Date) }
    var orderByDirection by remember { mutableStateOf<Ordered>(Ordered.Descending) }

    var productState = myProductsViewModel.productProcess.collectAsState()

    LaunchedEffect(key1 = true){
        myProductsViewModel.getProductListFromFirebase(orderByField,orderByDirection)
    }

    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                ),
        ) {
            var radioButtonsOrder = listOf(
                "Tarih",
                "Ad",
            )
            var radioButtonsDES = listOf(
                "Azalan",
                "Artan",
            )
            var selectedOrderField by remember { mutableStateOf(0) }
            var selectedOrderDirection by remember { mutableStateOf(0) }


            Column(
                modifier = Modifier
                    .fillMaxSize()
            )
            {

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier
                            .padding(start = 20.dp),
                        text = "Sırala :",
                        color =Color.Black,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium

                    )

                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp)
                    ) {
                        radioButtonsOrder.forEachIndexed{ index, s ->
                            Row(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(45.dp)),
                                verticalAlignment = Alignment.CenterVertically,

                                ) {
                                Text(
                                    text = s,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                RadioButton(
                                    selected = selectedOrderField == index,
                                    onClick = {
                                        orderByField = when(index){
                                            0 -> Ordered.Date
                                            else -> Ordered.Name
                                        }
                                        myProductsViewModel.getProductListFromFirebase(
                                            orderByField,orderByDirection
                                        )
                                        selectedOrderField = index })
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    color = Color.Black
                )

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier
                            .padding(start = 20.dp),
                        text = "Azalan/Artan :",
                        color =Color.Black,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium

                    )

                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp)
                    ) {
                        radioButtonsDES.forEachIndexed{ index, s ->
                            Row(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(45.dp)),
                                verticalAlignment = Alignment.CenterVertically,

                                ) {
                                Text(
                                    text = s,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                RadioButton(
                                    selected = selectedOrderDirection == index,
                                    onClick = {
                                        orderByDirection = when(index){
                                            0 -> Ordered.Descending
                                            else -> Ordered.Ascending
                                        }
                                        myProductsViewModel.getProductListFromFirebase(
                                            orderByField,orderByDirection
                                        )
                                        selectedOrderDirection = index })
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        ,
                    color = Color.Black,
                )

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
fun ProductList(productList : ArrayList<Product>,navController: NavController){

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
fun ProductItem(product: Product,navController: NavController){



    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                navController.navigate("${SellerPages.ProductDetailPage.route}/${product.id}")
            }
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = if(product.id!! % 2 == 0) Orange else Teal
                )
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
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
            )
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