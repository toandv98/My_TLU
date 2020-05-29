package com.toandv.mytlu.remote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Suppress("SpellCheckingInspection")
object DummyDocument {
    /**
     * @return [Document] of this url:
     * http://dangky.tlu.edu.vn/cmcsoft.iu.web.info/StudentViewExamList.aspx
     */
    fun getStudentViewExamList_aspx_html(): Document {
        return Jsoup.parse(
            javaClass.classLoader!!.getResource("StudentViewExamList.html").readText()
        )
    }

    /**
     * @return [Document] of this url:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/StudentService/StudentTuition.aspx
     */
    fun getStudentTuition_aspx_html(): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentTuition.html").readText())
    }

    /**
     * @return [Document] of this url:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/Reports/Form/StudentTimeTable.aspx
     */
    fun getStudentTimeTable_aspx_html(): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentTimeTable.html").readText())
    }

    /**
     * @return [Document] of this url:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.info/StudentMark.aspx
     */
    fun getStudentMark_aspx_html(): Document {
        return Jsoup.parse(javaClass.classLoader!!.getResource("StudentMark.html").readText())
    }

    /**
     * @return [Document] of this url:
     * http://dangky.tlu.edu.vn/CMCSoft.IU.Web.Info/StudentService/PractiseMarkAndStudyWarning.aspx
     */
    fun getPractiseMarkAndStudyWarning_aspx_html(): Document {
        return Jsoup.parse(
            javaClass.classLoader!!.getResource("PractiseMarkAndStudyWarning.html").readText()
        )
    }
}