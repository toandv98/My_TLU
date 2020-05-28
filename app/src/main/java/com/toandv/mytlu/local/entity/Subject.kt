package com.toandv.mytlu.local.entity

import androidx.room.*

@Entity(tableName = "subject")
data class Subject(
    @PrimaryKey
    val maMon: String,
    val tenMon: String,
    val soTin: Int,
    var hocKiGanNhat: Boolean = false
)

@Entity(tableName = "mark", primaryKeys = ["maMon", "lanHoc"])
@ForeignKey(entity = Subject::class, parentColumns = ["maMon"], childColumns = ["maMon"])
data class Mark(
    val maMon: String,
    val lanHoc: Int,
    val quaTrinh: Float = 0f,
    val diemThi: Float = 0f,
    val tongKet: Float = 0f,
    val diemChu: Char = ' '
)

data class SubjectWithMarks(
    @Embedded val subject: Subject,
    @Relation(parentColumn = "maMon", entityColumn = "maMon")
    val marks: List<Mark>
)