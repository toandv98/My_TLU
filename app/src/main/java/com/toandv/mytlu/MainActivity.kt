package com.toandv.mytlu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.toandv.mytlu.remote.JsoupServiceImp

class MainActivity : AppCompatActivity() {

    val jsoupServiceImp = JsoupServiceImp()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
