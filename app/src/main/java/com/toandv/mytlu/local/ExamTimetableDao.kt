package com.toandv.mytlu.local

import androidx.room.*
import com.toandv.mytlu.local.entity.ExamTimetable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ExamTimetableDao {
    @Query("select * from exam_timetables order by datetime")
    abstract fun getAll(): Flow<List<ExamTimetable>>

    @Query("delete from exam_timetables")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg examTimetables: ExamTimetable)

    @Transaction
    open suspend fun replaceExamTimeTable(vararg examTimetables: ExamTimetable) {
        deleteAll()
        insert(*examTimetables)
    }
}