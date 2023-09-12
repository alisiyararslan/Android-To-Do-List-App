package com.alisiyararslan.todolist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alisiyararslan.todolist.R
import com.alisiyararslan.todolist.databinding.RecyclerRowTaskBinding

import com.alisiyararslan.todolist.model.Task
import com.alisiyararslan.todolist.roomdb.TaskDao
import com.alisiyararslan.todolist.roomdb.TaskDatabase
import com.alisiyararslan.todolist.view.TaskDetailFragmentDirections
import com.alisiyararslan.todolist.view.TaskListFragmentDirections
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_task_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

public class TaskAdapter(var taskList: ArrayList<Task>,var db: TaskDatabase,var taskDao: TaskDao): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {


    class TaskHolder(val binding: RecyclerRowTaskBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = RecyclerRowTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        if(taskList.get(position).taskDescripion.length > 60){

            holder.binding.todoText.setText(taskList.get(position).taskDescripion.subSequence(0,60).toString() + "...")
        }else{
            holder.binding.todoText.setText(taskList.get(position).taskDescripion)
        }

        if (taskList.get(position).dueDate ==null){
            holder.binding.todoDateText.visibility = View.GONE
        }else{
            holder.binding.todoDateText.visibility = View.VISIBLE

            val myFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.UK)
            holder.binding.todoDateText.setText(sdf.format(taskList.get(position).dueDate))
        }

        holder.binding.todoChechBox.setChecked(taskList.get(position).isCompleted)




        holder.binding.todoChechBox.setOnClickListener{
            var compositeDisposible= CompositeDisposable()
            taskList.get(position).isCompleted = !taskList.get(position).isCompleted
            compositeDisposible.add(
                taskDao.update(taskList.get(position))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }

        holder.binding.taskDescriptionAndDateLayout.setOnClickListener{
            val action=TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(taskList.get(position))
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.recyclerRowFavoriteButton.setOnClickListener{
            var compositeDisposible= CompositeDisposable()
            taskList.get(position).isFavorite = !taskList.get(position).isFavorite
            compositeDisposible.add(
                taskDao.update(taskList.get(position))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }

        if (taskList.get(position).isFavorite){
            holder.binding.recyclerRowFavoriteButton.setImageResource(R.drawable.on)
        }else{
            holder.binding.recyclerRowFavoriteButton.setImageResource(R.drawable.off)
        }




    }



    public fun updateData(newData: kotlin.collections.ArrayList<Task>) {

        this.taskList = newData

        this.notifyDataSetChanged()
    }
}