package com.example.onlinebuying.Model

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.onlinebuying.R
import java.io.File

class ImageFileProvider : FileProvider(
    R.xml.file_paths
) {

    companion object{
        fun getImageUri(context : Context) : Uri {
            val directory = File(context.cacheDir,"images")
            directory.mkdirs()

            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory)

            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )

        }
    }

}

fun Context.createImageFile(name : String) : File {

    var image = File.createTempFile(
        name,
        ".jpg"
    )
    return image
}