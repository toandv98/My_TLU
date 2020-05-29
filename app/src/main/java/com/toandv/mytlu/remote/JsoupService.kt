package com.toandv.mytlu.remote

import org.jsoup.nodes.Document

interface JsoupService {

    val isLoggedIn: Boolean

    suspend fun login(username: String, password: String): Boolean

    fun logout()

    /**
     * get [Document] from this link:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/StudentService/StudentTuition.aspx
     * @return [Document] if success
     */
    suspend fun getTuitionDoc(): Document

    /**
     * get [Document] from this link:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.info/StudentMark.aspx
     * @return [Document] if success
     */
    suspend fun getMarkDoc(): Document

    /**
     * get [Document] from this link:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/StudentService/PractiseMarkAndStudyWarning.aspx
     * @return [Document] if success
     */
    suspend fun getPractiseDoc(): Document

    /**
     * get [Document] from this link:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/Reports/Form/StudentTimeTable.aspx
     * @param semester semester code
     * @param term term code
     * @return [Document] if success
     */
    suspend fun getTimetableDoc(semester: String? = null, term: String? = null): Document

    /**
     * get [Document] from this link:
     * http://dangky.tlu.edu.vn/cmcsoft.iu.web.info/StudentViewExamList.aspx
     * @param semester semester code
     * @param dot "đợt" code
     * @return [Document] if success
     */
    suspend fun getExamTimetableDoc(semester: String? = null, dot: String? = null): Document
}