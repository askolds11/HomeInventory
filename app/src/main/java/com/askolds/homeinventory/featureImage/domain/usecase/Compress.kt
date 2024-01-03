package com.askolds.homeinventory.featureImage.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class Compress(
    private val context: Context
) {
    /**
     * Returns path of compressed file
     */
    suspend operator fun invoke(imageUri: Uri): Uri {
        val contentResolver = context.contentResolver
        return withContext (Dispatchers.IO) {
            // get bitmap from uri
            val bitmap = contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
            // compress
            @Suppress("DEPRECATION") val compressFormat =
                if(Build.VERSION.SDK_INT >= 30)
                    Bitmap.CompressFormat.WEBP_LOSSY
                else
                    Bitmap.CompressFormat.WEBP
            val tempFile = File(context.filesDir, "temp.webp")
            val outputStream = FileOutputStream(tempFile)
            bitmap?.compress(
                compressFormat,
                80,
                outputStream
            )
            // output compressed file
            outputStream.flush()
            outputStream.close()
            return@withContext tempFile.toUri()
        }
    }
}