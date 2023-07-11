package com.example.onlinebuying.Repository

import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository() {

    lateinit var auth : FirebaseAuth
    var user : FirebaseUser? = null
    lateinit var db : FirebaseFirestore

    init {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        setCurrentUser()
    }

    fun setCurrentUser(){
        auth.currentUser?.let{
            user = it
        }
    }
    fun register(email : String,password: String,
            resultListener: (AuthProcessOf<Int>) -> Unit
                 ){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                resultListener(AuthProcessOf.Success(null))
            }
        }.addOnFailureListener{ exception ->
            exception.localizedMessage?.let{
                resultListener(AuthProcessOf.Error(it))
            }

        }

    }

    fun login(email : String, password: String,
              resultListener: (AuthProcessOf<Int>) -> Unit,
        )
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                getUserProfile(email){
                    resultListener(AuthProcessOf.Success(it))
                }
            }
        }.addOnFailureListener { exception ->
            exception.localizedMessage?.let {
                resultListener(AuthProcessOf.Error(it))
            }

        }

    }

    fun getUserProfile(email : String, getUser : (User?) -> Unit)  {

        db.collection("User").whereEqualTo("email",email).limit(1).get().addOnCompleteListener{ task ->
            if(task.isSuccessful) {
                task.result?.documents.let{
                    if(!it.isNullOrEmpty()){
                        val name = it[0].getString("name") as String
                        val surname = it[0].getString("surname") as String
                        val is_seller = it[0].getBoolean("seller_account") as Boolean
                        val user = User(email,name,surname,is_seller,"")

                        getUser(user)
                    }
                    else{
                        getUser(null)
                    }

                }
            }
        }
    }

    fun createUserProfile(name : String,surname : String,is_seller : Boolean,phone : String, resultListener: (AuthProcessOf<Int>) -> Unit)
    {
        user?.let{
            var userProfile = User(it.email.toString(), name,surname,is_seller,phone)

            db.collection("User").add(userProfile).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    resultListener(AuthProcessOf.Success(userProfile))
                }
            }.addOnFailureListener{ exception ->
                exception.localizedMessage?.let{
                    resultListener(AuthProcessOf.Error(it))
                }
            }
        }
    }

}