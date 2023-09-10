package com.alisiyararslan.todolist.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alisiyararslan.todolist.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getAll(): List<Task>

    @Insert
    fun insetAll(task: Task)

    @Delete
    fun delete(task: Task)
}