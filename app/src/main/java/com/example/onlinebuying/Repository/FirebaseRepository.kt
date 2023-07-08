package com.example.onlinebuying.Repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class FirebaseRepository() {

    lateinit var auth : FirebaseAuth
    var user : FirebaseUser? = null

    init {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let{
            user = it
        }
    }

    fun register(email : String,password: String,
            successListener : () -> Unit,
            failureListener : (String) -> Unit
                 ){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                successListener()
            }
        }.addOnFailureListener{ exception ->
            failureListener(exception.localizedMessage)
        }

    }

    suspend fun login(email : String,password: String,
            resultListener: () -> Unit,
              )
    {
        var result = coroutineScope{
            launch {
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        resultListener()
                    }
                }.addOnFailureListener{ exception ->
                    // failureListener(exception.localizedMessage)

                }
            }
        }

    }

}