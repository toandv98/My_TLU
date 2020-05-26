package com.toandv.mytlu.remote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Suppress("SpellCheckingInspection")
object DummyDocument {
    fun getStudentViewExamList_aspx_html(): Document {
        return Jsoup.parse(
            javaClass.classLoader!!.getResource("StudentViewExamList.html").readText()
        )
    }

    fun getStudentTuition_aspx_html(): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentTuition.html").readText())
    }

    fun getStudentTimeTable_aspx_html(): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentTimeTable.html").readText())
    }
}