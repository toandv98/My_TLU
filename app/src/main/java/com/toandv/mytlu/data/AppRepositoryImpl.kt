package com.toandv.mytlu.data

import com.toandv.mytlu.data.local.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppRepositoryImpl constructor(
    private val database: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppRepository