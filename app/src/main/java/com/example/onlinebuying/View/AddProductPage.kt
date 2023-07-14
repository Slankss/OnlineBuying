package com.example.onlinebuying.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.onlinebuying.Model.ImageFileProvider
import com.example.onlinebuying.Model.Product
import com.example.onlinebuying.Model.createImageFile
import com.example.onlinebuying.ViewModel.AddProcess
import com.example.onlinebuying.ViewModel.AddProductPageViewModel
import com.example.onlinebuying.ViewModelFactory.AddProductPageViewModelFactory
import com.example.onlinebuying.Widgets.LoadingDialog
import com.example.onlinebuying.Widgets.SuccessDialog
import com.example.onlinebuying.ui.theme.Navy
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import io.grpc.ExperimentalApi
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.net.URI
import java.util.*

@OptIn(ExperimentalCoilApi::class, ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun AddProductPage(
    navController: NavController,
    firebaseRepository: FirebaseRepository
){

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var addProductViewModel : AddProductPageViewModel = viewModel(
        factory = AddProductPageViewModelFactory(firebaseRepository)
    )

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

    val cameraPermission = Manifest.permission.CAMERA

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview() )
    {
            imageBitmap = it
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let{
            getImageFromGallery(context,it){ bitmap ->
                imageBitmap = bitmap
            }
        }
    }

    val requestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if(isGranted){
            // show camera
            val uri = ImageFileProvider.getImageUri(context)
            imageUri = uri
            cameraLauncher.launch(null)
        }
        else{
            // showDialog
        }
    }

    var loading_dialog_visibility by remember { mutableStateOf(false) }
    var add_process = addProductViewModel.addProcess.collectAsState()

    when(add_process.value){
        is AddProcess.Loading -> {
            loading_dialog_visibility = true
        }
        is AddProcess.Success ->{
            SuccessDialog(text = "Ürün başarıyla eklendi") {
                addProductViewModel.setAddProcess(AddProcess.NotStarted)
                name = ""
                description = ""
                stock = ""
                price = ""
                imageBitmap = null
            }
        }
        is AddProcess.Error -> {
            SuccessDialog(text = "Ürün eklenirken hata oluştu") {
                addProductViewModel.setAddProcess(AddProcess.NotStarted)
            }
        }
        else -> {
            loading_dialog_visibility = false
        }
    }
    loading_dialog_visibility = when(add_process.value){
        is AddProcess.Loading -> true
        else -> false
    }

    if(loading_dialog_visibility){
        LoadingDialog()
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
                                imageLauncher.launch("image/*")
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
                            .clickable {
                                checkAndRequestCameraPermission(
                                    context,
                                    cameraPermission
                                ) { isGranted ->
                                    when (isGranted) {
                                        true -> {
                                            try {
                                                val uri = ImageFileProvider.getImageUri(context)
                                                imageUri = uri
                                                cameraLauncher.launch(null)
                                                dialogVisibility = false
                                            } catch (e: Exception) {
                                                Log.e("errorumsu", e.localizedMessage)
                                            }
                                        }

                                        else -> requestLauncher.launch(cameraPermission)
                                    }
                                }
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
            var focusManager = LocalFocusManager.current

            var maxPrice = 10000000 // 10 milyon
            var maxStock = 1000

            if(imageBitmap != null){
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
                        bitmap = imageBitmap?.asImageBitmap()!!,
                        contentDescription = null,
                        modifier = Modifier
                            .requiredSize(200.dp),
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
                limitText = "(azami 100 karakter)",
                iconClick =
                {
                    name = ""
                },
                onValueChange = {
                    if(it.length <= 100)
                        name = it
                }
            )

            ProductInput(
                labelText = "Ürün Açıklaması",
                inputText = description,
                keyboardType = KeyboardType.Text,
                lineNumber = 3,
                limitText = "(azami 250 karakter)",
                errorState = descriptionErrorState,
                iconClick =
                {
                    description = ""
                },
                onValueChange = {
                    if(it.length <= 250)
                       description = it
                }
            )

            ProductInput(
                labelText = "Ürün Stok Miktarı",
                inputText = stock,
                keyboardType = KeyboardType.Number,
                lineNumber = 1,
                limitText = "(azami 1000 adet)",
                errorState = stockErrorState,
                iconClick =
                {
                    stock = ""
                },
                onValueChange = {
                    if(it.isBlank()){
                        stock = it
                    }
                    else {
                        val stock_double = it.trim().toDouble()
                        if(stock_double <= maxStock)
                            stock = it
                    }
                }
            )

            ProductInput(
                labelText = "Ürün Fiyatı",
                inputText = price,
                keyboardType = KeyboardType.Decimal,
                lineNumber = 1,
                limitText = "(azami 10 milyon tl)",
                errorState = priceErrorState,
                iconClick =
                {
                    price = ""
                },
                onValueChange = {
                    if(it.isBlank()){
                        price = it
                    }
                    else {
                        val price_double = it.trim().toDouble()
                        if(price_double <= maxPrice)
                            price = it
                    }
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

                nameErrorState = name.isBlank()
                descriptionErrorState = description.isBlank()
                stockErrorState = stock.isBlank()
                priceErrorState = price.isBlank()

                if(!nameErrorState && !descriptionErrorState && !stockErrorState && !priceErrorState){
                    if(imageBitmap != null){
                        var product = Product(null,name,description,null, price.toDouble()
                            ,stock.toInt(),null)

                        addProductViewModel.addProduct(product,imageBitmap!!)
                    }
                    else{
                        scope.launch {
                            snackbarHostState.showSnackbar(message = "Ürünün fotoğrafını yükleyin!")
                        }
                    }
                }

            }


        }
    }

}

@Composable
fun ProductInput(
    labelText : String,
    limitText : String,
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
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 7.dp, start = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier ,
                text = labelText,
                color = Color.Black,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier
                    .padding(start =  5.dp),
                text = limitText,
                color = Color.Gray,
                fontSize = 17.sp,
                style = MaterialTheme.typography.titleMedium
            )
        }
       
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


fun checkAndRequestCameraPermission(
    context: Context,
    permission: String,
    isGranted : (Boolean) -> Unit
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)

    var isGrantedState = permissionCheckResult == PackageManager.PERMISSION_GRANTED
    isGranted(isGrantedState)
}
fun getImageFromGallery(context : Context,imageUri : Uri,getBitmap : (Bitmap) ->Unit){
    try {
        if(Build.VERSION.SDK_INT < 28){
            var imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,imageUri)
            getBitmap(imageBitmap)
        }
        else{
            val source = ImageDecoder.createSource(context.contentResolver,imageUri)
            var imageBitmap = ImageDecoder.decodeBitmap(source)
            getBitmap(imageBitmap)
        }
    }catch (e : Exception){
        Log.e("errorumsu",e.localizedMessage)
    }
}


