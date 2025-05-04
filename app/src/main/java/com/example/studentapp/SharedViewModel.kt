package com.example.studentapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentapp.ui.classes.ClassesItem

class SharedViewModel : ViewModel() {
    private val _classesList = MutableLiveData<List<ClassesItem>>(emptyList())
    private var nextId = 0;

    val classList : LiveData<List<ClassesItem>> get() = _classesList

    fun addClass(name: String) {
        val newList = _classesList.value.orEmpty() + ClassesItem(nextId++, name, 0, 0)
        _classesList.value = newList
    }

    fun deleteClass(id: Int) {
        _classesList.value = _classesList.value?.filterNot {it.id == id}
    }
}