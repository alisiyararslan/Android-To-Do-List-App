package com.alisiyararslan.todolist.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alisiyararslan.todolist.model.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getAll(): Flowable <List<Task>>

    @Insert
    fun insert(task: Task) : Completable

    @Delete
    fun delete(task: Task) : Completable
}