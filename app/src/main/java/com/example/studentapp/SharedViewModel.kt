package com.example.studentapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentapp.ui.classes.ClassesItem

class SharedViewModel : ViewModel() {
    private val _classesList = MutableLiveData<List<ClassesItem>>(emptyList())
    // TODO save classes when exiting apps
    // need GSON for this
    private var nextId = 0;
    private var _currentClass = MutableLiveData<ClassesItem?> ()

    val classList : LiveData<List<ClassesItem>> get() = _classesList
    val currentClass : LiveData<ClassesItem?> get() = _currentClass

    fun addClass(name: String) {
        val newList = _classesList.value.orEmpty() + ClassesItem(nextId++, name, 0, 0)
        _classesList.value = newList
    }

    fun deleteClass(id: Int) {
        _classesList.value = _classesList.value?.filterNot {it.id == id}
    }

    fun switchClass(item: ClassesItem) : Boolean {
        // returns true if the class changed
        if (_currentClass.value == item) return false
        _currentClass.value = item
        return true
    }
}