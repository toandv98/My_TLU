package com.toandv.mytlu.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.toandv.mytlu.local.entity.Mark
import com.toandv.mytlu.local.entity.Subject
import com.toandv.mytlu.local.entity.SubjectWithMarks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class SubjectWithMarksDao {

    // See this link to know why need @Transaction annotation
    // https://developer.android.com/training/data-storage/room/relationships#one-to-many
    @Transaction
    @Query("select * from subject")
    abstract fun getAll(): Flow<List<SubjectWithMarks>>

    @Transaction
    @Query("select * from subject where hocKiGanNhat = 1")
    abstract fun getDetailHKMarks(): Flow<List<SubjectWithMarks>>

    @Query("delete from subject")
    protected abstract suspend fun deleteAllSubject()

    @Query("delete from mark")
    protected abstract suspend fun deleteAllMarks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertSubject(vararg subject: Subject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertMark(vararg mark: Mark)

    @Transaction
    suspend fun replaceSubjectWithMarksFlow(flow: Flow<SubjectWithMarks>) {
        deleteAllSubject()
        deleteAllMarks()
        flow.collect {
            insertSubject(it.subject)
            insertMark(*it.marks.toTypedArray())
        }
    }
}