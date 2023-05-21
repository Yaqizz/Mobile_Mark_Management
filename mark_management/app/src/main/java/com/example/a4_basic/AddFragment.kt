package com.example.a4_basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myVM = ViewModelProvider(requireActivity())[CourseViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_add, container, false)
        val cancelbutton = root.findViewById<Button>(R.id.CancelButton)
        val createButton = root.findViewById<Button>(R.id.CreateButton)
        val courseCodeET = root.findViewById<EditText>(R.id.courseCode)
        val descriptionET = root.findViewById<EditText>(R.id.description)
        val markET= root.findViewById<EditText>(R.id.mark)
        val wdSwitch = root.findViewById<Switch>(R.id.wd_switch)
        var currGrade = ""
        val termS = root.findViewById<Spinner>(R.id.term_spinner)

        // add listener to switch
        wdSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                markET.isEnabled = false
                currGrade = markET.text.toString()
                markET.setText("")
            } else {
                markET.isEnabled = true
                markET.setText(currGrade)
            }
        }
        // add listener to cancel
        cancelbutton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_add_pop)
            }
        }
        // add listener to create
        createButton.setOnClickListener {
            val name = courseCodeET.text.toString()
            var grade = markET.text.toString()
            val term = termS.selectedItem.toString()
            val description = descriptionET.text.toString()
            if (wdSwitch.isChecked) {
                grade = "WD"
            }
            if (checkCourse(name, term, grade)) {
                myVM.addCourse(name, description, term, grade)
                myVM.filterAndSort(f,s)
            }
            findNavController().navigate(R.id.action_add_pop)
        }

        return root
    }
}