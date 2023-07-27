package com.example.onlinebuying

import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.selects.selectUnbiased
import java.lang.Exception
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.PrimitiveIterator
import java.util.SimpleTimeZone
import kotlin.math.pow
import kotlin.math.sqrt


@RequiresApi(Build.VERSION_CODES.O)
fun main(){
    
    var c1 = "abcabcbb"
    var c2 = "bbbbb"
    var c3 = "pwwkew"
    var c4 = "dvdf"
    var c5 = " "
    
    
   // println("test 1 = "+(3 == lengthOfLongestSubstring(c1)))
    //println("test 2 = "+(1 == lengthOfLongestSubstring(c2)))
    //println("test 3 = "+(3 == lengthOfLongestSubstring(c3)))
    //println("test 4 = "+(3 == lengthOfLongestSubstring(c4)))
    //println("test 5 = "+(1 == lengthOfLongestSubstring(c5)))
    
   println(isMatch("aa","a*"))
    
}

fun lengthOfLongestSubstring(s: String): Int {
    
    var substring = ""
    var tmpstring = ""
    
    var index = 0
    var c_index = 0
    
    if(s.isBlank() && s.length >= 1)
        return 1
    else if(s.isEmpty())
        return 0
    
    s.forEachIndexed { index, c ->
    
    }
    
    while(index <= s.length-1 && substring.length > (s.lastIndex - c_index)){
        
        if(tmpstring.contains(s[index])){
            if(substring.length < tmpstring.length)
                substring = tmpstring
            tmpstring = ""
            c_index++
            index = c_index
        }
        else{
            tmpstring += s[index]
            index++
        }
        if(substring.length < tmpstring.length)
            substring = tmpstring
    }
    return substring.length
}

fun findMedianSortedArrays(nums1: IntArray, nums2: IntArray): Double {
    
    var median = 0.0
    var merged_array = intArrayOf()
    
    if(nums1.isEmpty() && nums2.isEmpty())
        return 0.0
    else if(nums1.isEmpty() && nums2.isNotEmpty()){
        for(i in nums2){
            merged_array += i
        }
    }
    else if(nums1.isNotEmpty() && nums2.isEmpty()){
        for(i in nums1){
            merged_array += i
        }
    }
    else{
        var n1_index = 0
        var n2_index = 0
        
        if(nums1[nums1.lastIndex] < nums2[0]){
            merged_array = nums1
            for(i in nums2){
                merged_array +=i
            }
        }
        else if(nums2[nums2.lastIndex] < nums1[0]){
            merged_array = nums2
            for(i in nums1){
                merged_array += i
            }
        }
        else{
            while(n1_index < nums1.size || n2_index < nums2.size){
                var n1 = 0
                var n2 = 0
                if(n1_index < nums1.size && n2_index < nums2.size){
                    n1 = nums1[n1_index]
                    n2 = nums2[n2_index]
                    if(n1 < n2){
                        merged_array += n1
                        n1_index++
                        continue
                    }
                    else if(n2 < n1){
                        merged_array += n2
                        n2_index++
                        continue
                    }
                    else{
                        merged_array += n1
                        merged_array += n2
                        n1_index++
                        n2_index++
                        continue
                    }
                }
                
                else if(n1_index < nums1.size && n2_index >= nums2.size){
                    n1 = nums1[n1_index]
                    merged_array += n1
                    n1_index++
                    continue
                }
                else if(n2_index < nums2.size && n1_index >= nums1.size){
                    n2 = nums2[n2_index]
                    merged_array += n2
                    n2_index++
                    continue
                }
            }
        }
    }
    var index = merged_array.size.toDouble() / 2
    if(merged_array.size % 2 == 0){
        median = ( (merged_array[ (index-1).toInt()] + merged_array[index.toInt()]).toDouble() ) / 2
    }
    else{
        index -=0.5
        median = (merged_array[index.toInt()]).toDouble()
    }
    return median
}

fun reverse(x: Int): Int {
    println(x::class.java.typeName)
    if(x > 2.0.pow(31))
        return 0
    var str = x.toString()
    var newStr = ""
    var is_negative = false
    var index = str.lastIndex
    while(index >= 0){
        if(str[index] == '-')
            is_negative = true
        else
            newStr += str[index]
        
        index--
    }
    return try
    {
        if(is_negative) newStr.toInt() * (-1) else newStr.toInt()
    }catch(e : Exception){
        0
    }
}

fun isPalindrome(x: Int): Boolean {
    
    var number_str = x.toString()
    var new_number_str = ""
    for(i in number_str.count() -1 downTo 0){
        new_number_str += i
    }
    
    return number_str == new_number_str
}
fun isMatch(s: String, p: String): Boolean {
    
    if(p.contains(s)){
        return true
    }
    else
        return p.contains("*")
    
}


