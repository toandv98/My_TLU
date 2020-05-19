package com.toandv.mytlu.data.remote

import org.jsoup.nodes.Document
import java.io.IOException

interface JsoupService {
    @get:Throws(IOException::class)
    val tuitionDoc: Document?

    @get:Throws(IOException::class)
    val markDoc: Document?

    @get:Throws(IOException::class)
    val practiseDoc: Document?

    @Throws(IOException::class)
    fun getTimetableDoc(
        semester: String,
        term: String
    ): Document?

    @Throws(IOException::class)
    fun getExamTimetableDoc(
        semester: String,
        dot: String
    ): Document?
}