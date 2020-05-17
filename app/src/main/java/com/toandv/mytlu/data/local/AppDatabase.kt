package com.toandv.mytlu.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.toandv.mytlu.data.local.entity.*

private const val DB_NAME = "schedule_db"

@Database(
    entities = [Schedule::class, DetailMark::class, SumMark::class, Tuition::class, ExamTimetable::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun detailMarkDao(): DetailMarkDao

    abstract fun examTimetableDao(): ExamTimetableDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun studentTuitionDao(): StudentTuitionDao

    abstract fun sumMarkDao(): SumMarkDao

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