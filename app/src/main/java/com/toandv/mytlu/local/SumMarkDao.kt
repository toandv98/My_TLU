package com.toandv.mytlu.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.toandv.mytlu.local.entity.SumMark

abstract class SumMarkDao {
    //endregion
    @Query("select * from sum_mark")
    abstract fun getAll(): LiveData<List<SumMark>>

    @Query("delete from sum_mark")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSumMarks(vararg sumMarks: SumMark)

    @Transaction
    suspend fun replaceSumMarks(vararg sumMarks: SumMark) {
        deleteAll()
        insertSumMarks(*sumMarks)
    }
}