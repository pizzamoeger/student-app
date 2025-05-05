package com.example.studentapp.ui.classesItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentapp.SharedData

class ClassesItemViewModel : ViewModel() {
    private var _classId : Int = 0

    private val _text = MutableLiveData<String>().apply {
        value = "This is class $_classId"
    }
    private val _dailyTime = MutableLiveData<String>()
    private val _weeklyTime = MutableLiveData<String>()
    private val _monthlyTime = MutableLiveData<String>()
    private val _totalTime = MutableLiveData<String>()

    val text: LiveData<String> = _text
    val dailyTime: LiveData<String> = _dailyTime
    val weeklyTime: LiveData<String> = _weeklyTime
    val monthlyTime: LiveData<String> = _monthlyTime
    val totalTime: LiveData<String> = _totalTime

    // TODO find a better way to do this
    // sets id of class and then calls write function
    fun setId(id : Int) {
       _classId = id
        _text.value = "This is class $_classId"
        write()
    }

    // writes information to the variables
    fun write() {
        val thisClass = SharedData.classList.value?.find { it.id == _classId }

        _dailyTime.value = "Day ${ClassesItem.getTimeStringFromSeconds(thisClass!!.secondsToday())}"
        _weeklyTime.value = "Week ${ClassesItem.getTimeStringFromSeconds(thisClass.secondsThisWeek())}"
        _monthlyTime.value = "Month ${ClassesItem.getTimeStringFromSeconds(thisClass.secondsThisMonth())}"
        _totalTime.value = "Total ${ClassesItem.getTimeStringFromSeconds(thisClass.secondsTotal())}"
    }
}