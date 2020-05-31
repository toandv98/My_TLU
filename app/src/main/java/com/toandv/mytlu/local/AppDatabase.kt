package com.toandv.mytlu.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.toandv.mytlu.local.entity.*

private const val DB_NAME = "schedule_db"

@Database(
    entities = [
        Schedule::class,
        Subject::class,
        Mark::class,
//        SumMark::class,
        Semester::class,
        SummarySemester::class,
        PractiseSemester::class,
        Tuition::class,
        ExamTimetable::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun detailMarkDao(): SubjectWithMarksDao

    abstract fun examTimetableDao(): ExamTimetableDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun studentTuitionDao(): StudentTuitionDao

    abstract fun sumMarkDao(): SemesterDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(application: Application): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = newInstance(application)
                    }
                }
            }
            return instance!!
        }

        private fun newInstance(application: Application): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME)
                .build()
    }
}