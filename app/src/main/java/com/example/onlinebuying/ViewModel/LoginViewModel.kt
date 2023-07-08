package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.launch
import java.util.function.BinaryOperator

class LoginViewModel(
    var firebaseRepository: FirebaseRepository) : ViewModel()
{

    fun login(){
        viewModelScope.launch {
            var a = firebaseRepository.login("asdada@gmail.com","123456")


        }
    }

}