package com.toandv.mytlu.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.toandv.mytlu.local.entity.Subject

abstract class SubjectDao {
    //endregion
    @Query("select * from subject")
    abstract fun getAll(): LiveData<List<Subject>>

    @Query("select * from subject where hocKiGanNhat = 1")
    abstract fun getDetailHKMarks(): LiveData<List<Subject>>

    @Query("delete from subject")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg detailMarks: Subject)

    @Transaction
    suspend fun replaceDetailMarks(vararg detailMarks: Subject) {
        deleteAll()
        insert(*detailMarks)
    }
}