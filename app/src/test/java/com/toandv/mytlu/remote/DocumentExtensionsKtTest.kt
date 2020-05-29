package com.toandv.mytlu.remote

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.toandv.mytlu.MyTluApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class DocumentExtensionsKtTest {

    @Test
    fun parseExamTableDataFlow_trueDoc() = runBlockingTest {
        // GIVEN
        val trueDoc = DummyDocument.getStudentViewExamList_aspx_html()

        // WHEN
        val count = trueDoc.parseExamTableDataFlow().count {
            println(it)
            true
        }

        // THEN
        assertThat(count, equalTo(3))
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseExamTableDataFlow_falseDoc_throwException() = runBlockingTest {
        // GIVEN
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        falseDoc.parseExamTableDataFlow().first()
    }

    @Test
    fun parseTuitionData_trueDoc() = runBlockingTest {
        val resources = ApplicationProvider.getApplicationContext<MyTluApplication>().resources
        requireNotNull(resources)
        // GIVEN
        val trueDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        val count = trueDoc.parseTuitionDataFlow(resources).count {
            println(it)
            true
        }

        assertThat(count, greaterThan(0))
            .also { println("assert passed with flow count = $count") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseTuitionData_falseDoc_throwException() = runBlockingTest {
        val resources = ApplicationProvider.getApplicationContext<MyTluApplication>().resources
        requireNotNull(resources)
        // GIVEN
        val falseDoc = DummyDocument.getStudentViewExamList_aspx_html()

        // WHEN
        falseDoc.parseTuitionDataFlow(resources).first()
    }

    @Test
    fun parseScheduleDataFlow_trueDoc() = runBlockingTest {
        // GIVEN
        val trueDoc = DummyDocument.getStudentTimeTable_aspx_html()

        // WHEN
        val count = trueDoc.parseScheduleDataFlow().count {
            println(it)
            true
        }

        // THEN
        assertThat(count, greaterThan(0))
            .also { println("assert passed with flow count = $count") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseScheduleDataFlow_falseDoc_throwException() = runBlockingTest {
        // GIVEN
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        falseDoc.parseScheduleDataFlow().first()
    }

    @Test
    fun parseSubjectWithMarksFlow_trueDoc() = runBlockingTest {
        // GIVEN
        val trueDoc = DummyDocument.getStudentMark_aspx_html()

        // WHEN
        val count = trueDoc.parseSubjectWithMarksFlow().count {
            println(it)
            true
        }

        // THEN
        assertThat(count, greaterThan(0))
            .also { println("assert passed with flow count = $count") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseSubjectWithMarksFlow_falseDoc_throwException() = runBlockingTest {
        // GIVEN
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        falseDoc.parseSubjectWithMarksFlow().first()
    }

    @Test
    fun parseSummarySemesterFlow_trueDoc() = runBlockingTest {
        // given
        val trueDoc = DummyDocument.getStudentMark_aspx_html()

        // when
        val count = trueDoc.parseSummarySemesterFlow().count { println(it)
        true}

        // then
        assertThat(count, equalTo(13))
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseSummarySemesterFlow_falseDoc_throwException() = runBlockingTest {
        // given
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // when
        falseDoc.parseSummarySemesterFlow().first()
    }

    @Test
    fun parsePractiseMarkFlow_trueDoc() = runBlockingTest {
        // given
        val trueDoc = DummyDocument.getPractiseMarkAndStudyWarning_aspx_html()

        // when
        val count = trueDoc.parsePractiseMarkFlow().count {
            println(it)
            true
        }

        // then
        assertThat(count, equalTo(10))
    }

    @Test(expected = IllegalArgumentException::class)
    fun parsePractiseMarkFlow_falseDoc_throwException() = runBlockingTest {
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // when
        falseDoc.parsePractiseMarkFlow().first()
    }
}