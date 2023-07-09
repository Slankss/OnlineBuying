package com.example.onlinebuying.Model

import java.lang.Exception
import java.lang.Process

sealed class ProcessOf<out T : Any> {
    object Success : ProcessOf<Nothing>()
    data class Error(val errorMessage: String) : ProcessOf<Nothing>()
}

