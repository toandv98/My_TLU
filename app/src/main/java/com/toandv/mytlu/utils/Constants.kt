package com.toandv.mytlu.utils

internal const val SEMESTER_VALUE = "5ad062230c5c4973a1bd241ed876d6fb"

internal const val URL_LINK = "http://dangky.tlu.edu.vn/"
internal const val URL_LOGIN_PAGE = "CMCSoft.IU.Web.info/login.aspx"
internal const val URL_HOME_PAGE = "CMCSoft.IU.Web.info/Home.aspx"
internal const val URL_TIME_TABLE = "CMCSoft.IU.Web.Info/Reports/Form/StudentTimeTable.aspx"
internal const val URL_MARK_PAGE = "CMCSoft.IU.Web.info/StudentMark.aspx"
internal const val URL_PRACTISE_MARK =
     "CMCSoft.IU.Web.Info/StudentService/PractiseMarkAndStudyWarning.aspx"
internal const val URL_STUDENT_TUITION = "CMCSoft.IU.Web.Info/StudentService/StudentTuition.aspx"
internal const val URL_ERROR_PAGE = "ErrorPage"
internal const val URL_BLANK_PAGE = "about:blank"
internal const val URL_EXAM_TIMETABLES = "cmcsoft.iu.web.info/StudentViewExamList.aspx"

internal const val SCRIPT_GET_MARK_DETAIL =
     "(function() { return (document.getElementById('tblMarkDetail').innerHTML + document.getElementById('tblSumMark').innerHTML); })();"
internal const val SCRIPT_GET_MARK_HK =
     "(function() { return (document.getElementById('tblMarkDetail').innerHTML); })();"
internal const val SCRIPT_GET_SCHEDULE =
     "(function() { return ('<select id=\"drpTerm\">' + document.getElementById('drpTerm').innerHTML + '</select>' + document.getElementById('Table2').innerHTML); })();"
internal const val SCRIPT_GET_PRACTISE_MARK =
     "(function() { return (document.getElementById('Table1').innerHTML); })();"
internal const val SCRIPT_GET_LOGIN =
     "(function() { return (document.getElementById('lblErrorInfo').innerHTML.length); })();"
internal const val SCRIPT_GET_TUITION =
     "(function() {return document.getElementById('Table4').innerHTML;})();"
internal const val SCRIPT_GET_EXAM_TIMETABLES =
     "(function() {return '<select id=\"drpDotThi\">' + document.getElementById('drpDotThi').innerHTML + '</select>' + document.getElementById('Table15').innerHTML;})();"

internal const val JS_LOGIN =
         "javascript:document.getElementById('txtUserName').value = '%s';" +
         "document.getElementById('txtPassword').value = CryptoJS.MD5('%s').toString();" +
         "javascript:document.getElementById('btnSubmit').click();"

internal const val JS_CHANGE_TIME_SCHEDULE =
     "javascript:document.getElementById('drpSemester').value='%s';" +
             "document.getElementById('drpSemester').onchange();"

internal const val JS_CHANGE_TERM_SCHEDULE =
     "javascript:document.getElementById('drpTerm').value='%s';" +
             "document.getElementById('drpTerm').onchange();"

internal const val JS_CHANGE_TIME_EXAM_TIMETABLES =
     "javascript:document.getElementById('drpSemester').value='%s';" +
             "document.getElementById('drpSemester').onchange();"

internal const val JS_CHANGE_TIME_DOT_THI =
     "javascript:document.getElementById('drpDotThi').value='%s';" +
             "document.getElementById('drpDotThi').onchange();"

internal const val JS_CHANGE_HK =
     "javascript:document.getElementById('drpHK').lastElementChild.selected = true;" +
             "document.getElementById('drpHK').onchange();"

internal const val SDK_VERSION_RELEASE = "6.0"

internal const val CHANNEL_ID = "subject_notification"
internal const val CHANNEL_UPDATE_ID = "12"
internal const val ACTION_SCHEDULE = "action_schedule"
internal const val ACTION_EXAM_SCHEDULE = "action_exam_schedule"
internal const val EXTRA_NAME_SCHEDULE = "subject_name"
internal const val EXTRA_NAME_EXAM_SCHEDULE = "exam_timetables"

internal const val PATTERN_TIME_FORMAT = "HH:mm"
internal const val INPUT_DATE_FORMAT = "dd/MM/yyyy"
internal const val PATTERN_DATE_FORMAT = "EEEE, dd/MM/yyyy"
internal const val INPUT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"

internal const val KEY_EXAM_TIMETABLES_HTML = "exam_timetables_html"
internal const val KEY_DOT_THI_ARRAYS = "dot_thi_arrays"
internal const val KEY_DOT_HOC_ARRAYS = "dot_hoc_arrays"
internal const val KEY_SCHEDULE_HTML = "schedule_html"
internal const val KEY_NEW_SCHEDULE_HTML = "new_schedule_html"
internal const val KEY_TRANSCRIPT_HTML = "student_mark_html"
internal const val KEY_TRANSCRIPT_HK_HTML = "student_mark_hk_html"
internal const val KEY_PRACTISE_MARK = "practise_mark"
internal const val KEY_TUITION = "tuition_html"
internal const val PREFERENCE_NAME = "html_data"
internal const val ENCRYPT_PREFERENCE = "secret_shared_prefs"
internal const val KEY_CIPHER_P = "cipher_p"
internal const val KEY_CIPHER_U = "cipher_u"
internal const val KEY_LAST_UPDATED = "last_updated"
internal const val KEY_ALARM_SCHEDULE_COUNT = "alarm_schedule_count"
internal const val KEY_ALARM_EXAM_COUNT = "alarm_exam_count"
internal const val DATE_02_02_2020 = "2020-02-02"

internal const val ERROR_CODE_LOGIN_FAILED = -2
internal const val ERROR_CODE_ERROR_PAGE = -3
internal const val CODE_FINISHED = -1
internal const val ERROR_CODE_TIME_OUT = -4
internal const val ERROR_CODE_USERNAME_EMPTY = -5
internal const val ERROR_CODE_PASSWORD_EMPTY = -6
internal const val ERROR_CODE_NETWORK_ERROR = -7
internal const val CODE_CLEAR_DATA = -10

internal const val PROGRESS_LOGIN = 10
internal const val PROGRESS_HOME_PAGE = 15
internal const val PROGRESS_TIMETABLES_SELECT = 20
internal const val PROGRESS_TIMETABLES_CHANGE = 30
internal const val PROGRESS_TIMETABLES_DONE = 40
internal const val PROGRESS_SELECTED_MARK = 50
internal const val PROGRESS_MARK_PAGE = 60
internal const val PROGRESS_PRACTISE_MARK = 70
internal const val PROGRESS_STUDENT_TUITION = 80
internal const val PROGRESS_EXAM_TIMETABLES = 90
internal const val PROGRESS_FINISHED = 100

internal const val ACTION_QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON"

internal const val ACC_INFO = 1