package com.example.onlinebuying.Model

sealed class Ordered{
    object Date : Ordered()
    object Name : Ordered()
    object Descending : Ordered()
    object Ascending : Ordered()
}