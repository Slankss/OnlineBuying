package com.example.onlinebuying.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebuying.Model.Pages
import com.example.onlinebuying.Model.StoreData
import com.example.onlinebuying.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FirstPageViewModel(
    val firebaseRepository: FirebaseRepository,
    context : Context
) : ViewModel()
{

    var storedData = StoreData(context)
    var _page  = MutableStateFlow<String?>(null)
    var page = _page.asStateFlow()

    var first_time : Boolean? = null
    init {
        viewModelScope.launch {
            val user = firebaseRepository.user
            
            if(user != null){
                firebaseRepository.getUserProfile(
                    email =  user.email!!,
                    getUser = { userProfile ->
                        if(userProfile != null){
                            // Profil oluşturulmuş ana sayfaya gidilecek
                            _page.value = when(userProfile.seller_account){
                                true -> Pages.SellerPage.name
                                false -> Pages.CustomerPage.name
                            }
                        }
                        else{
                            // Profil oluşturulmamış profil oluşturma sayfasına gidilecek
                            _page.value = Pages.CreateProfilePage.name
                        }
                    }
                )
            }
            else{
                storedData.getData.collect{
                    first_time = it
                    nextPage()
                }
            }
        }
    }


    fun  nextPage(){

        var user = firebaseRepository.user

        if(user != null){
            _page.value = Pages.LoginPage.name
        }
        else{
            when(first_time)
            {
                false -> _page.value = Pages.LoginPage.name
                else -> _page.value = Pages.TrailerPage.name
            }
        }
    }
    
    fun setPage(page : String?){
        _page.value = page
    }

}