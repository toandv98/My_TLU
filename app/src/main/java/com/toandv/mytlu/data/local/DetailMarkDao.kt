package com.toandv.mytlu.data.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.toandv.mytlu.data.local.entity.DetailMark

abstract class DetailMarkDao {
    //endregion
    @Query("select * from detail_mark")
    abstract fun getAll(): LiveData<List<DetailMark>>

    @Query("select * from detail_mark where dp = 1")
    abstract fun getDetailHKMarks(): LiveData<List<DetailMark>>

    @Query("delete from detail_mark")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg detailMarks: DetailMark)

    @Transaction
    suspend fun replaceDetailMarks(vararg detailMarks: DetailMark) {
        deleteAll()
        insert(*detailMarks)
    }
}