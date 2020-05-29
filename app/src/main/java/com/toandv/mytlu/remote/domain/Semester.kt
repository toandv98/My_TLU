package com.toandv.mytlu.remote.domain

sealed class Semester(open val year: String, open val semester: String)

data class SummarySemester(
    override val year: String,
    override val semester: String,
    val cumulativeAverage: String,
    val average: String
) : Semester(year, semester)

data class PracticeSemester(
    override val year: String,
    override val semester: String,
    val practiceMark: String,
    val grade: String
) : Semester(year, semester)