package com.example.onlinebuying.ViewModel

import android.content.Context
import android.graphics.pdf.PdfDocument.Page
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.dataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebuying.Model.Pages
import com.example.onlinebuying.Model.StoreData
import com.example.onlinebuying.Repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuthSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
            storedData.getData.collect{
                first_time = it
                nextPage()
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
                true -> _page.value = Pages.TrailerPage.name
                else -> _page.value = Pages.LoginPage.name
            }
        }
    }

}