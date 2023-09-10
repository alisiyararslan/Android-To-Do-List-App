package com.alisiyararslan.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.alisiyararslan.todolist.R
import com.alisiyararslan.todolist.adapter.TaskAdapter
import com.alisiyararslan.todolist.databinding.FragmentTaskListBinding
import com.alisiyararslan.todolist.model.Task
import com.alisiyararslan.todolist.roomdb.TaskDao
import com.alisiyararslan.todolist.roomdb.TaskDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class TaskListFragment : Fragment() {


    private var _binding: FragmentTaskListBinding? = null

    private val binding get() = _binding!!

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao

    private var compositeDisposible= CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db= Room.databaseBuilder(requireContext(),TaskDatabase::class.java,"Tasks").build()
        taskDao=db.taskDao()

        compositeDisposible.add(
            taskDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

    }

    fun handleResponse(taskList:List<Task>){
//        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
//        val adapter=ArtAdapter(artList)
//        binding.recyclerView.adapter=adapter

        val uncompletedTasks = taskList.filter { !it.isCompleted }
        val completedTasks = taskList.filter { it.isCompleted }

        binding.recyclerViewUnCompletedTask.layoutManager = LinearLayoutManager(requireContext())
        val un_completed_adapter = TaskAdapter(uncompletedTasks,db,taskDao)
        binding.recyclerViewUnCompletedTask.adapter =un_completed_adapter

        if (completedTasks.size>0){

            binding.completedTextCountLayout.visibility = View.VISIBLE
            binding.recyclerViewCompletedTask.visibility = View.VISIBLE

            binding.recyclerViewCompletedTask.layoutManager = LinearLayoutManager(requireContext())
            val completed_adapter = TaskAdapter(completedTasks,db,taskDao)
            binding.recyclerViewCompletedTask.adapter =completed_adapter

            binding.completedCountText.setText("Completed ("+completedTasks.size.toString()+")")


        }else{
            binding.completedTextCountLayout.visibility = View.INVISIBLE
            binding.recyclerViewCompletedTask.visibility = View.INVISIBLE
        }



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
        compositeDisposible.clear()
        _binding = null
    }


}