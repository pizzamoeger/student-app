package com.example.studentapp.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClassesViewModel : ViewModel() {
    private val _classesList = MutableLiveData<List<ClassesItem>>(emptyList())
    private var nextId = 0;

    val classList : LiveData<List<ClassesItem>> get() = _classesList

    private val _text = MutableLiveData<String>().apply {
        value = "This is classes Fragment"
    }
    val text: LiveData<String> = _text

    fun addClass(name: String) {
        val newList = _classesList.value.orEmpty() + ClassesItem(nextId++, name, 0, 0)
        _classesList.value = newList
    }

    fun deleteClass(id: Int) {
        _classesList.value = _classesList.value?.filterNot {it.id == id}
    }
}