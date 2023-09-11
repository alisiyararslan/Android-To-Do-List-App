package com.alisiyararslan.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.Navigation
import androidx.room.Room
import com.alisiyararslan.todolist.R
import com.alisiyararslan.todolist.databinding.FragmentTaskDetailBinding
import com.alisiyararslan.todolist.databinding.FragmentTaskListBinding
import com.alisiyararslan.todolist.model.Task
import com.alisiyararslan.todolist.roomdb.TaskDao
import com.alisiyararslan.todolist.roomdb.TaskDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_task_detail.*


class TaskDetailFragment : Fragment() {


    private var _binding: FragmentTaskDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao

    private lateinit var task :Task

    private var compositeDisposible= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db= Room.databaseBuilder(requireContext(),TaskDatabase::class.java,"Tasks").build()
        taskDao=db.taskDao()


        // For the back button to work properly
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            val action=TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskListFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)


        val view = binding.root
        return view
        //return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            task=TaskDetailFragmentArgs.fromBundle(it).task
            editTextTaskDetail.setText(task.taskDetail)
            taskDescriptionTextView.setText(task.taskDescripion)

            if (task.isCompleted){
                changeCompleteButton.setText("Make It Uncomplete")
            }else{
                changeCompleteButton.setText("Make It Complete")
            }
        }

        binding.changeCompleteButton.setOnClickListener{
            changeComplete(it)
        }

        binding.updateTaskDetailButton.setOnClickListener{
            updateTask(it)
        }

        binding.deleteTaskDetailButton.setOnClickListener{
            deleteTask(it)
        }
    }

    fun changeComplete(view: View){

        var compositeDisposible= CompositeDisposable()
        task.isCompleted = !task.isCompleted
        compositeDisposible.add(
            taskDao.update(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun updateTask(view: View){
        var compositeDisposible= CompositeDisposable()
        task.taskDetail = binding.editTextTaskDetail.text.toString()
        task.taskDescripion = binding.taskDescriptionTextView.text.toString()

        compositeDisposible.add(
            taskDao.update(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun deleteTask(view: View){
        var compositeDisposible= CompositeDisposable()

        compositeDisposible.add(
            taskDao.delete(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    // Handle the response or any other necessary actions here
                    val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskListFragment()
                    Navigation.findNavController(view).navigate(action)
                }
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposible.clear()
        _binding = null
    }


}