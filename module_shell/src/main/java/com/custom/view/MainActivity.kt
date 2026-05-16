package com.custom.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.arc.menu.ArcMenuActivity
import com.custom.activitys.BatteryActivity
import com.custom.activitys.CircleTimeActivity
import com.custom.activitys.LightCircleActivity
import com.poster.ActivityPoster
//import com.poster.ActivityPoster
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arc_menu_view.setOnClickListener(this)
        poster_view.setOnClickListener(this)
        battery_view.setOnClickListener(this)
        circle_view.setOnClickListener(this)
        light_view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            /**
             * 弧形菜单
             */
            R.id.arc_menu_view -> {
                startActivity(Intent(this@MainActivity, ArcMenuActivity::class.java))
            }

            /**
             * 海报编辑
             */
            R.id.poster_view->{
                startActivity(Intent(this@MainActivity, ActivityPoster::class.java))
            }

            /**
             * 电池电量
             */
            R.id.battery_view->{
                startActivity(Intent(this@MainActivity, BatteryActivity::class.java))
            }

            /**
             * 欢迎页面倒计时
             */
            R.id.circle_view->{
                startActivity(Intent(this@MainActivity, CircleTimeActivity::class.java))
            }

            /**
             * 灯光控制
             */
            R.id.light_view->{
                startActivity(Intent(this@MainActivity, LightCircleActivity::class.java))
            }
        }
    }
}