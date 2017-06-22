package ru.bupyc9.demotv

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.widget.Toast

class Utils {
    companion object {
        @JvmStatic fun getDisplaySize(context: Context): Point {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            return size
        }

        @JvmStatic fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        @JvmStatic fun convertDpToPixel(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density

            return  Math.round(dp * density)
        }

        @JvmStatic fun formatMillis(millis: Int): String {
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
    }
}