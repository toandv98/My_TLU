package com.toandv.mytlu.remote

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.toandv.mytlu.MyTluApplication
import com.toandv.mytlu.local.entity.ExamTimetable
import com.toandv.mytlu.local.entity.Tuition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
        assertThat(mlist.size, equalTo(3))

        mlist.clear()
        // GIVEN
        val falseDoc = DummyDocument.getStudentTuition_aspx_html()

        // WHEN
        falseDoc.parseExamTableDataFlow().collect { mlist.add(it) }

        // THEN
        assertThat(mlist.size, equalTo(0))
    }

    @Test
    fun parseTuitionData() = runBlockingTest {
        val resources = ApplicationProvider.getApplicationContext<MyTluApplication>().resources
        requireNotNull(resources)

        val mlist = mutableListOf<Tuition>()

        // GIVEN
        val trueDoc = DummyDocument.getStudentTuition_aspx_html()
        trueDoc.parseTuitionData(resources).collect {
            mlist.add(it)
            println(it)
        }

        assertThat(mlist.size, greaterThan(0))

        mlist.clear()
        // GIVEN
        val falseDoc = DummyDocument.getStudentViewExamList_aspx_html()

        // WHEN
        falseDoc.parseTuitionData(resources).collect { mlist.add(it) }

        // THEN
        assertThat(mlist.size, equalTo(0))
    }
}