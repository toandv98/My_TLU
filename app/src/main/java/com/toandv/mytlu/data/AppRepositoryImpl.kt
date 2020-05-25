package com.toandv.mytlu.data

import com.toandv.mytlu.data.local.AppDatabase
import com.toandv.mytlu.data.local.entity.ExamTimetable
import com.toandv.mytlu.data.remote.JsoupService
import kotlinx.coroutines.*
import org.jsoup.nodes.Document

class AppRepositoryImpl constructor(
    private val database: AppDatabase,
    private val jsoupService: JsoupService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppRepository {

    override suspend fun login(userName: String, password: String): Boolean {
        if (jsoupService.isLoggedIn) jsoupService.logout()
        return jsoupService.login(userName, password)
    }

    override suspend fun refresh() {
        //TODO login lai o day hoac check login
        coroutineScope {
            launch(ioDispatcher) {
                val doc = jsoupService.getMarkDoc()
                //TODO implement this @Toandv
                ensureActive()
                database.sumMarkDao().insertSumMarks()
            }
            ensureActive()
            launch(ioDispatcher) {
                val doc = jsoupService.getPractiseDoc()
                //TODO implement this @Toandv
                ensureActive()
                database.detailMarkDao()
            }
        }
    }

    private suspend fun examTimeTablesData(document: Document): Deferred<List<ExamTimetable>> = withContext(ioDispatcher){
        return@withContext async {
            val examTimetables = mutableListOf<ExamTimetable>()

            examTimetables
        }
    }
}