package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LoginViewModel(

    var firebaseRepository: FirebaseRepository) : ViewModel()
{
    var _processOf = MutableStateFlow<AuthProcessOf<Int>?>(AuthProcessOf.NotStarted)
    var processOf  = _processOf.asStateFlow()

    init
    {

    }

    fun login(
        email : String,password : String
    ){
        setProcess(AuthProcessOf.Loading)
        firebaseRepository.login(
            email = email,
            password = password,
            resultListener = { process ->
                setProcess(process)
            }
        )

    }
    
    fun setProcess(process : AuthProcessOf<Int>){
        _processOf.value = process
    }



}