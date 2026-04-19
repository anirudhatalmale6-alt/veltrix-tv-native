package com.veltrix.tv.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import coil.load
import coil.transform.RoundedCornersTransformation
import com.veltrix.tv.R

fun Context.toast(message: String, long: Boolean = false) {
    Toast.makeText(this, message, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun ImageView.loadImage(url: String?, cornerRadius: Float = 4f) {
    load(url) {
        crossfade(true)
        placeholder(R.drawable.placeholder_image)
        error(R.drawable.placeholder_image)
        transformations(RoundedCornersTransformation(cornerRadius))
    }
}

fun String.ensureHttp(): String {
    return when {
        startsWith("http://") || startsWith("https://") -> this
        else -> "http://$this"
    }
}

fun String?.orDefault(default: String = ""): String {
    return if (this.isNullOrBlank()) default else this
}
