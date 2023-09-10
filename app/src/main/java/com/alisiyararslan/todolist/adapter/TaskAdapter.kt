package com.alisiyararslan.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alisiyararslan.todolist.databinding.RecyclerRowTaskBinding

import com.alisiyararslan.todolist.model.Task

class TaskAdapter(val taskList: List<Task>): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {
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
        holder.binding.todoChechBox.setText(taskList.get(position).taskDescripion.toString())
        holder.binding.todoChechBox.setChecked(taskList.get(position).isCompleted)


    }
}