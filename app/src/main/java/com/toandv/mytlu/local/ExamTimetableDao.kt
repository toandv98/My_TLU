package com.toandv.mytlu.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.toandv.mytlu.local.entity.ExamTimetable
import kotlinx.coroutines.Deferred

abstract class ExamTimetableDao {
    @Query("select * from exam_timetables order by datetime")
    abstract fun getAll(): LiveData<List<ExamTimetable>>

    @Query("select * from exam_timetables order by datetime")
    abstract suspend fun getAllAsync(): Deferred<List<ExamTimetable>>

    @Query("delete from exam_timetables")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg examTimetables: ExamTimetable)

    @Transaction
    suspend fun replaceExamTimeTable(vararg examTimetables: ExamTimetable){
        deleteAll()
        insert(*examTimetables)
    }
}