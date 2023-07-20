package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.Order
import com.example.onlinebuying.Model.Ordered
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class OrderProcess(){
    data class Success(var orderList : ArrayList<Order>) : OrderProcess()
    data class Failed(var exception : String) : OrderProcess()
    object Loading : OrderProcess()
}

class MyOrdersPageViewModel(
        var firebaseRepository: FirebaseRepository
) : ViewModel()
{

    private var _order = MutableStateFlow<OrderProcess>(OrderProcess.Loading)
    var order = _order.asStateFlow()
    
    fun getOrderList(){
        setProcess(OrderProcess.Loading)
        firebaseRepository.getOrderList{ state ->
            setProcess(state)
        }
        
    }
    
    fun setProcess(process: OrderProcess){
        _order.value = process
    }
    
}