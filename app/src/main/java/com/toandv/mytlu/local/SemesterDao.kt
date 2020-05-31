package com.toandv.mytlu.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.toandv.mytlu.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class SemesterDao {
    //endregion
    @Query("select * from summary_semester")
    abstract fun getAllSummarySemester(): Flow<List<SummarySemester>>

    @Query("select * from practise_semester")
    abstract fun getAllPractiseSemester(): Flow<List<PractiseSemester>>

    @Transaction
    @Query("select * from semester")
    abstract fun getAllSemesterInfo(): Flow<List<SemesterInfo>>

    @Transaction
    suspend fun replaceSemesterFlow(semester:Flow<Semester>, semesterType: Flow<SemesterType>) {
        // Cascaded (xóa bản ghi ở bảng chính thì xóa luôn ở bảng con)
        deleteAllSemester()
        semester.collect {
            insertSemester(it)
        }
        semesterType.collect {
            when(it){
                is SummarySemester -> insertSummarySemester(it)
                is PractiseSemester -> insertPractiseSemester(it)
            }
        }
    }

    @Query("delete from semester")
    protected abstract suspend fun deleteAllSemester()

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = Semester::class)
    protected abstract suspend fun insertSemester(vararg semester: Semester)

    @Query("delete from summary_semester")
    protected abstract suspend fun deleteAllSummarySemester()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertSummarySemester(vararg summary: SummarySemester)

    @Query("delete from practise_semester")
    protected abstract suspend fun deleteAllPractiseSemester()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertPractiseSemester(vararg practiseSemester: PractiseSemester)
}