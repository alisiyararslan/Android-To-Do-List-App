package com.alisiyararslan.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alisiyararslan.todolist.R
import com.alisiyararslan.todolist.databinding.FragmentTaskListBinding


class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null

    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.addButton.setOnClickListener{
            addTask(it)
        }

        binding
    }

    fun addTask(view: View){

    }


}