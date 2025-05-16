package com.example.studentapp.ui.assignments.checkbox

sealed class CheckboxItem {
    data class Checkbox(val text: String, var checked: Boolean = false) : CheckboxItem()
    object InputBox : CheckboxItem()
}
