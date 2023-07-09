package com.example.onlinebuying.Repository

import android.util.Log
import com.example.onlinebuying.Model.ProcessOf
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
            resultListener: (ProcessOf<Int>) -> Unit
                 ){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                resultListener(ProcessOf.Success)
            }
        }.addOnFailureListener{ exception ->
            exception.localizedMessage?.let{
                resultListener(ProcessOf.Error(it))
            }

        }

    }

    fun login(email : String,password: String,
            resultListener: (ProcessOf<Int>) -> Unit,
        )
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                resultListener(ProcessOf.Success)
            }
        }.addOnFailureListener { exception ->
            exception.localizedMessage?.let {
                resultListener(ProcessOf.Error(it))
            }

        }

    }



}