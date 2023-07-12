package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AddProcess{
    object Success : AddProcess()
    object Error : AddProcess()
    object Loading : AddProcess()
    object NotStarted : AddProcess()
}

class AddProductPageViewModel(
    var firebaseRepository: FirebaseRepository
) : ViewModel() {

    var _addProcess = MutableStateFlow<AddProcess?>(AddProcess.NotStarted)
    var addProcess = _addProcess.asStateFlow()

    fun addProduct(product : Product,){

        _addProcess.value = AddProcess.Loading
        firebaseRepository.addProduct(product){ success ->
            _addProcess.value = when(success){
                true -> AddProcess.Success
                false -> AddProcess.Error
            }
        }

    }

}