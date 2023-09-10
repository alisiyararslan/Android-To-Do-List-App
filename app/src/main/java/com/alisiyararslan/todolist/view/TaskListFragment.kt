package com.alisiyararslan.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.alisiyararslan.todolist.R
import com.alisiyararslan.todolist.databinding.FragmentTaskListBinding
import com.alisiyararslan.todolist.roomdb.TaskDao
import com.alisiyararslan.todolist.roomdb.TaskDatabase


class TaskListFragment : Fragment() {


    private var _binding: FragmentTaskListBinding? = null

    private val binding get() = _binding!!

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db= Room.databaseBuilder(requireContext(),TaskDatabase::class.java,"Tasks").build()
        taskDao=db.taskDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentTaskListBinding.inflate(inflater, container, false)


        val view = binding.root
        return view
//        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTaskButton.setOnClickListener{
            addTask(it)
        }




    }

    fun addTask(view: View){
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(requireActivity().supportFragmentManager,"BottomSheetDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}