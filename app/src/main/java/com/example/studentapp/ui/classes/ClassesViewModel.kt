package com.example.studentapp.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.security.auth.Subject

class ClassesViewModel : ViewModel() {
    private val _classList = MutableLiveData<List<Class>>(emptyList())
    private var nextId = 0;

    val classList : LiveData<List<Class>> get() = _classList

    private val _text = MutableLiveData<String>().apply {
        value = "This is classes Fragment"
    }
    val text: LiveData<String> = _text

    fun addClass(name: String) {
        val newList = _classList.value.orEmpty() + Class(nextId++, name)
        _classList.value = newList
    }

    fun deleteClass(id: Int) {
        _classList.value = _classList.value?.filterNot {it.id == id}
    }
}