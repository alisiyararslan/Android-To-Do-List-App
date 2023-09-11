package com.alisiyararslan.todolist.view

import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.recycler_row_task.*
import kotlinx.android.synthetic.main.task_layout.*
import java.text.SimpleDateFormat
import java.util.*


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

            if (task.dueDate !=null){
                val myFormat = "dd-MM-yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.UK)
                displayTaskDetailDateText.setText(sdf.format(task.dueDate))
            }


            if (task.isCompleted){
                changeCompleteButton.setText("Make It Uncomplete")
            }else{
                changeCompleteButton.setText("Make It Complete")
            }

            displayFavoriteIcon()
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

        binding.taskDetailUpdateDateImage.setOnClickListener{
            updateDate(it)
        }

        binding.taskDetailFavoriteButton.setOnClickListener{
            updateFavorite(it)
        }
    }

    private fun displayFavoriteIcon() {
        if (task.isFavorite){
            taskDetailFavoriteButton.setImageResource(R.drawable.on)
        }else{
            taskDetailFavoriteButton.setImageResource(R.drawable.off)
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

    fun updateDate(view: View){
        val myCalendar  =  Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMont ->
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
        displayTaskDetailDateText.setText(sdf.format(myCalendar.time))
        task.dueDate = myCalendar.time
    }


    fun updateFavorite(view: View){
        task.isFavorite = !task.isFavorite
        displayFavoriteIcon()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposible.clear()
        _binding = null
    }


}