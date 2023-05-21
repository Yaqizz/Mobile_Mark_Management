package com.example.a4_basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class EditFragment : Fragment() {
    private val termList = listOf("F20", "W21", "S21","F21", "W22", "S22","F22", "W23", "S23","F23")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myVM = ViewModelProvider(requireActivity())[CourseViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_edit, container, false)
        val cancelbutton = root.findViewById<Button>(R.id.CancelButton)
        val submitButton = root.findViewById<Button>(R.id.SubmitButton)
        val codeTV = root.findViewById<TextView>(R.id.courseCodeName)
        val descriptionET = root.findViewById<EditText>(R.id.description)
        val markET= root.findViewById<EditText>(R.id.mark)
        val wdSwitch = root.findViewById<Switch>(R.id.wd_switch)
        var currGrade = ""
        val termS = root.findViewById<Spinner>(R.id.term_spinner)

        // init data from main fragment
        codeTV.text = currEditName
        descriptionET.setText(currEditDes)
        if (currEditMark == "WD") {
            wdSwitch.isChecked = true
            markET.isEnabled = false
            markET.setText("")
        } else {
            wdSwitch.isChecked = false
            markET.setText(currEditMark)
        }
        termS.setSelection(termList.indexOf(currEditTerm))

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
                findNavController().navigate(R.id.action_edit_pop)
            }
        }
        // add listener to submit
        submitButton.setOnClickListener {
            val name = codeTV.text.toString()
            var grade = markET.text.toString()
            val term = termS.selectedItem.toString()
            val description = descriptionET.text.toString()
            if (wdSwitch.isChecked) {
                grade = "WD"
            }
            if (checkCourse(name, term, grade)) {
                myVM.updateCourse(name, description, term, grade)
                myVM.filterAndSort(f,s)
                findNavController().navigate(R.id.action_edit_pop)
            }
        }

        return root
    }
}