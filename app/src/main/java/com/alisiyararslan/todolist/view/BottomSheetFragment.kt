package com.alisiyararslan.todolist.view

import android.app.ActivityManager.TaskDescription
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.alisiyararslan.todolist.R
import com.alisiyararslan.todolist.adapter.TaskAdapter
import com.alisiyararslan.todolist.databinding.FragmentTaskListBinding
import com.alisiyararslan.todolist.databinding.TaskLayoutBinding
import com.alisiyararslan.todolist.model.Task
import com.alisiyararslan.todolist.roomdb.DateTypeConverter
import com.alisiyararslan.todolist.roomdb.TaskDao
import com.alisiyararslan.todolist.roomdb.TaskDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.android.synthetic.main.task_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: TaskLayoutBinding? = null

    private val binding get() = _binding!!

    private var _binding2: FragmentTaskListBinding? = null

    private val binding2 get() = _binding2!!

    private var compositeDisposible= CompositeDisposable()

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao

    private lateinit var taskAdapter: TaskAdapter

    private  var taskList: ArrayList<Task>  = ArrayList()
    private var date : Date? = null

    private var isFavorite = false

    private  var newTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db= Room.databaseBuilder(requireContext(), TaskDatabase::class.java,"Tasks").build()
        taskDao=db.taskDao()

        compositeDisposible.add(
            taskDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

        taskAdapter= TaskAdapter(taskList,db,taskDao)
    }

    fun handleResponse(_taskList:List<Task>){


        taskList = ArrayList(_taskList)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = TaskLayoutBinding.inflate(inflater, container, false)
        _binding2 = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newTaskFavoriteButton.setOnClickListener{
            isFavorite = !isFavorite

            if (isFavorite){
                newTaskFavoriteButton.setImageResource(R.drawable.on)
            }else{
                newTaskFavoriteButton.setImageResource(R.drawable.off)
            }
        }

        saveNewTaskButton.setOnClickListener{

            try {
                newTask = Task(binding.newTaskText.text.toString(),isFavorite,false,"",date)

                compositeDisposible.add(
                    taskDao.insert(newTask!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse2)
                )

            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        newTaskDate.setOnClickListener{
            datePick(it)
        }
    }

    fun datePick(view: View){

        val myCalendar  =  Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMont ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMont)
            updateLable(myCalendar)
        }

        DatePickerDialog(requireActivity(),datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun updateLable(myCalendar : Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateDisplayTextView.setText(sdf.format(myCalendar.time))
        date = myCalendar.time
    }

    fun handleResponse2(){

        taskList += newTask!!

        taskAdapter.updateData(taskList)
        dismiss()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposible.clear()

    }
}