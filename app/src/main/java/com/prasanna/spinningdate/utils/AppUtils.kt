package com.prasanna.spinningdate.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {
    companion object {

        fun getGrayAreaHeight(activity: Activity, height: Float): Float {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, displayMetrics)
        }

        fun getDeviceHeight(activity: Activity): Int {
            return getDeviceDimensions(activity).heightPixels
        }

        fun getDeviceWidth(activity: Activity): Int {
            return getDeviceDimensions(activity).widthPixels
        }

        private fun getDeviceDimensions(activity: Activity): DisplayMetrics {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics
        }

        fun getStatusBarHeight(context: Context): Int {
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0 && Build.VERSION.SDK_INT != Build.VERSION_CODES.P)
                context.resources.getDimensionPixelSize(resourceId)
            else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Math.ceil((25 * context.resources.displayMetrics.density).toDouble()).toInt()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                Math.ceil((24 * context.resources.displayMetrics.density).toDouble()).toInt()
            } else 0
        }

        fun getDateFromJson(data: String): Date? {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())
            return dateFormat.parse(data)
        }

        fun getFormattedDate(date: Date?): String {
            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return dateFormat.format(date).toString()
        }
    }

}