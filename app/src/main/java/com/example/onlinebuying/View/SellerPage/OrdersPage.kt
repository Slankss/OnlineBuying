package com.example.onlinebuying.View.SellerPage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onlinebuying.Model.Order
import com.example.onlinebuying.Model.toDateString
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.MyOrdersPageViewModel
import com.example.onlinebuying.ViewModel.OrderProcess
import com.example.onlinebuying.ViewModel.OrdersPageViewModel
import com.example.onlinebuying.ViewModelFactory.MyOrdersViewModelFactory
import com.example.onlinebuying.ViewModelFactory.OrdersViewModelFactory
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.ui.theme.Navy
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Red
import com.example.onlinebuying.ui.theme.Silver
import com.example.onlinebuying.ui.theme.Teal

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrdersPage(
        navController: NavController,
        firebaseRepository: FirebaseRepository
){
    
    var orderViewModel : OrdersPageViewModel = viewModel(factory = OrdersViewModelFactory(firebaseRepository))
    
    var order = orderViewModel.order.collectAsState()
    
    LaunchedEffect(
        key1 = true
    ){
        orderViewModel.setProcess(OrderProcess.Loading)
        orderViewModel.getOrderList()
    }
    
    Scaffold()
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                )
        ) {
            
            when(order.value){
                is OrderProcess.Loading -> com.example.onlinebuying.View.CustomerPage.LoadingScreen()
                is OrderProcess.Success -> {
                    var orderList = (order.value as OrderProcess.Success).orderList
                    OrderList(orderList,orderViewModel)
                    
                }
                is OrderProcess.Failed -> {
                    var errorMessage = (order.value as OrderProcess.Failed).exception
                    com.example.onlinebuying.View.CustomerPage.FailedScreen()
                }
            }
            
        }
    }
    
}

@Composable
fun OrderList(orderList : ArrayList<Order>,orderViewModel: OrdersPageViewModel){
    
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        
        Column(
            modifier = Modifier
                .padding(top = 10.dp)
                .weight(49f)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = Red)
            ){
                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                    ,
                    text = "Onay Bekleyen Siparişler",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.White
                    ),
                )
            }
            
            
            LazyColumn(){
                
                items(orderList){ order ->
                    OrderColumn(order = order, state = false,orderViewModel)
                }
                
            }
        }
        
        Divider(
            modifier = Modifier
                .weight(2f),
            color = Navy
        )
        Column(
            modifier = Modifier
                .padding(top = 10.dp)
                .weight(49f)
        ){
            
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = Orange)
            ){
                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                    ,
                    text = "Onaylanan Siparişler",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.White
                    ),
                )
            }
            
            LazyColumn(
                modifier = Modifier
                    .padding(top =  20.dp)
            ){
                
                items(orderList){ order ->
                    OrderColumn(order = order,state = true,orderViewModel)
                }
                
            }
        }
        
    }
    
    
    
}

@Composable
fun OrderColumn(order : Order, state : Boolean,orderViewModel : OrdersPageViewModel){
    
    var expandableCardVisibility by remember { mutableStateOf(false) }
    
    var cardColor = if(order.id!! % 2 == 0) Red else Silver
    var innerColor = if(order.id!! % 2 == 0) Silver else Red
    
    if(order.state == state){
        Column(
            modifier = Modifier
                .padding(10.dp),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                
                Text(
                    text = order.product_name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )
                Text(
                    text = order.date!!.toDateString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Thin,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle().copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        ){
                            append("Siparişi veren :")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle().copy(
                                fontWeight = FontWeight.Thin,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        ){
                            append("${order.buyer_name} ${order.buyer_surname}")
                        }
                    }
                )
            }
            
            if(!order.state){
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    OutlinedButton(
                        onClick = {
                            orderViewModel.updateOrder(order.id!!,true)
                        }
        
                    ) {
                        Text(
                            text =  "Onayla",
                            color = Teal
                        )
                    }
        
                    Spacer(modifier = Modifier.width(15.dp))
        
                    OutlinedButton(
                        onClick = {
                            orderViewModel.updateOrder(order.id!!,false)
                        }
        
                    ) {
                        Text(
                            text =  "İptal et",
                            color = Red
                        )
                    }
                }
            }
            
            
        }
        Divider()
    }
    
}