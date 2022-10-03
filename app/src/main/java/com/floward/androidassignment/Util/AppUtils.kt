package com.floward.androidassignment.Util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }

fun logMessage(message: String) {
    if (com.floward.androidassignment.BuildConfig.DEBUG) {
        Log.i("#FlowardTest#", message)
    }
}

fun glideCacheClear(context: Context) {
    Thread {
        Glide.get(context).clearDiskCache()
    }.start()
}

fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    capabilities.also {
        if (it != null) {
            if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
        }
    }
    return false
}

@SuppressLint("ShowToast")
fun toast(context: Context, message: String) {
     Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun fromHtml(htmlContent: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
        // we are using this flag to give a consistent behaviour
        Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(htmlContent).toString()
    }
}
