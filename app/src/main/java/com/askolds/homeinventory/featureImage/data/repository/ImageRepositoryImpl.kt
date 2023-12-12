package com.askolds.homeinventory.featureImage.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.askolds.homeinventory.featureImage.data.dao.ImageDao
import com.askolds.homeinventory.featureImage.data.model.ImageEntity
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class ImageRepositoryImpl(
    private val context: Context,
    private val dao: ImageDao
) : ImageRepository {
    override suspend fun insert(image: ImageEntity, subfolder: String): Int {
        // insert image into db (to get id)
        val id = dao.insert(image).toInt()
        // open file
        val inputStream = context.contentResolver.openInputStream(
            Uri.parse(image.imageUri)
        )
        // output file
        val dir = File(context.filesDir, subfolder)
        if (!dir.exists())
            dir.mkdir()
        val file = File(dir,"${id}.webp")
        val outputStream = FileOutputStream(file)//context.openFileOutput("${id}.webp", Context.MODE_PRIVATE)
        // copy input file to output file
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        // update image with real uri
        dao.update(
            image.copy(
                id = id,
                imageUri = file.absolutePath
            )
        )

        // delete temp file
        val tempFile = File(URI(image.imageUri).path)
        try {
            if (tempFile.exists())
                tempFile.delete()
        } catch (ex: SecurityException) {
            Log.w("FileSave", ex.message ?: "")
        }

        return id
    }

    override suspend fun getById(id: Int): ImageEntity? {
        return dao.getById(id)
    }

    override suspend fun delete(image: ImageEntity) {
        // file
        val file = File(image.imageUri)
        try {
            if (file.exists())
                file.delete()
        } catch (ex: SecurityException) {
            Log.w("FileDelete", ex.message ?: "")
        }
        dao.delete(image)
    }

    override suspend fun deleteById(id: Int) {
        val image = getById(id) ?: return
        // file
        val file = File(image.imageUri)
        try {
            if (file.exists())
                file.delete()
        } catch (ex: SecurityException) {
            Log.w("FileDelete", ex.message ?: "")
        }
        dao.deleteById(id)
    }
}