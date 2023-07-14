package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductDetailPageViewModel(
     var firebaseRepository: FirebaseRepository
) : ViewModel()
{
    var _process = MutableStateFlow<AddProcess>(AddProcess.NotStarted)
    var process = _process.asStateFlow()
    
    var _product = MutableStateFlow<Product?>(null)
    var product = _product.asStateFlow()
    
    
    fun getProduct(id : Int){
        firebaseRepository.getProduct(id){
            _product.value = it
        }
    }
    
    fun updateProduct(id : Int,name : String,description : String,
        stock : Double,price : Double,
        onSuccess : (Boolean) -> Unit)
    {
        firebaseRepository.updateProduct(name, description,stock, price)
        { success ->
            if(success){
                getProduct(id)
            }
            onSuccess(success)
        }
    }
    
    fun setProcess(state : AddProcess){
        _process.value = state
    }

}