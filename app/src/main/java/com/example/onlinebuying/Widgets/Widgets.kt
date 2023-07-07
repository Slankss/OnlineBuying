package com.example.onlinebuying.Widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(modifier : Modifier,
                  shape : Shape,
                  text : String,
                  fontSize : Int,
                  textColor : Color,
                  containerColor : Color,
                  iconId : Int?,
                  onClick : () -> Unit){

    // Shapes
    // CutCornerShape( corner = CornerSize(10.dp) )
    // RoundedCornerShape(corner = CornerSize(7.dp))
    // Rectangle


    Button(
        modifier = modifier,
        shape = shape,
        onClick = {
            onClick()
        },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 7.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            iconId?.let{
                Icon(
                    painter =
                    painterResource(id = iconId),
                    contentDescription = "Icon"
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize.sp
            )
        }
    }
}