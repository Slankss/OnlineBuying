package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.Order
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddOrderPageViewModel(
        var firebaseRepository: FirebaseRepository
) : ViewModel()
{
    
    var product = MutableStateFlow<Product?>(null)
    
    private var _process = MutableStateFlow<AddProcess>(AddProcess.NotStarted)
    var process = _process.asStateFlow()
    
    fun getProduct(id : Int){
        firebaseRepository.getProduct(id){
            product.value = it
        }
    }
    
    fun addOrder(order : Order){
        
        setProcess(AddProcess.Loading)
        firebaseRepository.addOrder(order){ state ->
            setProcess(state)
        }
    
    }
    
    fun setProcess(process : AddProcess){
        _process.value = process
    }
    
    
    
    
}