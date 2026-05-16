package com.arc.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.custom.arc.menu.SemiCircleMenuView
import kotlinx.android.synthetic.main.activity_arcmenu.*

class ArcMenuActivity : AppCompatActivity(), SemiCircleMenuView.MenuInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arcmenu)

        menu_view.setMenuIml(this)
    }

    /**
     * 分享按钮
     */
    override fun onClickShared(flag: Int) {
        when (flag) {
            SemiCircleMenuView.VOICE_IMG_VIEW_TAG -> {
                resetMenu(flag) {}
            }

            SemiCircleMenuView.SET_IMG_VIEW_TAG -> {
                resetMenu(flag) {

                }
            }

            SemiCircleMenuView.USER_IMG_VIEW_TAG -> {
                resetMenu(flag) {

                }
            }

            SemiCircleMenuView.RESET_IMG_VIEW_TAG, SemiCircleMenuView.NORMAL_IMG_VIEW_TAG -> {   //分享地锁

            }
        }
    }

    /**
     * 声音按钮
     */
    override fun onClickVoice(flag: Int) {
        when (flag) {
            SemiCircleMenuView.NORMAL_IMG_VIEW_TAG, SemiCircleMenuView.RESET_IMG_VIEW_TAG -> {
                menu_view.resetAnimationStatus()
                menu_view.setImgBuTag(SemiCircleMenuView.VOICE_IMG_VIEW_TAG)
                shared_img_view.visibility = View.GONE
                set_img_view.setBackgroundResource(R.drawable.main_sound_short)
                user_img_view.setBackgroundResource(R.drawable.main_sound_long)
                set_img_view.setTextStr("短鸣")
                user_img_view.setTextStr("长鸣")
                set_img_view.setTvColor(resources.getColor(R.color.colorBlue_3))
                user_img_view.setTvColor(resources.getColor(R.color.colorBlue_3))
                menu_view.invalidate()
            }

            SemiCircleMenuView.VOICE_IMG_VIEW_TAG -> {                //寻锁鸣叫

            }

            SemiCircleMenuView.SET_IMG_VIEW_TAG -> {                    //灯光亮度

            }

            SemiCircleMenuView.USER_IMG_VIEW_TAG -> {}
        }
    }

    /**
     * 设置按钮
     */
    override fun onClickSet(flag: Int) {
        when (flag) {
            SemiCircleMenuView.NORMAL_IMG_VIEW_TAG, SemiCircleMenuView.RESET_IMG_VIEW_TAG -> {

            }

            SemiCircleMenuView.VOICE_IMG_VIEW_TAG -> {

            }

            SemiCircleMenuView.SET_IMG_VIEW_TAG -> {

            }

            SemiCircleMenuView.USER_IMG_VIEW_TAG -> {

            }
        }
    }

    /**
     * 用户按钮
     */
    override fun onClickUser(flag: Int) {
        when (flag) {
            SemiCircleMenuView.NORMAL_IMG_VIEW_TAG, SemiCircleMenuView.RESET_IMG_VIEW_TAG -> {
                menu_view.resetAnimationStatus()
                menu_view.setImgBuTag(SemiCircleMenuView.USER_IMG_VIEW_TAG)
                shared_img_view.setBackgroundResource(R.drawable.me_islot)
                voice_img_view.setBackgroundResource(R.drawable.me_my_black)
                set_img_view.setBackgroundResource(R.drawable.setting)
                user_img_view.setBackgroundResource(R.drawable.me_message_tips)
                menu_view.setWidgetBgColor(resources.getColor(R.color.white))

                shared_img_view.setTextStr("控锁")
                voice_img_view.setTextStr("我的")
                set_img_view.setTextStr("设置")
                user_img_view.setTextStr("通知")

                shared_img_view.setTvColor(resources.getColor(R.color.colorBlue_3))
                voice_img_view.setTvColor(resources.getColor(R.color.black))
                set_img_view.setTvColor(resources.getColor(R.color.black))
                user_img_view.setTvColor(resources.getColor(R.color.black))

                menu_view.invalidate()
                menu_view.setCanvasIml {

                }
            }

            SemiCircleMenuView.SET_IMG_VIEW_TAG -> {

            }

            SemiCircleMenuView.USER_IMG_VIEW_TAG -> {
            }
        }
    }

    /**
     * 底部小叉叉的事件回调
     * 重置底部菜单
     */
    override fun onReLoadMenu() {
        resetMenu(0) {
        }
    }

    /**
     * 重置底部菜单
     */
    private fun resetMenu(flag: Int, callback: SemiCircleMenuView.CanvasCallBackInterface) {
        menu_view.resetAnimationStatus()
        menu_view.setLastAreaTag(flag)
        menu_view.setImgBuTag(SemiCircleMenuView.RESET_IMG_VIEW_TAG)
        shared_img_view.visibility = View.VISIBLE
        menu_view.setWidgetBgColor(resources.getColor(R.color.white))
        shared_img_view.setBackgroundResource(R.drawable.setting)
        voice_img_view.setBackgroundResource(R.drawable.findcar)
        set_img_view.setBackgroundResource(R.drawable.control)
        user_img_view.setBackgroundResource(R.drawable.user_my)

        shared_img_view.setTextStr("设置")
        voice_img_view.setTextStr("寻车")
        set_img_view.setTextStr("控锁")
        user_img_view.setTextStr("我的")
        shared_img_view.setTvColor(resources.getColor(R.color.black))
        voice_img_view.setTvColor(resources.getColor(R.color.black))
        set_img_view.setTvColor(resources.getColor(R.color.black))
        user_img_view.setTvColor(resources.getColor(R.color.colorBlue_3))

        menu_view.invalidate()
        menu_view.setCanvasIml {
            callback.deawFinsh()
        }
    }
}