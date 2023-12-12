package com.askolds.homeinventory.featureImage.domain.usecase

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFileName(
    private val context: Context
) {
    suspend operator fun invoke(imageUri: Uri): String? {
        // https://developer.android.com/training/secure-file-sharing/retrieve-info
        val contentResolver = context.contentResolver
        return withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(imageUri, null, null, null, null)
                ?: return@withContext null
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val fileName = cursor.getString(nameIndex)
            cursor.close()
            return@withContext fileName
        }
    }
}