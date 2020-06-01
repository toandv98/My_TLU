package com.toandv.mytlu.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.toandv.mytlu.local.entity.Tuition
import com.toandv.mytlu.local.entity.TuitionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@Dao
abstract class StudentTuitionDao {
    @Query("select id, label, amount, info from student_tuition where info < 2")
    abstract fun getTuitionInfo(): LiveData<List<TuitionInfo>>

    @Query("select * from student_tuition where info > 1")
    abstract fun getPaidTuition(): LiveData<List<Tuition>>

    @Query("delete from student_tuition")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg tuitionArr: Tuition)

    @Transaction
    open suspend fun replaceTuitionFlow(tuitionFlow: Flow<Tuition>) {
        deleteAll()
        tuitionFlow.collect { insert(it) }
    }
}