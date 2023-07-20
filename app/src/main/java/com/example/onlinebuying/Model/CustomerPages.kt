package com.example.onlinebuying.Model

import com.example.onlinebuying.R

sealed class CustomerPages(val route : String,val label : String,val iconId : Int?){
    
    object ProductListPage : CustomerPages("ProductListPage","Ürünler", R.drawable.ic_products)
    
    object MyOrdersPage : CustomerPages("MyOrdersPage","Siparişlerim",R.drawable.ic_order)
    
    object ProfilePage : CustomerPages("ProfilePage","Profil",R.drawable.ic_profile)
    
    object AddOrderPage : CustomerPages("AddOrderPage","Sipariş ver",null)
    
}
