package com.toandv.mytlu.service

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.app.JobIntentService
import com.toandv.mytlu.data.AppRepository
import kotlinx.coroutines.*
import javax.inject.Inject

@SuppressLint("Registered")
class UpdateService :  JobIntentService(){
    @Inject
    lateinit var appRepository: AppRepository

    private val job = Job()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
        //TODO read https://medium.com/androiddevelopers/exceptions-in-coroutines-ce8da1ec060c
    }

    private val corountineScope = CoroutineScope(Dispatchers.Main + job + exceptionHandler)

    override fun onCreate() {
        super.onCreate()
        TODO("inject dependencies")
    }

    override fun onHandleWork(intent: Intent) {
        corountineScope.launch { appRepository.refresh() }
    }
}