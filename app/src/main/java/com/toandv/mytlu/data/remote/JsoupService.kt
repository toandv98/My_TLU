package com.toandv.mytlu.data.remote

import org.jsoup.nodes.Document

interface JsoupService {

    val isLoggedIn: Boolean

    suspend fun login(username: String, password: String): Boolean

    fun logout()

    suspend fun getTuitionDoc(): Document

    suspend fun getMarkDoc(): Document

    suspend fun getPractiseDoc(): Document

    suspend fun getTimetableDoc(semester: String, term: String): Document

    suspend fun getExamTimetableDoc(semester: String, dot: String? = null): Document
}