package com.example.onlinebuying.ViewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.FirstPageViewModel

class FirstPageViewModelFactory(var firebaseRepository: FirebaseRepository,var context : Context)
    : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T = FirstPageViewModel(firebaseRepository,context) as T

}