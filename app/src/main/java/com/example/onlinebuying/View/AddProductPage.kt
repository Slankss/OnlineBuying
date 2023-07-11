package com.example.onlinebuying.View

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.onlinebuying.R
import com.example.onlinebuying.Repository.FirebaseRepository
import com.example.onlinebuying.Widgets.CustomButton
import com.example.onlinebuying.Widgets.CustomOutlinedTextField
import com.example.onlinebuying.Widgets.UsernameOutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import com.example.onlinebuying.ui.theme.Navy
import com.example.onlinebuying.ui.theme.Teal
import firebase.com.protolitewrapper.BuildConfig
import java.util.Objects

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun AddProductPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var nameErrorState by remember { mutableStateOf(false) }
    var descriptionErrorState by remember { mutableStateOf(false) }
    var stockErrorState by remember { mutableStateOf(false) }
    var priceErrorState by remember { mutableStateOf(false) }

    var dialogVisibility by remember { mutableStateOf(false) }

    //var cameraPermissionState

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->

           imageUri = uri
    }

    var cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()){ uri ->

        //mageUri = uri
    }

    // get image from gallery
    imageUri?.let{
        if(Build.VERSION.SDK_INT < 28){
            imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
        }
        else{
            val source = ImageDecoder.createSource(context.contentResolver,it)
            imageBitmap = ImageDecoder.decodeBitmap(source)
        }
    }


    var dialogColor = Color(0xFFFFFFFF)

    if(dialogVisibility){
        Dialog(
            onDismissRequest = { dialogVisibility = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                usePlatformDefaultWidth = true
            )
        ) {
            Surface(
                modifier = Modifier,
                shape = RoundedCornerShape(16.dp),
                elevation = 20.dp,
                border = BorderStroke(width = 1.dp,Color.DarkGray),
                color =  dialogColor
                //border = BorderStroke(width = 2.dp, color = Color.Black)

            ) {
                Text(
                    text = "Resim Yükle",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Card(
                        modifier = Modifier
                            .clickable {
                                launcher.launch("image/*")
                                dialogVisibility = false
                            }
                            .weight(1f),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 7.dp
                        ),
                        colors =  CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_galery),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Galeriden Al",
                                    color = Color.Black
                                )
                            }
                        }

                    Spacer(modifier = Modifier.width(20.dp))
                    Card(
                        modifier = Modifier
                            .weight(1f),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 7.dp
                        ),
                        colors =  CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Fotoğraf Çek",
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }


    Scaffold() {
        Column(
            modifier = Modifier
                .background(
                    color = Color.White
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            if(imageUri != null && imageBitmap != null){
                Card(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(200.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            dialogVisibility = true
                        },
                    elevation = CardDefaults.cardElevation(
                        14.dp
                    )
                ){
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            else{
                Card(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clickable {
                            dialogVisibility = true
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(
                        14.dp
                    )
                ){
                    Column(
                        modifier = Modifier
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_image),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fotoğraf Ekle",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = 22.sp,
                                    color = Color.Black
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Image(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null)
                        }
                    }
                }
            }


            ProductInput(
                labelText = "Ürün adı",
                inputText = name,
                keyboardType = KeyboardType.Text,
                lineNumber = 1,
                errorState = nameErrorState,
                iconClick =
                {
                    name = ""
                },
                onValueChange = {
                    name = it
                }
            )

            ProductInput(
                labelText = "Ürün Açıklaması",
                inputText = description,
                keyboardType = KeyboardType.Text,
                lineNumber = 3,
                errorState = descriptionErrorState,
                iconClick =
                {
                    description = ""
                },
                onValueChange = {
                    description = it
                }
            )

            ProductInput(
                labelText = "Ürün Stok Miktarı",
                inputText = stock,
                keyboardType = KeyboardType.Number,
                lineNumber = 1,
                errorState = stockErrorState,
                iconClick =
                {
                    stock = ""
                },
                onValueChange = {
                    stock = it
                }
            )

            ProductInput(
                labelText = "Ürün Fiyatı",
                inputText = price,
                keyboardType = KeyboardType.Number,
                lineNumber = 1,
                errorState = priceErrorState,
                iconClick =
                {
                    price = ""
                },
                onValueChange = {
                    price = it
                }
            )

            CustomButton(
                modifier = Modifier
                    .padding(horizontal = 100.dp),
                shape = RoundedCornerShape(14.dp),
                text = "Ekle",
                fontSize = 18,
                textColor = Color.White,
                containerColor = Navy,
                iconId = R.drawable.ic_add
            ) {

            }


        }
    }

}

@Composable
fun ProductInput(
    labelText : String,
    inputText : String,
    keyboardType: KeyboardType,
    lineNumber : Int,
    errorState : Boolean,
    iconClick :() -> Unit,
    onValueChange : (String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 7.dp, start = 7.dp),
            text = labelText,
            color = Color.Black,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium
        )
        CustomOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            text = inputText,
            labelText = "",
            lineNumber = lineNumber,
            keyboardType = keyboardType,
            errorState = errorState,
            iconClick = {
                iconClick()
            },
            onValueChange ={
                onValueChange(it)
            }
        )
    }
}

fun getImageFromGallery(){

}

fun getImageFromCamera(){

}