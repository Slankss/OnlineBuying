package com.example.onlinebuying.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebuying.Model.ProcessOf
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.function.BinaryOperator


class LoginViewModel(

    var firebaseRepository: FirebaseRepository) : ViewModel()
{
    var _processOf = MutableStateFlow<ProcessOf<Int>?>(null)
    var processOf  = _processOf.asStateFlow()

    init
    {

    }

    fun login(
        email : String,password : String,
        resultListener : (ProcessOf<Int>) -> Unit
    ){

        firebaseRepository.login(
            email = "asdada@gmail.com",
            password = "123456",
            resultListener = { process ->
                resultListener(process)
            }
        )

    }

}