package com.alisiyararslan.todolist.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alisiyararslan.todolist.model.Task

@Database(entities = [Task::class],version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}