package com.custom.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.custom.other.LightCircleView.COLOR_ATTR_VALUE_ARR
import com.custom.other.R
import kotlinx.android.synthetic.main.activity_battery.*
import kotlinx.android.synthetic.main.activity_lightcircle.*
import kotlinx.android.synthetic.main.activity_lightcircle.view.*

class LightCircleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lightcircle)

        var mBatterValue = 1
        val mThread = Thread() {
            run {
                while (true) {
                    Thread.sleep(500)
                    light_circle_view.setPaintColor(mBatterValue)
                    mBatterValue += 1
                    if (mBatterValue >= COLOR_ATTR_VALUE_ARR.size) {
                        mBatterValue = 1
                    }
                }
            }
        }
        mThread.start()
    }
}