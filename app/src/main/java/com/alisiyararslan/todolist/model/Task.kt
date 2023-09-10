package com.alisiyararslan.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
class Task(
    var taskDescripion : String,

    var taskDetail:String,

    var dueDate: Date,

    var isFavorite: Boolean,

    var isCompleted : Boolean

) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}

