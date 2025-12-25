package com.elevatestudio.careerlink.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {

    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            // 1. Dapatkan nama file asli
            val fileName = getFileName(context, uri)

            // 2. Buat file sementara (temporary) di folder cache aplikasi
            //    Ini aman karena tidak butuh izin storage luas
            val tempFile = File(context.cacheDir, fileName)

            // 3. Buka Stream dari URI lalu Copy isinya ke file sementara
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(tempFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // 4. Kembalikan file sementara yang sudah berisi data
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Helper untuk mengambil nama file yang benar
    private fun getFileName(context: Context, uri: Uri): String {
        var name = "temp_file_${System.currentTimeMillis()}" // Default fallback
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)

        returnCursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }

        // Pastikan nama file bersih (kadang ada spasi aneh)
        return name.replace(" ", "_")
    }
}