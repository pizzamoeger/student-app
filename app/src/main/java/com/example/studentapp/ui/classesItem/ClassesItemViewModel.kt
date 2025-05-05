package com.example.studentapp.ui.classesItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClassesItemViewModel : ViewModel() {
    private var _classId : Int = 0

    private val _text = MutableLiveData<String>().apply {
        value = "This is class $_classId"
    }
    val text: LiveData<String> = _text

    fun setId(id : Int) {
       _classId = id
        _text.value = "This is class $_classId"
    }
}