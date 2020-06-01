package com.toandv.mytlu.repo

import android.content.res.Resources
import com.toandv.mytlu.local.AppDatabase
import com.toandv.mytlu.local.entity.SemesterType
import com.toandv.mytlu.remote.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class AppRepositoryImpl constructor(
    private val database: AppDatabase,
    private val jsoupService: JsoupService,
    private val resources: Resources,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppRepository {

    override suspend fun login(userName: String, password: String): Boolean {
        if (jsoupService.isLoggedIn) jsoupService.logout()
        return jsoupService.login(userName, password)
    }

    @ExperimentalCoroutinesApi
    override suspend fun refresh() {
        //TODO login lai o day hoac check login
        coroutineScope {
            launch(ioDispatcher) {
                // Default parameters Để lấy các học kỳ
                val examTimetableDocDefault = async { jsoupService.getExamTimetableDoc() }
                val markDoc = async { jsoupService.getMarkDoc() }
                val practiseDoc = async { jsoupService.getPractiseDoc() }
                val tuitionDoc = async { jsoupService.getTuitionDoc() }

                // Thêm vào bảng semester và 2 bảng phụ summary_semester, practise_semester
//                val semesterFlow = examTimetableDocDefault.await().parseSemesterFlow()
//                ensureActive()
//                flow<SemesterType> {
//                    markDoc.await().parseSummarySemesterFlow().let { emitAll(it) }
//                    practiseDoc.await().parsePractiseMarkFlow().let { emitAll(it) }
//                }.let { database.semesterDao().replaceSemesterFlow(semesterFlow, it) }
//
//                // Thêm vào bảng student_tuition
//                ensureActive()
//                database.studentTuitionDao()
//                    .replaceTuitionFlow(tuitionDoc.await().parseTuitionDataFlow(resources))
            }
            ensureActive()
        }
    }
}