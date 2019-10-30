package me.kmmiller.baseui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.Exception

fun Context.hideKeyboard() {
    //https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = if(this is Activity) currentFocus ?: View(this) else View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Any?.nonNullString(): String {
    if(this == null) return ""

    return try {
        (this as? String) ?: ""
    } catch (e: Exception) {
        ""
    }
}

fun Any?.nonNullLong(): Long {
    if(this == null) return 0L

    return try {
        (this as? Long) ?: 0L
    } catch (e: Exception) {
        0L
    }
}