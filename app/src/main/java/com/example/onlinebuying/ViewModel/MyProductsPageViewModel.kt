package com.example.onlinebuying.ViewModel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.Ordered
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class ProductProcess(){
    data class Success(var productList : ArrayList<Product>) : ProductProcess()
    data class Failed(var exception : String) : ProductProcess()
    object Loading : ProductProcess()
}

class MyProductsPageViewModel(
    var firebaseRepository: FirebaseRepository
) : ViewModel()
{
    var _productProcess = MutableStateFlow<ProductProcess>(ProductProcess.Loading)
    var productProcess = _productProcess.asStateFlow()


    init {
    }

    fun getProductListFromFirebase(orderByField : Ordered,orderByDirection : Ordered){

        firebaseRepository.user?.let{
            firebaseRepository.getProductList(it.email,orderByField, orderByDirection)
            { arrayList ->
                if(arrayList != null){
                    _productProcess.value = ProductProcess.Success(arrayList)
                    arrayList.forEach{
                        Log.e("errorumsu",it.id.toString())
                    }
                }
                else{
                    _productProcess.value = ProductProcess.Failed("Ürün Yok")
                }
            }

        }
    }

}