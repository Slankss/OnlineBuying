package com.example.onlinebuying

import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.selects.selectUnbiased
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.PrimitiveIterator
import java.util.SimpleTimeZone
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
    println(    lengthOfLongestSubstring("c"))
    
    var array1 = intArrayOf(3,4,7,8)
    var array2 = intArrayOf(1,2,5)
    
    println(findMedianSortedArrays(array1,array2))

    
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
    
    var medians = intArrayOf()
    var merged_array = nums1.toMutableList()
    
    merged_array.addAll(nums2.toMutableList())
    if(nums1.isNotEmpty() && nums2.isNotEmpty()){
        if(nums1[nums1.lastIndex] > nums2[0]){
            for(i in merged_array.indices){
                for(j in i+1 until merged_array.size){
                    if(merged_array [i] > merged_array[j]){
                        var tmp = merged_array[i]
                        merged_array[i] = merged_array[j]
                        merged_array[j] = tmp
                    }
                }
            }
        }
    }
    
    
    var index = merged_array.size.toDouble() / 2
    if(merged_array.size % 2 == 0){
        medians += merged_array[ (index-1).toInt() ]
        medians += merged_array[index.toInt()]
    }
    else{
        index -=0.5
        medians += merged_array[index.toInt()]
    }
    
    if(medians.size > 1){
        return ( (medians[0].toDouble() + medians[1].toDouble()) / 2 ).toDouble()
    }
    return medians[0].toDouble()
}


