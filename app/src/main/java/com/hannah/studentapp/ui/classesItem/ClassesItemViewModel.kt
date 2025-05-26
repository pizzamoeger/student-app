package com.hannah.studentapp.ui.classesItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hannah.studentapp.TimeInterval

class ClassesItemViewModel : ViewModel() {
    private var _classId : Int = 0

    private val _dailyTime = MutableLiveData<String>()
    private val _weeklyTime = MutableLiveData<String>()
    private val _monthlyTime = MutableLiveData<String>()
    private val _totalTime = MutableLiveData<String>()

    val dailyTime: LiveData<String> = _dailyTime
    val weeklyTime: LiveData<String> = _weeklyTime
    val monthlyTime: LiveData<String> = _monthlyTime
    val totalTime: LiveData<String> = _totalTime

    // sets id of class and then calls write function
    fun setId(id : Int) {
       _classId = id
        write()
    }

    // writes information to the variables
    fun write() {
        val thisClass = ClassesItem.get(_classId)

        _dailyTime.value = "Day ${ClassesItem.getTimeStringFromSeconds(thisClass.getSeconds(TimeInterval.DAY))}"
        _weeklyTime.value = "Week ${ClassesItem.getTimeStringFromSeconds(thisClass.getSeconds(TimeInterval.WEEK))}"
        _monthlyTime.value = "Month ${ClassesItem.getTimeStringFromSeconds(thisClass.getSeconds(TimeInterval.MONTH))}"
        _totalTime.value = "Total ${ClassesItem.getTimeStringFromSeconds(thisClass.getSeconds(TimeInterval.TOTAL))}"
    }
}