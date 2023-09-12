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
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.util.*
import kotlin.collections.ArrayList


class TaskListFragment : Fragment() {


    private var _binding: FragmentTaskListBinding? = null

    private val binding get() = _binding!!

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao

    private var compositeDisposible= CompositeDisposable()

    private var sortFlag:Boolean = false
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

        var uncompletedTasks = taskList.filter { !it.isCompleted }
        var completedTasks = taskList.filter { it.isCompleted }

        if (!sortFlag){ // sort by favorite
            var uncompletedTasksFavorite = uncompletedTasks.filter { it.isFavorite }
            var uncompletedTasksUnFavorite = uncompletedTasks.filter { !it.isFavorite }
            uncompletedTasks = uncompletedTasksFavorite + uncompletedTasksUnFavorite

            var completedTasksFavorite = completedTasks.filter { it.isFavorite }
            var completedTasksUnFavorite = completedTasks.filter { !it.isFavorite }
            completedTasks = completedTasksFavorite + completedTasksUnFavorite


        }else{//sort by date
//comparator that compares tasks by their due date. If the dueDate is null, it uses Date(Long.MAX_VALUE) to place those tasks at the end of the list.
            uncompletedTasks = uncompletedTasks.sortedWith(compareBy { it.dueDate ?: Date(Long.MAX_VALUE) })
            completedTasks = completedTasks.sortedWith(compareBy { it.dueDate ?: Date(Long.MAX_VALUE) })
        }

        binding.recyclerViewUnCompletedTask.layoutManager = LinearLayoutManager(requireContext())
        val un_completed_adapter = TaskAdapter(ArrayList(uncompletedTasks),db,taskDao)
        binding.recyclerViewUnCompletedTask.adapter =un_completed_adapter

        if (completedTasks.size>0){

            binding.completedTextCountLayout.visibility = View.VISIBLE
            binding.recyclerViewCompletedTask.visibility = View.VISIBLE


            binding.recyclerViewCompletedTask.layoutManager = LinearLayoutManager(requireContext())
            val completed_adapter = TaskAdapter(ArrayList(completedTasks),db,taskDao)
            binding.recyclerViewCompletedTask.adapter =completed_adapter

            binding.completedCountText.setText("Completed ("+completedTasks.size.toString()+")")


        }else{
            binding.completedTextCountLayout.visibility = View.GONE
            binding.recyclerViewCompletedTask.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTaskButton.setOnClickListener{
            addTask(it)
        }

        binding.completedTextCountLayout.setOnClickListener{
            changeCompletedTasksVisiblity(it)
        }

        binding.taskListFragmentSortButton.setOnClickListener{
            sortFlag = !sortFlag

            compositeDisposible.add(
                taskDao.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            )


        }
    }

    fun addTask(view: View){
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(requireActivity().supportFragmentManager,"BottomSheetDialog")

        val dneme = 1

    }

    fun changeCompletedTasksVisiblity(view : View){
        if(binding.recyclerViewCompletedTask.visibility == View.GONE){
            binding.recyclerViewCompletedTask.visibility = View.VISIBLE
        }else if (binding.recyclerViewCompletedTask.visibility == View.VISIBLE){
            binding.recyclerViewCompletedTask.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposible.clear()
        _binding = null
    }
}