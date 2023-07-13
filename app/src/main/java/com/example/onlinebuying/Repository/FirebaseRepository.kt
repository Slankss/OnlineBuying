package com.example.onlinebuying.Repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Model.Ordered
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.lang.reflect.Field
import java.util.concurrent.DelayQueue

class FirebaseRepository() {

    lateinit var auth : FirebaseAuth
    var user : FirebaseUser? = null
    lateinit var db : FirebaseFirestore
    var storage = Firebase.storage
    var storageRef = storage.reference

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

    fun addProduct(product : Product,image_bitmap : Bitmap,onSucces : (Boolean) -> Unit){

        getLastProductId{ lastId ->
            saveProductImage(
                product_id = if(lastId == null) 1 else lastId+1,
                image_bitmap){ uri ->
                uri?.let{
                    Log.e("errorumsu",it.toString())
                    product.id = if(lastId == null) 1 else lastId+1
                    product.seller_email = user?.email
                    product.image_url = it.toString()
                    db.collection("Product").add(product)
                        .addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                onSucces(true)
                            }
                        }

                }
            }
        }

    }

    fun saveProductImage(product_id : Int,bitmap : Bitmap,getUri : (Uri?) -> Unit){
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()

        var image_ref = storageRef.child("product_images").child("$product_id.jpg")

        var uploadTask = image_ref.putBytes(data)

        uploadTask.continueWithTask{ task ->
            if(!task.isSuccessful){
                task.exception?.let{
                    throw it
                }
            }
            image_ref.downloadUrl
        }.addOnCompleteListener{ task ->
            if(task.isSuccessful()){
                val dowloadUrl = task.result
                getUri(dowloadUrl)
            }
            else{

            }
        }


    }
    fun getProduct(id : Int,getProduct : (Product?) -> Unit){

        db.collection("Product").whereEqualTo("id",id).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                var document = task.result.documents
                if(document.isNotEmpty()){
                    document[0].apply{
                        val id = getDouble("id")?.toInt() as Int
                        val name = getString("name") as String
                        val description = getString("description") as String
                        val seller_email = getString("seller_email") as String
                        val price = getDouble("price") as Double
                        val stock = getDouble("stock").toString().toInt() as Int
                        val image_url = getString("image_url") as String

                        var product = Product(id, name,description,seller_email,price,stock,image_url)
                        getProduct(product)
                    }
                }
                else{
                    getProduct(null)
                }
            }
        }

    }

    fun getLastProductId(getLastId : (Int?) -> Unit){

        db.collection("Product").orderBy("id",Query.Direction.DESCENDING).limit(1)
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    var document = task.result.documents
                    if(document.isNotEmpty()){
                        var lastId = document[0].getDouble("id")?.toInt()
                        getLastId(lastId)
                    }
                    else{
                        getLastId(null)
                    }
                }
            }

    }

    fun getProductList(seller_email : String?,orderByName : Ordered,
                       orderByDirection : Ordered,
                       getProductList : (ArrayList<Product>?) -> Unit){


        Log.e("arabam","firebase repo geldi")

        db.collection("Product")
            .orderBy( when(orderByName)
            {
                is Ordered.Name -> "name"
                else -> "id"
            },when(orderByDirection){
                is Ordered.Descending -> Query.Direction.DESCENDING
                else -> Query.Direction.ASCENDING
            })
            .get()
            .addOnCompleteListener{ task ->
            if(task.isSuccessful){
                var document = task.result.documents
                if(document.isNotEmpty()){
                    var productList = arrayListOf<Product>()

                    Log.e("arabam","BOŞ DEĞİL")

                    document.forEachIndexed { index, doc->
                        val id = doc.getDouble("id")?.toInt() as Int
                        val name = doc.getString("name") as String
                        val description = doc.getString("descripton") as String
                        val seller_email = doc.getString("seller_email") as String
                        val price = doc.getDouble("price") as Double
                        val stock = doc.getDouble("stock")?.toInt() as Int
                        val image_url = doc.getString("image_url") as String

                        var product = Product(id, name,description,seller_email,price,stock,image_url)
                        productList.add(product)

                        Log.e("arabam",product.name)
                    }
                    getProductList(productList)
                }
                else{
                    Log.e("arabam","LİSTE BOŞ")

                    getProductList(null)
                }
            }

            else{
                Log.e("arabam","succef olmadı")

            }
        }

    }
}