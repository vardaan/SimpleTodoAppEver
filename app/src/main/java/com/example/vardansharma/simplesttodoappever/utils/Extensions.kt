package com.example.vardansharma.simplesttodoappever.utils

/**
 * Created by vardansharma on 14/05/17.
 */


import android.content.Context
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by vardansharma on 14/05/17.
 */


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(message: Int) =
        Toast.makeText(this, this.getText(message), Toast.LENGTH_SHORT).show()

fun CharSequence.isValidEmail(): Boolean {
    when (this) {
        null -> return false
        else -> return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Context.snakbar(view: View, message: CharSequence) =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)

fun Context.snakbar(view: View, msgRes: Int) =
        Snackbar.make(view, msgRes, Snackbar.LENGTH_LONG)