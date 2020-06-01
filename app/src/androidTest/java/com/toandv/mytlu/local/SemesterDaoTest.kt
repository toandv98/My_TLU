package com.toandv.mytlu.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.toandv.mytlu.local.entity.PractiseSemester
import com.toandv.mytlu.local.entity.Semester
import com.toandv.mytlu.local.entity.SummarySemester
import com.toandv.mytlu.remote.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SemesterDaoTest {
    private val semesters = listOf(
        Semester("2019_2020", "1", ""),
        Semester("2019_2020", "2", ""),
        Semester("2020_2021", "1", "")
    )
    private lateinit var semesterDao: SemesterDao
    private lateinit var db: AppDatabase
    private lateinit var jsoupService: JsoupService

    // see this link: https://www.youtube.com/watch?v=KMb0Fs8rCRs
//    private val testDispatcher = TestCoroutineDispatcher()
//    @get:Rule
//    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
//        Dispatchers.setMain(testDispatcher)

        jsoupService = FakeJoupService()
        val context = getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )//.allowMainThreadQueries()
            .build()
        semesterDao = db.semesterDao()
    }

    @After
    fun tearDown() {
        db.close()

//        Dispatchers.resetMain()
//        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    @SmallTest
    fun insertSemester() = runBlockingTest {
        // when
        semesterDao.insertSemester(semesters[0])
        semesterDao.insertSemester(semesters[1])
        semesterDao.insertSemester(semesters[2])

        // then
        val result = semesterDao.getAllSemesterInfo()
        assertThat(result.size, equalTo(3))

        val flow = semesterDao.getAllSemesterInfoFlow()
//        flow.take(1).flowOn(coroutineRule.coroutineContext).collect {
//            assertThat(it.size, equalTo(3))
//        }

        flow.take(1).collect {
            assertThat(it.size, equalTo(3))
        }
    }

    @Test
    @SmallTest
    fun insertSemester_vararg() = runBlockingTest {
        // when
        semesterDao.insertSemester(*semesters.toTypedArray())

        // then
        val result = semesterDao.getAllSemesterInfo()
        assertThat(result.size, equalTo(3))

        val flow = semesterDao.getAllSemesterInfoFlow()

        flow.take(1).collect {
            assertThat(it.size, equalTo(3))
        }
    }

    @Test
    @SmallTest
    fun deleteAllSemester() = runBlockingTest {
        // given
        semesterDao.insertSemester(*semesters.toTypedArray())

        semesterDao.getAllSemesterInfoFlow().take(1).collect {
            assertThat(it.size, equalTo(3))
        }
        // when
        semesterDao.deleteAllSemester()

        // then
        semesterDao.getAllSemesterInfo().size.let { assertThat(it, equalTo(0)) }
    }

    @Test
    @SmallTest
    fun replaceSemesterFlow() = runBlockingTest {
        // given
        semesterDao.insertSemester(*semesters.toTypedArray())

        semesterDao.getAllSemesterInfoFlow().first().let { assertThat(it.size, equalTo(3)) }

        val newSemesters = listOf(
            Semester("2009_2010", "1", ""),
            Semester("2009_2010", "2", ""),
            Semester("2010_2011", "1", "")
        )
        // when
        semesterDao.replaceSemesterFlow(newSemesters.toTypedArray(), emptyArray(), emptyArray())

        // then
        semesterDao.getAllSemesterInfoFlow().first().map { it.semester }
            .let { assertThat(it, equalTo(newSemesters)) }
    }

    @Test
    @SmallTest
    fun insertSummarySemester() = runBlockingTest {
        // given
        semesterDao.insertSemester(*semesters.toTypedArray())
        semesterDao.getAllSemesterInfoFlow().first().size.let { assertThat(it, equalTo(3)) }
        val summarySemesters = listOf(
            SummarySemester("2019_2020", "1", "5.0", "5.0"),
            SummarySemester("2019_2020", "2", "7.0", "7.0"),
            SummarySemester("2020_2021", "1", "9.0", "9.0")
        )

        // when
        semesterDao.insertSummarySemester(*summarySemesters.toTypedArray())

        // then
        semesterDao.getAllSummarySemesterFlow().first().let { assertThat(it, equalTo(summarySemesters)) }
    }
    @Test
    @SmallTest
    fun insertPractiseSemester() = runBlockingTest {
        // given
        semesterDao.insertSemester(*semesters.toTypedArray())
        semesterDao.getAllSemesterInfoFlow().first().size.let { assertThat(it, equalTo(3)) }
        val practiseSemesters = listOf(
            PractiseSemester("2019_2020", "1", "5.0", "C"),
            PractiseSemester("2019_2020", "2", "7.0", "B"),
            PractiseSemester("2020_2021", "1", "9.0", "A")
        )

        // when
        semesterDao.insertPractiseSemester(*practiseSemesters.toTypedArray())

        // then
        semesterDao.getAllPractiseSemesterFlow().first().let { assertThat(it, equalTo(practiseSemesters)) }
    }

// TODO Cứuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu
    @Test
    fun replaceSemesterFlow_semesterFlow_semesterTypeFlow_notCrash() = runBlockingTest {
        // given
        val examTimetableDocDefault = jsoupService.getTimetableDoc()
        val markDoc = jsoupService.getMarkDoc()
        val practiseDoc = jsoupService.getPractiseDoc()

        val semesterFlow = examTimetableDocDefault.parseSemesterFlow().toList()
        val semesterCount = semesterFlow.size
        val summarySemesterFlow = markDoc.parseSummarySemesterFlow().toList()
        val summaryCount = summarySemesterFlow.size
        val practiseSemesterFlow = practiseDoc.parsePractiseMarkFlow().toList()
        val practiseCount = practiseSemesterFlow.size

        // when
//            flow<SemesterType> {
//                emitAll(summarySemesterFlow)
//                emitAll(practiseSemesterFlow)
//            }.also { semesterDao.replaceSemesterFlow(semesterFlow, it) }
        semesterDao.replaceSemesterFlow(
            semesterFlow.toTypedArray(),
            summarySemesterFlow.toTypedArray(),
            practiseSemesterFlow.toTypedArray()
        )

        // then
        val allSemesterInfo = semesterDao.getAllSemesterInfo()
        assertThat(allSemesterInfo.size, equalTo(semesterCount))

        semesterDao.getAllSummarySemesterFlow().first().let { assertThat(it.size, equalTo(summaryCount)) }

        semesterDao.getAllPractiseSemesterFlow().first().let { assertThat(it.size, equalTo(practiseCount)) }

        val semesterInfo = semesterDao.getAllSemesterInfo()
        println(semesterInfo)
    }
}