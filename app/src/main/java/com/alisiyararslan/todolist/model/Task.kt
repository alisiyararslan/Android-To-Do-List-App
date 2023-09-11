package com.alisiyararslan.todolist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date


@Entity
@Parcelize
class Task(
    var taskDescripion : String,
    var isFavorite: Boolean,

    var isCompleted : Boolean,

    var taskDetail:String?,

   // var dueDate: Date?,



) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}

