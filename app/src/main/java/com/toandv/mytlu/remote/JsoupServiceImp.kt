package com.toandv.mytlu.remote

import com.toandv.mytlu.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
class JsoupServiceImp(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    JsoupService {

    private var cookies: Map<String, String>? = null

    override val isLoggedIn: Boolean
        get() = cookies != null

    override suspend fun login(username: String, password: String): Boolean =
        withContext(ioDispatcher) {
            val form = Jsoup.connect(URL_BASE + URL_LOGIN).timeout(TIME_OUT).get()

            val data = HashMap<String, String>()
            data[KEY_VIEW_STATE] = form.getElementById(KEY_VIEW_STATE).`val`()
            data[KEY_VIEW_STATE_GENERATOR] = form.getElementById(KEY_VIEW_STATE_GENERATOR).`val`()
            data[KEY_EVENT_VALIDATION] = form.getElementById(KEY_EVENT_VALIDATION).`val`()
            data[KEY_USER_NAME] = username
            data[KEY_PASSWORD] = password
            data[KEY_SUBMIT] = "login"

            val response = Jsoup.connect(URL_BASE + URL_LOGIN)
                .data(data).method(Connection.Method.POST).timeout(TIME_OUT).execute()
            cookies = response.cookies()

            return@withContext response.parse().getElementById(LBL_ERROR_INFO) == null
        }

    override fun logout() {
        cookies = null
    }

    override suspend fun getTuitionDoc(): Document {
        return getDoc(URL_BASE + URL_TUITION)
    }

    override suspend fun getMarkDoc(): Document {
        return getDoc(URL_BASE + URL_MARK)
    }

    override suspend fun getPractiseDoc(): Document {
        return getDoc(URL_BASE + URL_PRACTISE)
    }

    override suspend fun getTimetableDoc(semester: String?, term: String?): Document {
        var timetableDoc = getDoc(URL_BASE + URL_TIMETABLE)

        val data = HashMap<String, String>()
        data[KEY_VIEW_STATE] = timetableDoc.getElementById(KEY_VIEW_STATE).`val`()
        data[KEY_VIEW_STATE_GENERATOR] =
            timetableDoc.getElementById(KEY_VIEW_STATE_GENERATOR).`val`()
        data[KEY_EVENT_VALIDATION] = timetableDoc.getElementById(KEY_EVENT_VALIDATION).`val`()
        data[HID_TUITION_FACTOR_MODE] = timetableDoc.getElementById(HID_TUITION_FACTOR_MODE).`val`()
        data[HID_LOAI_UU_TIEN_HE_SO_HOC_PHI] =
            timetableDoc.getElementById(HID_LOAI_UU_TIEN_HE_SO_HOC_PHI).`val`()
        data[HID_STUDENT_ID] = timetableDoc.getElementById(HID_STUDENT_ID).`val`()
        data[DRP_SEMESTER] = semester ?: ""

        timetableDoc = postDoc(URL_BASE + URL_TIMETABLE, data)
        if (term.isNullOrEmpty()) return timetableDoc

        data[KEY_VIEW_STATE] = timetableDoc.getElementById(KEY_VIEW_STATE).`val`()
        data[KEY_VIEW_STATE_GENERATOR] =
            timetableDoc.getElementById(KEY_VIEW_STATE_GENERATOR).`val`()
        data[KEY_EVENT_VALIDATION] = timetableDoc.getElementById(KEY_EVENT_VALIDATION).`val`()
        data[DRP_TERM] = term

        return postDoc(URL_BASE + URL_TIMETABLE, data)
    }

    override suspend fun getExamTimetableDoc(semester: String?, dot: String?): Document {
        var examDoc = getDoc(URL_BASE + URL_EXAM_TIMETABLE)

        val data = HashMap<String, String>()
        data[KEY_VIEW_STATE] = examDoc.getElementById(KEY_VIEW_STATE).`val`()
        data[KEY_VIEW_STATE_GENERATOR] = examDoc.getElementById(KEY_VIEW_STATE_GENERATOR).`val`()
        data[KEY_EVENT_VALIDATION] = examDoc.getElementById(KEY_EVENT_VALIDATION).`val`()
        data[HID_SHOW_SHIFT_END_TIME] = examDoc.getElementById(HID_SHOW_SHIFT_END_TIME).`val`()
        data[HID_ES_SHOW_ROOM_CODE] = examDoc.getElementById(HID_ES_SHOW_ROOM_CODE).`val`()
        data[HID_STUDENT_ID] = examDoc.getElementById(HID_STUDENT_ID).`val`()
        data[DRP_SEMESTER] = semester ?: ""

        examDoc = postDoc(URL_BASE + URL_EXAM_TIMETABLE, data)
        if (dot.isNullOrEmpty()) return examDoc

        data[KEY_VIEW_STATE] = examDoc.getElementById(KEY_VIEW_STATE).`val`()
        data[KEY_VIEW_STATE_GENERATOR] = examDoc.getElementById(KEY_VIEW_STATE_GENERATOR).`val`()
        data[KEY_EVENT_VALIDATION] = examDoc.getElementById(KEY_EVENT_VALIDATION).`val`()
        data[DRP_DOT_THI] = dot

        return postDoc(URL_BASE + URL_EXAM_TIMETABLE, data)
    }

    private suspend fun getDoc(url: String): Document {
        return withContext(ioDispatcher) {
            val doc = async {
                Jsoup.connect(url).cookies(
                    cookies ?: throw IllegalStateException("User has logged out, cookies = null")
                ).timeout(TIME_OUT).get()
            }
            noError(doc.await())
        }
    }

    private suspend fun postDoc(url: String, data: Map<String, String>): Document {
        return withContext(ioDispatcher) {
            val doc = async {
                Jsoup.connect(url).cookies(
                    cookies ?: throw IllegalStateException("User has logged out, cookies = null")
                ).data(data).timeout(TIME_OUT).post()
            }
            return@withContext noError(doc.await())
        }
    }

    private fun noError(doc: Document): Document {
        if (doc.html().length < 600 && doc.html().contains(MSG_ERROR_PAGE))
            throw HttpStatusException(MSG_ERROR_PAGE, 904, doc.baseUri())
        return doc
    }
}