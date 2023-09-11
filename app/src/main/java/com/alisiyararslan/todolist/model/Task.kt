package com.alisiyararslan.todolist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.alisiyararslan.todolist.roomdb.DateTypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.Date


@Entity
@Parcelize
class Task(
    var taskDescripion : String,
    var isFavorite: Boolean,

    var isCompleted : Boolean,

    var taskDetail:String?,

    @TypeConverters(DateTypeConverter::class)
    var dueDate: Date?,



) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}

