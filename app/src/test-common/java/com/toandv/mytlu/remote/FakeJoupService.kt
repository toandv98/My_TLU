package com.toandv.mytlu.remote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Suppress("SpellCheckingInspection")
class FakeJoupService: JsoupService {
    override suspend fun getExamTimetableDoc(semester: String?, dot: String?): Document {
        return Jsoup.parse(
            javaClass.classLoader!!.getResource("StudentViewExamList.html").readText()
        )
    }

    override val isLoggedIn: Boolean
        get() = TODO("Not yet implemented")

    override suspend fun login(username: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun getTuitionDoc(): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentTuition.html").readText())
    }

    override suspend fun getTimetableDoc(semester: String?, term: String?): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentTimeTable.html").readText())
    }

    override suspend fun getMarkDoc(): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentMark.html").readText())
    }

    override suspend fun getPractiseDoc(): Document {
        return Jsoup.parse(
            javaClass.classLoader!!.getResource("PractiseMarkAndStudyWarning.html").readText()
        )
    }
}