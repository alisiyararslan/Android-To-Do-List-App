package com.alisiyararslan.todolist.view

import android.app.ActivityManager.TaskDescription
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.room.Room
import com.alisiyararslan.todolist.R
import com.alisiyararslan.todolist.databinding.FragmentTaskListBinding
import com.alisiyararslan.todolist.databinding.TaskLayoutBinding
import com.alisiyararslan.todolist.model.Task
import com.alisiyararslan.todolist.roomdb.TaskDao
import com.alisiyararslan.todolist.roomdb.TaskDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.android.synthetic.main.task_layout.*

class BottomSheetFragment: BottomSheetDialogFragment() {

//    private var _binding: BottomSheetDialogFragmentBinding? = null
//
//    private val binding get() = _binding!!

    private var compositeDisposible= CompositeDisposable()

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db= Room.databaseBuilder(requireContext(), TaskDatabase::class.java,"Tasks").build()
        taskDao=db.taskDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //_binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.task_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        saveNewTaskButton.setOnClickListener{



            try {
                var newTask = Task(newTaskText.toString(),false,false,"")

                compositeDisposible.add(
                    taskDao.insert(newTask)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )




            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun handleResponse(){
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        compositeDisposible.clear()

    }
}