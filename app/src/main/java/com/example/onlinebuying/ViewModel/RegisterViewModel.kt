package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Repository.FirebaseRepository

class RegisterViewModel(
    var firebaseRepository: FirebaseRepository
) : ViewModel()
{

    fun register(
        email : String,password : String,
        resultListener : (AuthProcessOf<Int>) -> Unit
    ){

        firebaseRepository.register(
            email = email,
            password = password,
        ){
            resultListener(it)
        }

    }


}