package com.example.onlinebuying.Model

import android.graphics.Bitmap
import androidx.room.Dao
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor

@Dao
data class Product(
    var id : Int?,
    var name : String,
    var descripton : String,
    var seller_email : String?,
    var price : Double,
    var stock : Int,
    var image_url : String?
)
{

}
