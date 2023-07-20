package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(
    var firebaseRepository: FirebaseRepository
) : ViewModel()
{
    
    var _processOf = MutableStateFlow<AuthProcessOf<Int>?>(AuthProcessOf.NotStarted)
    var processOf  = _processOf.asStateFlow()
    fun register(
        email : String,password : String,
        resultListener : (AuthProcessOf<Int>) -> Unit
    ){
        setProcess(AuthProcessOf.Loading)
        firebaseRepository.register(
            email = email,
            password = password,
        ){ process ->
            setProcess(process)
        }

    }
    
    fun setProcess(process : AuthProcessOf<Int>){
        _processOf.value = process
    }


}