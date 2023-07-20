package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.Ordered
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductListPageViewModel(
        var firebaseRepository: FirebaseRepository
) : ViewModel()
{
    
    private var _productProcess = MutableStateFlow<ProductProcess>(ProductProcess.Loading)
    var productProcess = _productProcess.asStateFlow()
    
    fun getProductListFromFirebase(){
        setProductProcess(ProductProcess.Loading)
        
        firebaseRepository
            .getProductList(null,Ordered.Date,Ordered.Descending)
        { productList ->
            if(productList != null){
                setProductProcess(ProductProcess.Success(productList))
            }
            else{
                setProductProcess(ProductProcess.Failed("Ürün bulunamadı!"))
            }
        }
        
    }
    
    fun setProductProcess(process: ProductProcess){
        _productProcess.value = process
    }
    
}