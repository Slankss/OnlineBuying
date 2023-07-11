package com.example.onlinebuying.ViewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Model.User
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.launch

class ProfilePageViewModel(
    var firebaseRepository: FirebaseRepository
) : ViewModel() {


    fun createProfile(name : String,surname : String,is_seller : Boolean,phone : String,resultListener : (AuthProcessOf<Int>) -> Unit ){
        viewModelScope.launch {
            firebaseRepository.createUserProfile(
                name = name,
                surname = surname,
                is_seller = is_seller,
                phone = phone,
            ){ process ->
                resultListener(process)
            }
        }
    }

}