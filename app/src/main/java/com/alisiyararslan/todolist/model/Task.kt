package com.alisiyararslan.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
class Task(
    var taskDescripion : String,
    var isFavorite: Boolean,

    var isCompleted : Boolean,

    var taskDetail:String?,

   // var dueDate: Date?,



) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}

