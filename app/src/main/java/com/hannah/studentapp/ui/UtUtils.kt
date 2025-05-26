package com.hannah.studentapp.ui

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat

// returns the color of the App Theme with ID attrResIt
fun Context.getThemeColor(attrResId : Int) : Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrResId, typedValue, true)
    return if (typedValue.resourceId != 0) {
        ContextCompat.getColor(this, typedValue.resourceId)
    } else {
        typedValue.data
    }
}
