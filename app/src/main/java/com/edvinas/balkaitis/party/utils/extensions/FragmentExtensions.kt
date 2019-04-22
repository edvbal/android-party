package com.edvinas.balkaitis.party.utils.extensions

import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    activity?.currentFocus?.let { view ->
        context?.let {
            val inputManager = it.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
