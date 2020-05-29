@file:Suppress("ClassName")

package com.toandv.mytlu.remote

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.toandv.mytlu.MyTluApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.security.MessageDigest

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DocumentExtensions_JsoupServiceTest {
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
    fun parseExampDataFlow_ky1_2019_2020_size3() = runBlockingTest {
        // GIVEN
        val ky1_2019_2020 = "bf298b4722c84138b4dea0f498e8bc59"
        val examTableDoc = jsoupService.getExamTimetableDoc(ky1_2019_2020)
        val actualSize = 3

        // WHEN
        val count = examTableDoc.parseExamTableDataFlow().count()

        // THEN
        assertThat(count, `is`(actualSize))
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseExamData_falseDocument_notCrash() = runBlockingTest {
        // false Document
        jsoupService.getMarkDoc().parseExamTableDataFlow().first()
    }

    @Test
    fun parseTuitionDataFlow_trueDocument_countGreaterOrEqual14() = runBlockingTest {
        // given
        val resources = ApplicationProvider.getApplicationContext<MyTluApplication>().resources
        requireNotNull(resources)
        val trueDoc = jsoupService.getTuitionDoc()

        // when
        val count = trueDoc.parseTuitionDataFlow(resources).count()

        // then
        assertThat(count, greaterThanOrEqualTo(14))
    }

    @Test
    fun parseScheduleDataFlow_trueDoc_countGreaterThanOrEqualTo10() = runBlockingTest {
        // given
        val ky2_2019_2020 = "5ad062230c5c4973a1bd241ed876d6fb"

        // when
        val trueDoc = jsoupService.getTimetableDoc(ky2_2019_2020)
        val count = trueDoc.parseScheduleDataFlow().count()

        // then
        assertThat(count, greaterThanOrEqualTo(10))

        // Pass2
        jsoupService.getTimetableDoc().parseScheduleDataFlow().count()
            .let { assertThat(it, greaterThanOrEqualTo(0)) }
    }

    @Test
    fun parseSubjectWithMarksFlow_trueDoc_countGreaterThanOrEqualTo53() = runBlockingTest {
        // when
        val trueDoc = jsoupService.getMarkDoc()
        val count = trueDoc.parseSubjectWithMarksFlow().count()

        // then
        assertThat(count, greaterThanOrEqualTo(53))
    }

    @Test
    fun parseSummarySemesterFlow_trueDoc_countGreaterThanOrEqualTo13() = runBlockingTest {
        // when
        val trueDoc = jsoupService.getMarkDoc()
        val count = trueDoc.parseSummarySemesterFlow().count()

        // then
        assertThat(count, greaterThanOrEqualTo(13))
    }

    @Test
    fun parsePractiseMarkFlow_trueDoc_countGreaterThanOrEqualTo10() = runBlockingTest {
        // when
        val trueDoc = jsoupService.getPractiseDoc()
        val count = trueDoc.parsePractiseMarkFlow().count{
            println(it)
            true
        }

        // then
        assertThat(count, greaterThanOrEqualTo(10))
    }
}