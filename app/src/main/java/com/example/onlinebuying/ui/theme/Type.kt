package com.example.onlinebuying.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.onlinebuying.R

var Inter = FontFamily(
    Font(R.font.inter_regular,FontWeight.W400),
    Font(R.font.inter_medium,FontWeight.W500),
    Font(R.font.inter_semibold,FontWeight.W600),
    Font(R.font.inter_bold, FontWeight.W700)
    )

// Set of Material typography styles to start with
val Typography = Typography(

    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)