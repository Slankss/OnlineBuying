package com.example.onlinebuying.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.ViewModel.MyOrdersPageViewModel

class MyOrdersViewModelFactory(
        var firebaseRepository: FirebaseRepository
) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MyOrdersPageViewModel(firebaseRepository) as T
}