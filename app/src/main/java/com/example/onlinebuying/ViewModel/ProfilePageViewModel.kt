package com.example.onlinebuying.ViewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Model.User
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfilePageViewModel(
    var firebaseRepository: FirebaseRepository
) : ViewModel() {

    private var _user = MutableStateFlow<User?>(null)
    var user = _user.asStateFlow()
    
    private var _process = MutableStateFlow<AuthProcessOf<Int>>(AuthProcessOf.NotStarted)
    var process = _process.asStateFlow()
    
    fun createProfile(name : String,surname : String,is_seller: Boolean,phone : String,address : String){
        setProcess(AuthProcessOf.Loading)
        viewModelScope.launch {
            firebaseRepository.createUserProfile(
                name = name,
                surname = surname,
                is_seller = is_seller,
                phone = phone,
                address
            ){ state ->
                setProcess(state)
            }
        }
    }
    
    fun getUser(){
        firebaseRepository.user?.let { currentUser ->
            firebaseRepository.getUserProfile(currentUser.email!!){ profile ->
                if(profile != null){
                    setUser(profile)
                }
                
            }
        }
    }
    
    fun setUser(user : User){
        _user.value = user
    }
    
    fun setProcess(process: AuthProcessOf<Int> ){
        _process.value = process
    }

}