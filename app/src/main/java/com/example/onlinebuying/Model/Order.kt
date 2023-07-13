package com.example.onlinebuying.Model

import java.util.Date

data class Order(

    var id : Int,
    var product_id : Int,
    var seller_email : String,
    var buyer_email : String,
    var date : Date?,
    var state : Boolean

)
