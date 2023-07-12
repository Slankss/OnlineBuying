package com.example.onlinebuying.Model

import android.graphics.Bitmap
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor

data class Product(
    var id : Int?,
    var name : String,
    var descripton : String,
    var seller_email : String?,
    var price : Double,
    var stock : Int,
)
{
    var image_bitmap : Bitmap? = null
    var image_url : String? = null
    constructor(id : Int?,name : String,description : String,seller_email : String?,
        price : Double,stock : Int,image_url : String?)
            : this(id,name,description,seller_email,price,stock){
                this.image_url = image_url
            }

    constructor(id : Int?, name : String, description : String, seller_email : String?,
                price : Double, stock : Int,image_bitmap : Bitmap?)
            : this(id,name,description,seller_email,price,stock){
                this.image_bitmap = image_bitmap
            }


}
