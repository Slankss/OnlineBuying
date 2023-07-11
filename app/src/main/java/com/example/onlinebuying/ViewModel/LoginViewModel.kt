package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LoginViewModel(

    var firebaseRepository: FirebaseRepository) : ViewModel()
{
    var _processOf = MutableStateFlow<AuthProcessOf<Int>?>(null)
    var processOf  = _processOf.asStateFlow()

    init
    {

    }

    fun login(
        email : String,password : String,
        resultListener : (AuthProcessOf<Int>) -> Unit
    ){

        firebaseRepository.login(
            email = email,
            password = password,
            resultListener = { process ->
                resultListener(process)
            }
        )

    }



}