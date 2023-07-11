package com.example.onlinebuying.Widgets

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebuying.R
import com.example.onlinebuying.ui.theme.Orange
import com.example.onlinebuying.ui.theme.Teal

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameOutlinedTextField(
    modifier: Modifier,
    text : String,
    labelText : String,
    errorState : Boolean,
    iconClick : () -> Unit,
    onValueChange : (String) -> Unit
){

    OutlinedTextField(
        modifier = modifier,
        value = text,
        shape = RoundedCornerShape(8.dp),
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = labelText,
                color = Color.Black
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            errorLabelColor = Color.Red,
            cursorColor = Color.Black,
            containerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.DarkGray,
            focusedIndicatorColor = Teal

        ),
        trailingIcon = {
            IconButton(
                onClick =
                {
                    iconClick()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Tralaiding icon"
                )
            }
        },
        singleLine = true,
        isError = errorState,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 20.sp
        )

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordOutlinedTextField(
    modifier: Modifier,
    text : String,
    labelText : String,
    errorState : Boolean,
    passwordVisibility : Boolean,
    isThereTrailingIcon : Boolean,
    iconClick : () -> Unit,
    onValueChange : (String) -> Unit
){

    OutlinedTextField(
        modifier = modifier,
        value = text,
        shape = RoundedCornerShape(8.dp),
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = labelText,
                color = Color.Black
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            errorLabelColor = Color.Red,
            cursorColor = Color.Black,
            containerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Black,
            focusedIndicatorColor = Teal

        ),
        visualTransformation =
        if(passwordVisibility == null || passwordVisibility) VisualTransformation.None
        else PasswordVisualTransformation(),
        trailingIcon = {
            if(isThereTrailingIcon){
                IconButton(
                    onClick =
                    {
                        iconClick()
                    }
                ) {

                    Icon(
                        painter = painterResource(
                            id = when(passwordVisibility){
                                true -> R.drawable.ic_visibility_off
                                false -> R.drawable.ic_visibility_on
                            }
                        ),
                        contentDescription = "Tralaiding icon"
                    )
                }
            }

        },
        singleLine = true,
        isError = errorState,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii
        ),
        textStyle = MaterialTheme.typography.bodyMedium

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    modifier: Modifier,
    text : String,
    lineNumber: Int,
    labelText : String,
    keyboardType: KeyboardType,
    errorState : Boolean,
    iconClick : () -> Unit,
    onValueChange : (String) -> Unit
){

    OutlinedTextField(
        modifier = modifier,
        value = text,
        shape = RoundedCornerShape(8.dp),
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = labelText,
                color = Color.Black
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            errorLabelColor = Color.Red,
            cursorColor = Color.Black,
            containerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.DarkGray,
            focusedIndicatorColor = Teal

        ),
        trailingIcon = {
            IconButton(
                onClick =
                {
                    iconClick()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Tralaiding icon"
                )
            }
        },
        maxLines = lineNumber,
        isError = errorState,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 20.sp
        )

    )

}


@Composable
fun CustomSnackkBar(
    message : String,
    containerColor : Color,
    icon : Icon?
){

    Card(
        modifier = Modifier
            .padding(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ){
        Snackbar(
            modifier = Modifier,
            containerColor = containerColor
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = message,
                    style =  MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}