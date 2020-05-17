package com.toandv.mytlu.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.toandv.mytlu.data.local.entity.Schedule
import kotlinx.coroutines.Deferred

@Dao
abstract class ScheduleDao {
    @Query("select * from schedule where status < 2 order by datetime")
    abstract fun getAll(): LiveData<List<Schedule>>

    @Query("select * from schedule where status < 2 order by datetime")
    abstract suspend fun getAllAsync(): Deferred<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg schedules: Schedule)

    @Query("delete from schedule")
    abstract suspend fun deleteAll()

    @Transaction
    suspend fun replaceSchedules(vararg schedules: Schedule){
        deleteAll()
        insert(*schedules)
    }
}