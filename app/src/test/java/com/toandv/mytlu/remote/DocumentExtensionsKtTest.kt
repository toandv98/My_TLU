package com.toandv.mytlu.remote

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.toandv.mytlu.MyTluApplication
import com.toandv.mytlu.local.entity.ExamTimetable
import com.toandv.mytlu.local.entity.Schedule
import com.toandv.mytlu.local.entity.SubjectWithMarks
import com.toandv.mytlu.local.entity.Tuition
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
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
    fun parseExamTableDataFlow_trueDoc_falseDoc_notCrash() = runBlockingTest {
        val mlist = mutableListOf<ExamTimetable>()
        // GIVEN
        val trueDoc = DummyDocument.getStudentViewExamList_aspx_html()

        // WHEN
        trueDoc.parseExamTableDataFlow().collect {
            mlist.add(it)
            println(it)
        }

        // THEN
        assertThat(
            mlist.size,
            equalTo(3)
        ).also { println("assert passed with List<ExamTimetable>.size = ${mlist.size}") }

        mlist.clear()
        // GIVEN
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        falseDoc.parseExamTableDataFlow().collect { mlist.add(it) }

        // THEN
        assertThat(mlist.size, equalTo(0))
    }

    @Test
    fun parseTuitionData_trueDoc_falseDoc_notCrash() = runBlockingTest {
        val resources = ApplicationProvider.getApplicationContext<MyTluApplication>().resources
        requireNotNull(resources)

        val mlist = mutableListOf<Tuition>()

        // GIVEN
        val trueDoc = DummyDocument.getStudentTuition_aspx_html()
        trueDoc.parseTuitionDataFlow(resources).collect {
            mlist.add(it)
            println(it)
        }

        assertThat(
            mlist.size,
            greaterThan(0)
        ).also { println("assert passed with List<Tuition>.size = ${mlist.size}") }

        mlist.clear()
        // GIVEN
        val falseDoc = DummyDocument.getStudentViewExamList_aspx_html()

        // WHEN
        falseDoc.parseTuitionDataFlow(resources).collect { mlist.add(it) }

        // THEN
        assertThat(mlist.size, equalTo(0))
    }

    @Test
    fun parseScheduleDataFlow_trueDoc_falseDoc_notCrash() = runBlockingTest {
        val mlist = mutableListOf<Schedule>()

        // GIVEN
        val trueDoc = DummyDocument.getStudentTimeTable_aspx_html()

        // WHEN
        trueDoc.parseScheduleDataFlow().collect {
            mlist.add(it)
        }

        // THEN
        assertThat(
            mlist.size,
            greaterThan(0)
        ).also { println("assert passed with List<Schedule>.size = ${mlist.size}") }

        mlist.clear()
        // GIVEN
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        falseDoc.parseScheduleDataFlow().collect {
            mlist.add(it)
        }

        // THEN
        assertThat(mlist.size, equalTo(0))
    }

    @Test
    fun parseSubjectWithMarksFlow_trueDoc_falseDoc_notCrash() = runBlockingTest {
        val mlist = mutableListOf<SubjectWithMarks>()

        // GIVEN
        val trueDoc = DummyDocument.getStudentMark_aspx_html()

        // WHEN

        trueDoc.parseSubjectWithMarksFlow().collect {
            mlist.add(it)
            println(it)
        }

        // THEN
        assertThat(
            mlist.size,
            greaterThan(0)
        ).also { println("assert passed with List<SubjectWithMarks>.size = ${mlist.size}") }

        mlist.clear()
        // GIVEN
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        falseDoc.parseSubjectWithMarksFlow().collect { mlist.add(it) }

        // THEN
        assertThat(mlist.size, equalTo(0))
    }
}