package com.toandv.mytlu.local

import androidx.room.*
import com.toandv.mytlu.local.entity.PractiseSemester
import com.toandv.mytlu.local.entity.Semester
import com.toandv.mytlu.local.entity.SemesterInfo
import com.toandv.mytlu.local.entity.SummarySemester
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SemesterDao {
    //endregion
    @Query("select * from summary_semester")
    abstract fun getAllSummarySemesterFlow(): Flow<List<SummarySemester>>

    @Query("select * from practise_semester")
    abstract fun getAllPractiseSemesterFlow(): Flow<List<PractiseSemester>>

    @Transaction
    @Query("select * from semester")
    abstract fun getAllSemesterInfoFlow(): Flow<List<SemesterInfo>>

    @Transaction
    @Query("select * from semester inner join summary_semester on semester.year == summary_semester.year and semester.semester == summary_semester.semester inner join practise_semester on semester.year == practise_semester.year and semester.semester == practise_semester.semester")
    abstract suspend fun getAllSemesterInfo(): List<SemesterInfo>

    suspend fun replaceSemesterFlow(
        semester: Array<Semester>,
        summarySemester: Array<SummarySemester>,
        practiseSemester: Array<PractiseSemester>
    ) {
        // Cascaded (xóa bản ghi ở bảng chính thì xóa luôn ở bảng con)
        deleteAllSemester()
        insertSemester(*semester)
        insertSummarySemester(*summarySemester)
        insertPractiseSemester(*practiseSemester)
    }

    @Query("delete from semester")
    abstract suspend fun deleteAllSemester()

    @Insert(onConflict = OnConflictStrategy.ABORT, entity = Semester::class)
    abstract suspend fun insertSemester(vararg semester: Semester)

    @Query("delete from summary_semester")
    protected abstract suspend fun deleteAllSummarySemester()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSummarySemester(vararg summary: SummarySemester)

    @Query("delete from practise_semester")
    protected abstract suspend fun deleteAllPractiseSemester()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPractiseSemester(vararg practiseSemester: PractiseSemester)
}