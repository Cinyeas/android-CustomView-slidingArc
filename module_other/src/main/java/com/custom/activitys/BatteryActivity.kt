package com.custom.activitys

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.custom.other.R
import kotlinx.android.synthetic.main.activity_battery.*

class BatteryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)

        var mBatterValue = 0.01f
        val mThread = Thread() {
            run {
                while (true) {
                    Thread.sleep(500)
                    battery_view.setPower(mBatterValue)
                    mBatterValue += 0.02f
                    if (mBatterValue >= 1f) {
                        mBatterValue = 0f
                    }
                }
            }
        }
        mThread.start()
    }
}