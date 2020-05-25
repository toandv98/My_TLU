package com.toandv.mytlu.remote

import org.jsoup.nodes.Document

interface JsoupService {

    val isLoggedIn: Boolean

    suspend fun login(username: String, password: String): Boolean

    fun logout()

    suspend fun getTuitionDoc(): Document

    suspend fun getMarkDoc(): Document

    suspend fun getPractiseDoc(): Document

    suspend fun getTimetableDoc(semester: String, term: String? = null): Document

    suspend fun getExamTimetableDoc(semester: String, dot: String? = null): Document
}