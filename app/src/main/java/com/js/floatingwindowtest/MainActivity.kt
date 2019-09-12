package com.js.floatingwindowtest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.js.libfwindow.FloatingWindow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btOpen = findViewById<Button>(R.id.bt_open)

        FloatingWindow.INSTANCE.initWindow(this)
        btOpen.setOnClickListener{
            if(!FloatingWindow.INSTANCE.isOpenView()) {
                val textView = TextView(this)
                textView.text = "https://github.com/DOUBLEYOUJS"
                textView.setTextColor(Color.GREEN)
                FloatingWindow.INSTANCE.addView(textView, 200, 500)
            } else {
                FloatingWindow.INSTANCE.closeWindow()
            }
        }
    }

    override fun onDestroy() {
        FloatingWindow.INSTANCE.destory()
        super.onDestroy()
    }
}
