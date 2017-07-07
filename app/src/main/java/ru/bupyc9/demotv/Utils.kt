package ru.bupyc9.demotv

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.view.WindowManager
import android.widget.Toast
import android.media.MediaMetadataRetriever

fun getDisplaySize(context: Context): Point {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

    return size
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun convertDpToPixel(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density

    return Math.round(dp * density)
}

fun formatMillis(millis: Int): String {
    var m = millis
    var result = ""
    val hr = m / 3600000
    m %= 3600000
    val min = m / 60000
    m %= 60000
    val sec = m / 1000
    if (hr > 0) {
        result += hr.toString() + ":"
    }
    if (min >= 0) {
        if (min > 9) {
            result += min.toString() + ":"
        } else {
            result += "0$min:"
        }
    }
    if (sec > 9) {
        result += sec
    } else {
        result += "0" + sec
    }

    return result
}

fun getDuration(videoUrl: String): Long {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(videoUrl, hashMapOf())

    return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
}

fun hasPermission(context: Context, permission: String): Boolean {
    return PackageManager.PERMISSION_GRANTED == context.packageManager.checkPermission(permission, context.packageName)
}