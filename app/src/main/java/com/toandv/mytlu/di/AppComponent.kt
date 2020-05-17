package com.toandv.mytlu.di

import android.app.Application
import android.content.Context
import com.toandv.mytlu.MainActivity
import com.toandv.mytlu.data.local.AppDatabase
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

//@Singleton
//@Component
//interface AppComponent {
//    fun inject(activity: MainActivity)
//
//
//}
//
//@Singleton
//@Module
//class DatabaseModule {
//    @Provides
//    fun provideAppDatabase(application: Application): AppDatabase = AppDatabase.build(application)
//
//
//}