package com.bogdan801.digitalfarmer.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

fun saveBitmapToPrivateStorage(context: Context, bitmap: Bitmap, filename: String) {
    val fileOutputStream: FileOutputStream
    try {
        fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun readBitmapFromPrivateStorage(context: Context, filename: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val fileInputStream: FileInputStream = context.openFileInput(filename)
        bitmap = BitmapFactory.decodeStream(fileInputStream)
        fileInputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}