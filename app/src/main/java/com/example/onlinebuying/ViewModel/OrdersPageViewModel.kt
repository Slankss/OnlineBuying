package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrdersPageViewModel(
        var firebaseRepository: FirebaseRepository
) : ViewModel()
{
    
    private var _order = MutableStateFlow<OrderProcess>(OrderProcess.Loading)
    var order = _order.asStateFlow()
    
    fun getOrderList(){
        firebaseRepository.getOrderList{ state ->
            setProcess(state)
        }
        
    }
    
    fun setProcess(process: OrderProcess){
        _order.value = process
    }
    
    fun updateOrder(id : Int,state: Boolean){
        firebaseRepository.updateOrder(id,state){
            getOrderList()
        }
    }
    
}