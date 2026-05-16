package com.custom.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.custom.other.R
import kotlinx.android.synthetic.main.activity_circletime.*

class CircleTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circletime)

        circle_time_view.setTimeLength(10)
        circle_time_view.start()
    }
}