package com.example.a4_basic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CourseViewModel: ViewModel() {
    var currList = MutableLiveData<MutableList<Course>?>()
    private var wholeList = MutableLiveData<MutableList<Course>?>()
    init {
        currList.value = mutableListOf()
        wholeList.value = mutableListOf()
    }

    private fun filterList(courseList: MutableList<Course>?, filter: String): MutableList<Course>? {
        if (courseList == null) {
            return null
        }
        return when(filter) {
            "All Courses" -> {courseList}
            "CS Only" -> {
                courseList.filter { it.name.take(2) == "CS" }.toMutableList()
            }
            "Math Only" -> {
                courseList.filter { it.name.take(4) == "MATH" ||
                        it.name.take(4) == "STAT" ||
                        it.name.take(2) == "CO"}.toMutableList()
            }
            else -> {
                courseList.filter {
                    it.name.take(4) != "MATH" &&
                            it.name.take(4) != "STAT" &&
                            it.name.take(2) != "CO" &&
                            it.name.take(2) != "CS"
                }.toMutableList()
            }
        }
    }

    private fun sortList(courseList: MutableList<Course>?, sort: String): MutableList<Course>? {
        when (sort) {
            "By Term" -> {
                courseList?.sortWith(compareBy<Course> {
                    it.term.substring(1).toInt()
                }.thenByDescending { it.term.take(1) })
            }
            "By Course Code" -> {
                courseList?.sortBy { it.name}
            }
            else -> {
                courseList?.forEach {
                    if (it.grade == "WD") {
                        it.grade = "-1"
                    }
                }
                courseList?.sortByDescending { it.grade.toInt() }
                courseList?.forEach {
                    if (it.grade == "-1") {
                        it.grade = "WD"
                    }
                }
            }
        }
        return courseList
    }

    fun filterAndSort(filter: String, sort: String): MutableLiveData<MutableList<Course>?> {
        val filteredList = filterList(wholeList.value, filter)
        val sortedList = sortList(filteredList, sort)
        currList.value = sortedList
        return currList
    }

    fun addCourse(n: String, d: String, t: String, g: String) {
        wholeList.value?.add(Course(n, d, t, g))
    }
    fun deleteCourse(n: String) {
        wholeList.value?.removeIf {it.name == n}
    }

    fun updateCourse(n: String, d: String, t: String, g: String) {
        wholeList.value?.forEach {
            if (it.name == n) {
                it.grade = g
                it.term = t
                it.description = d
            }
        }
    }
}