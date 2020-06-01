package com.toandv.mytlu.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_tuition")
data class Tuition(
    val label: String,
    val amount: String,
    val note: String = "",
    val info: Int = 0
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

data class TuitionInfo(
    val id: Int,
    val label: String,
    val amount: String,
    val info: Int
)