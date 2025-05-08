package com.example.studentapp.ui

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

// TODO
/*fun setSelectionElement(selection : , selected: Boolean, context: Context) {
    val color = ContextCompat.getColor(context, colorRes)
    textView.setTextColor(color)
    lineView.setBackgroundColor(color)
}*/

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
