package com.toandv.mytlu.data.remote

import com.toandv.mytlu.utils.URL_BASE
import com.toandv.mytlu.utils.URL_LOGIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class JsoupServiceImpTest {
    private lateinit var subject: JsoupService

    private lateinit var messageDigest: MessageDigest

    private val acc = "1651060845"
    private val pw = "Cong0336594779"
    private val `class` = "58TH3"
    private val k58 = "K58"

    // d504d5f66b63f694b51200516a6bde35
    private lateinit var md5pw: String

    @Before
    fun setup() {
        subject = JsoupServiceImp(Dispatchers.Unconfined)
        messageDigest = MessageDigest.getInstance("MD5")
        md5pw = messageDigest.digest(pw.toByteArray(Charsets.UTF_8))
            .joinToString("") { String.format("%02x", it) }
    }

    @After
    fun teardown() {
        subject.logout()
    }

    @Test
    fun login_accountPasswordMD5_true() = runBlockingTest {
        println("with ($acc, $pw) -> ($acc, $md5pw)")

        // check has not logged in
        assertThat(subject.isLoggedIn, `is`(false))

        // check false if password is not hashed
        val result = subject.login(acc, pw)
        assertThat(result, `is`(false))

        // check true if password is hashed
        val resultMD5 = subject.login(acc, md5pw)
        assertThat(resultMD5, `is`(true))
    }

    @Test
    fun getMarkdoc_loggedIn_true() = runBlockingTest {
        // Given
        // map of element id and its value
        val checkMap = mapOf<String, String>(
            "lblStudentCode" to acc,
            "lblAdminClass" to `class`,
            "lblAy" to k58
        )

        // login
        assertThat(subject.login(acc, md5pw), equalTo(true))

        val result = subject.getMarkDoc()

        checkMap.forEach { (elementId: String, value: String) ->
            assertThat(result.getElementById(elementId).text(), equalTo(value))
        }
    }

    @Test
    fun getMarkDoc_loggedOut_false() = runBlockingTest {
        // login with wrong password
        assertThat(subject.login(acc, pw), equalTo(false))

        val url = URL_BASE + "CMCSoft.IU.Web.info/Home.aspx"

        val result = subject.getMarkDoc()
        assertThat(result.baseUri(), `is`(url))
    }

    @Test
    fun getTuitionDoc_loggedIn_true() = runBlockingTest {
        // login
        assertThat(subject.login(acc, md5pw), equalTo(true))

        val doc = subject.getTuitionDoc()
        val idBankAccountNum = "lblSoTaiKhoanNganHang"
        val expected = "Số tài khoản ngân hàng: 100003570711"
        assertThat(doc.getElementById(idBankAccountNum).text(), `is`(expected))
    }
}