package com.toandv.mytlu.local.entity

import androidx.room.*

@Entity(
    tableName = "semester",
    indices = [Index(value = ["year", "semester"])],
    primaryKeys = ["year", "semester"]
)
data class Semester(val year: String, val semester: String, var code: String)

sealed class SemesterType

@Entity(
    tableName = "summary_semester",
    indices = [Index(value = ["year", "semester"])],
    primaryKeys = ["year", "semester"]//,
//    foreignKeys = [ForeignKey(
//        entity = Semester::class,
//        parentColumns = ["year", "semester"],
//        childColumns = ["year", "semester"],
//        onDelete = ForeignKey.CASCADE,
//        onUpdate = ForeignKey.CASCADE
//    )]
)
data class SummarySemester(
    val year: String,
    val semester: String,
    val cumulativeAverage: String,
    val average: String
):SemesterType()

@Entity(
    tableName = "practise_semester",
    indices = [Index(value = ["year", "semester"])],
    primaryKeys = ["year", "semester"]//,
//    foreignKeys = [ForeignKey(
//        entity = Semester::class,
//        parentColumns = ["year", "semester"],
//        childColumns = ["year", "semester"],
//        onUpdate = ForeignKey.CASCADE,
//        onDelete = ForeignKey.CASCADE
//    )]
)
data class PractiseSemester(
    val year: String,
    val semester: String,
    val practiseMark: String,
    val grade: String
): SemesterType()

data class SemesterInfo(
    @Embedded
    val semester: Semester,
    @Relation(parentColumn = "year", entityColumn = "year")
    val summarySemesters: List<SummarySemester>,
    @Relation(parentColumn = "year", entityColumn = "year")
    val practiseSemester: List<PractiseSemester>
)