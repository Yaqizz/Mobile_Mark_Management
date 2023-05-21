package com.example.a4_basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

var currEditName = ""
var currEditDes = ""
var currEditTerm = ""
var currEditMark = ""
var f = "All Courses"
var s = "By Course Code"

// check positive integers between 0 and 100
fun isValidNum(g: String): Boolean {
    val regex = Regex("\\b([0-9]|[1-9][0-9]|100)\\b")
    return regex.matches(g)
}
// check valid course
fun checkCourse(name: String, term: String, grade: String): Boolean {
    if (name.isNotEmpty() && grade.isNotEmpty() && term.isNotEmpty()) {
        if (grade == "WD") {
            return true
        }
        if (isValidNum(grade))  {
            return true
        }
    }
    return false
}

class MainFragment : Fragment() {
    // set Background color for each course based on grade
    private fun setBGcolor(grade: String) : Int {
        if (grade == "WD") {
            return ContextCompat.getColor(requireContext(), R.color.wd)
        } else {
            when (grade.toInt()) {
                in 0..49 -> {
                    return ContextCompat.getColor(requireContext(), R.color.failed)
                }
                in 50..59 -> {
                    return ContextCompat.getColor(requireContext(), R.color.low)
                }
                in 60..90 -> {
                    return ContextCompat.getColor(requireContext(), R.color.good)
                }
                in 91..95 -> {
                    return ContextCompat.getColor(requireContext(), R.color.great)
                }
                in 96..100 -> {
                    return ContextCompat.getColor(requireContext(), R.color.excellent)
                }
            }
        }
        return ContextCompat.getColor(requireContext(), R.color.wd)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myVM = ViewModelProvider(requireActivity())[CourseViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val addbutton = root.findViewById<Button>(R.id.addButton)
        val filterSpinner = root.findViewById<Spinner>(R.id.filter_spinner)
        val sortSpinner = root.findViewById<Spinner>(R.id.sort_spinner)

        // add listener to filter spinner
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                f = parent.getItemAtPosition(position).toString()
                myVM.filterAndSort(f,s)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        // add listener to sortspinner
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                s = parent.getItemAtPosition(position).toString()
                myVM.filterAndSort(f,s)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        // add course
        addbutton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_mainFrag_to_addFrag)
            }
        }

        val courseListLayout = root.findViewById<LinearLayout>(R.id.course_list)
        myVM.currList.observe(viewLifecycleOwner) { courseList ->
            courseListLayout.removeAllViews()
            if (!courseList.isNullOrEmpty()) {
                for (course in courseList) {
                    val courseLayout = layoutInflater.inflate(R.layout.one_course, container, false)
                    val courseCodeTV = courseLayout.findViewById<TextView>(R.id.course_code_text_view)
                    val markTV = courseLayout.findViewById<TextView>(R.id.mark_text_view)
                    val termTV = courseLayout.findViewById<TextView>(R.id.term_text_view)
                    val descriptionTV = courseLayout.findViewById<TextView>(R.id.description_text_view)
                    val bgColor = setBGcolor(course.grade)
                    courseCodeTV.text = course.name
                    markTV.text = course.grade
                    termTV.text = course.term
                    descriptionTV.text = course.description
                    courseLayout.setBackgroundColor(bgColor)

                    // delete
                    val deleteButton = courseLayout.findViewById<Button>(R.id.one_delete)
                    deleteButton.setOnClickListener {
                        myVM.deleteCourse(course.name)
                        courseListLayout.removeView(courseLayout)
                    }

                    // update
                    val updateButton = courseLayout.findViewById<Button>(R.id.one_update)
                    updateButton.setOnClickListener {
                        currEditName = course.name
                        currEditDes = course.description
                        currEditMark = course.grade
                        currEditTerm = course.term
                        findNavController().navigate(R.id.action_mainFrag_to_editFrag)
                    }

                    courseListLayout.addView(courseLayout)
                }
            }
        }


        return root
    }
}