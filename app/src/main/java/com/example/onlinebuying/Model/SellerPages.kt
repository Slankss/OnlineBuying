package com.example.onlinebuying.Model

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.example.onlinebuying.R

sealed class SellerPages(val route : String,val label : String,val iconId : Int){

    object MyProductsPage : SellerPages("MyProductsPage","Ürünlerim", R.drawable.ic_products)

    object AddProductPage : SellerPages("AddProductPage","Ürün Ekle",R.drawable.ic_add)

    object OrdersPage : SellerPages("OrdersPage","Siparişler",R.drawable.ic_order)

    object ProfilePage : SellerPages("ProfilePage","Profil",R.drawable.ic_profile)

}