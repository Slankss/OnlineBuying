package com.example.onlinebuying.Model

sealed class AuthProcessOf<out T : Any> {
    data class Success(val user : User?) : AuthProcessOf<Nothing>()
    data class Error(val errorMessage: String) : AuthProcessOf<Nothing>()
    
    object Loading : AuthProcessOf<Nothing>()
    object NotStarted : AuthProcessOf<Nothing>()
}

