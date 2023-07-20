package com.example.onlinebuying.Repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.currentComposer
import androidx.room.util.query
import com.example.onlinebuying.Model.AuthProcessOf
import com.example.onlinebuying.Model.Order
import com.example.onlinebuying.Model.Ordered
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Model.User
import com.example.onlinebuying.ViewModel.AddProcess
import com.example.onlinebuying.ViewModel.OrderProcess
import com.example.onlinebuying.ViewModel.ProductProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.lang.reflect.Field
import java.util.Date
import java.util.concurrent.DelayQueue

class FirebaseRepository() {

    lateinit var auth : FirebaseAuth
    var user : FirebaseUser? = null
    lateinit var db : FirebaseFirestore
    var storage = Firebase.storage
    var storageRef = storage.reference
    
    var curentProductId : String? = null

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
        var loginTask = auth.signInWithEmailAndPassword(email,password)
    
        loginTask.continueWith { task ->
            if(task.isSuccessful){
                user = auth.currentUser
                getUserProfile(email){
                    resultListener(AuthProcessOf.Success(it))
                }
            }
        }.addOnFailureListener { exception ->
            resultListener(AuthProcessOf.Error(exception.localizedMessage))
        }

    }

    fun getUserProfile(email : String, getUser : (User?) -> Unit)  {

        if(user != null) {
            db.collection("User").whereEqualTo("email",email).limit(1).get().addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    task.result?.documents.let{
                        if(!it.isNullOrEmpty()){
                            val name = it[0].getString("name") as String
                            val surname = it[0].getString("surname") as String
                            val is_seller = it[0].getBoolean("seller_account") as Boolean
                            var address = it[0].getString("address") as String
                            val profile = User(email,name,surname,is_seller,"",address)
                    
                            getUser(profile)
                        }
                        else{
                            getUser(null)
                        }
                
                    }
                }
            }
        }
        else{
            Log.e("icardi","user null")
        }
        
    }

    fun createUserProfile(name : String,surname : String,is_seller : Boolean,phone : String,
                          address : String,resultListener: (AuthProcessOf<Int>) -> Unit)
    {
        user?.let{
            var userProfile = User(it.email.toString(), name,surname,is_seller,phone,address)

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
                        val description = getString("descripton") as String
                        val seller_email = getString("seller_email") as String
                        val price = getDouble("price") as Double
                        val stock = getDouble("stock")?.toInt() as Int
                        val image_url = getString("image_url") as String

                        var product = Product(id, name,description,seller_email,price,stock,image_url)
                        curentProductId = this.id
                        getProduct(product)
                    }
                }
                else{
                    curentProductId = null
                    getProduct(null)
                }
            }
        }

    }
    
    fun updateProduct(name : String,description : String,
            stock : Double,price : Double,onSucces: (Boolean) -> Unit
    )
    {
        if(curentProductId != null){
            db.collection("Product").document(curentProductId!!)
                .update("name",name,"descripton",description,
                "stock",stock,"price",price).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        onSucces(true)
                    }
                    else if(task.exception != null){
                        onSucces(false)
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

    fun getProductList(email : String?,orderByName : Ordered,
                       orderByDirection : Ordered,
                       getProductList : (ArrayList<Product>?) -> Unit){
        
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
                        
                        if(email == null){
                            productList.add(product)
                        }
                        else if(email == seller_email){
                            productList.add(product)
                        }
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
    
    fun deleteProduct(id : Int,resultListener : () -> Unit){
        
        db.collection("Product").whereEqualTo("id",id)
            .get().addOnCompleteListener { getTask ->
                if(getTask.isSuccessful){
                    var document = getTask.result.documents
                    var documentId = document[0].id
                    db.collection("Product").document(documentId)
                        .delete().addOnCompleteListener { task->
                            if(task.isSuccessful){
                                resultListener()
                            }
                        }
                }
            }
        
    }
    
    fun addOrder(order : Order,resultListener: (AddProcess) -> Unit){
        
        getLastOrderId { orderId ->
            user?.let { curentUser ->
                getUserProfile(curentUser.email!!){ profile ->
                    if(profile != null){
                        order.id = orderId
                        order.buyer_email = profile.email
                        order.buyer_name = profile.name
                        order.buyer_surname = profile.surname
                        order.buyer_address = profile.address
    
                        db.collection("Order").add(order)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    resultListener(AddProcess.Success)
                                }
                            }.addOnFailureListener { exception ->
                                resultListener(AddProcess.Error)
                            }
                    }
                }
            }
        }
    }
    
    fun getLastOrderId(getLastId: (Int) -> Unit){
        
        db.collection("Order").limit(1).orderBy("id",Query.Direction.DESCENDING)
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val document = task.result.documents
                    if(document.isNotEmpty()){
                        
                        var id = document[0].getDouble("id")!!.toInt()
                        getLastId(id+1)
                    }
                    else{
                        getLastId(1)
                    }
                }
            }
        
    }
    
    fun getOrderList(resultListener: (OrderProcess) -> Unit)
    {
        user?.let {
            getUserProfile(it.email!!){ profile ->
                if(profile != null){
                    var email = profile.email
                    var isSeller = profile.seller_account
                    
                    db.collection("Order")
                        .orderBy("id",Query.Direction.DESCENDING)
                        .get().addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            var documents = task.result.documents
                            if(documents.isNotEmpty()){
                
                                var orderList = arrayListOf<Order>()
                                for(doc in documents){
                                    var id = doc.getDouble("id")?.toInt() as Int
                                    var product_id = doc.getDouble("product_id")?.toInt() as Int
                                    var seller_email = doc.getString("seller_email") as String
                                    var buyer_email = doc.getString("buyer_email") as String
                                    var date = doc.getDate("date") as Date
                                    var product_name = doc.getString("product_name") as String
                                    var state = doc.getBoolean("state") as Boolean
                                    var buyer_name = doc.getString("buyer_name") as String
                                    var buyer_surname = doc.get("buyer_surname") as String
                                    var buyer_address = doc.getString("buyer_address") as String
    
                                    var order = Order(id,product_id,product_name,seller_email,buyer_email,date,state,buyer_name,buyer_surname,buyer_address)
                                    if(isSeller){
                                        if(seller_email == email){
                                            orderList.add(order)
                                        }
                                    }
                                    else{
                                        if(buyer_email == email){
                                            orderList.add(order)
                                        }
                                    }
                                }
                                resultListener(OrderProcess.Success(orderList))
                            }
                            else{
                                resultListener(OrderProcess.Failed("Sipariş bulunamadı"))
                            }
                        }
                    }.addOnFailureListener { exception ->
                        resultListener(OrderProcess.Failed("Sipariş bulunamadı"))
                    }
                }
            }
        }
    }
    
    fun updateOrder(id : Int,state : Boolean,resultListener: () -> Unit){
        
        db.collection("Order").whereEqualTo("id",id)
            .limit(1)
            .get().addOnCompleteListener { getTask ->
                if(getTask.isSuccessful){
                    var document = getTask.result.documents
                    var documentId = document[0].id
                
                    if(state){
                        db.collection("Order").document(documentId)
                            .update("state",true).addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    resultListener()
                                }
                            }
                        }
                    else{
                        db.collection("Order").document(documentId).delete().addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                resultListener()
                            }
                        }
                    }
                }
            }
    }
    
    
}