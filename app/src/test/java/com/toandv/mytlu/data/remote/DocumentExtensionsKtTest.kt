package com.toandv.mytlu.data.remote

import com.toandv.mytlu.data.local.entity.ExamTimetable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class DocumentExtensionsKtTest {
    private lateinit var jsoupService: JsoupService

    private lateinit var messageDigest: MessageDigest

    private val acc = "1651060845"
    private val pw = "Cong0336594779"

    // d504d5f66b63f694b51200516a6bde35
    private lateinit var md5pw: String

    @Before
    fun setup() {
        jsoupService = JsoupServiceImp(Dispatchers.Unconfined)
        messageDigest = MessageDigest.getInstance("MD5")
        md5pw = messageDigest.digest(pw.toByteArray(Charsets.UTF_8))
            .joinToString("") { String.format("%02x", it) }
        runBlockingTest {
            jsoupService.login(acc, md5pw)
        }
    }

    @After
    fun teardown() {
        jsoupService.logout()
    }

    @Test
    fun parseExampData_ky1_2019_2020_size3() = runBlockingTest {
        assertThat(jsoupService.isLoggedIn, equalTo(true))
        // GIVEN
        val ky1_2019_2020 = "bf298b4722c84138b4dea0f498e8bc59"
        val examTableDoc = jsoupService.getExamTimetableDoc(ky1_2019_2020)
        val actualSize = 3

        // WHEN
        val result = examTableDoc.parseExamTableDataFlow()
        val examTimeList = mutableListOf<ExamTimetable>()

        result.collect {
            examTimeList.add(it)
            println(it)
        }

        // THEN
        assertThat(examTimeList.size, `is`(actualSize))
    }

    @Test
    fun parseExamData_falseDocument_notCrash() = runBlockingTest {
        assertThat(jsoupService.isLoggedIn, equalTo(true))

        val examTimeList = mutableListOf<ExamTimetable>()
        // false Document
        jsoupService.getMarkDoc()
            .parseExamTableDataFlow()
            .collect{
                examTimeList.add(it)
            }

        // PASS1
        assertThat(examTimeList.size, equalTo(0))

        // logout
        jsoupService.logout()
        assertThat(jsoupService.isLoggedIn, equalTo(false))
        jsoupService.login(acc, "false password")
        examTimeList.clear()

        // GIVEN
        val ky1_2019_2020 = "bf298b4722c84138b4dea0f498e8bc59"
        // false document
        jsoupService.getExamTimetableDoc(ky1_2019_2020)
            .parseExamTableDataFlow()
            .collect{
                examTimeList.add(it)
            }

        // PASS2
        assertThat(examTimeList.size, equalTo(0))
    }

    @Test
    fun flowTest() = runBlockingTest {
        val flow = flowOf(1, 2, 3)

        val mlist = mutableListOf<Int>()
        flow.collect {
            mlist.add(it)
        }

        assertThat(mlist.size, `is`(3))
    }
}