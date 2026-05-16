package com.poster

import android.Manifest
import com.poster.adapter.AdapterForPictureTrack.ItemClickListener
import com.poster.fragment.FragmentForSelect
import com.poster.fragment.FragmentForFlash
import com.poster.fragment.FragmentForFont
import com.poster.fragment.FragmentForAnno
import com.poster.model.PreviewModel
import com.poster.adapter.AdapterForPictureTrack
import android.view.WindowManager
import butterknife.ButterKnife
import android.content.Intent
import android.annotation.SuppressLint
import com.poster.model.PreviewModel.TemplateBean.PagesBean.ObjectsBean
import com.poster.custom.StickerTextView
import com.poster.custom.StickerImageView
import com.bumptech.glide.Glide
import com.poster.http.UrlConfig
import android.view.ViewGroup
import com.poster.utils.FloatingManager
import com.poster.model.PreviewModel.TemplateBean.PagesBean
import com.poster.dialogs.DownloadDialog.DialogListener
import com.poster.dialogs.DialogUtils
import android.content.DialogInterface
import com.poster.dialogs.DownloadDialog
import com.poster.model.BeanComparator
import com.poster.model.PreviewModel.TemplateBean
import com.arthenica.mobileffmpeg.FFmpeg
import com.poster.utils.DensityUtils
import pl.droidsonroids.gif.GifIOException
import com.poster.glidecache.GlideCache
import android.graphics.drawable.ColorDrawable
import com.poster.custom.StickerView.StickerViewIml
import com.google.gson.Gson
import android.app.Activity
import android.graphics.*
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arthenica.mobileffmpeg.Config
import com.poster.utils.MathUtils
import kotlinx.android.synthetic.main.activity_poster.*
import pl.droidsonroids.gif.GifDrawable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.NullPointerException
import java.util.*

class ActivityPoster : AppCompatActivity(), View.OnClickListener, ItemClickListener {
    private val permission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )

    private var fragmentManager: FragmentManager? = null
    private var mFragmentSelect: FragmentForSelect? = null
    private var mFragmentFlash: FragmentForFlash? = null
    private var mFragmentFont: FragmentForFont? = null
    private var mFragmentAnno: FragmentForAnno? = null
    private var mPreview: PreviewModel? = null
    private var mHandlerThread: HandlerThread? = null
    private var mHandler: Handler? = null
    private var mPictureTrackAdapter: AdapterForPictureTrack? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_poster)

        ButterKnife.bind(this)
        requestPermiss()
        defaultViewBg(R.color.colorGray, false)
        copy_text_view!!.setTextColor(resources.getColor(R.color.colorGray))
        delete_text_view!!.setTextColor(resources.getColor(R.color.colorGray))
        last_text_view!!.setTextColor(resources.getColor(R.color.colorGray))
        next_text_view!!.setTextColor(resources.getColor(R.color.colorGray))
        copy_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.copy_img_0, 0, 0)
        delete_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.delete_img_0, 0, 0)
        last_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.last_view_img_0, 0, 0)
        next_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.next_view_img_0, 0, 0)
        copy_text_view!!.isClickable = false
        delete_text_view!!.isClickable = false
        last_text_view!!.isClickable = false
        next_text_view!!.isClickable = false
        select_text_view!!.setOnClickListener(this)
        add_text_view!!.setOnClickListener(this)
        change_text_view!!.setOnClickListener(this)
        anno_text_view!!.setOnClickListener(this)
        right_edit_view!!.setOnClickListener(this)
        float_bu_view!!.setOnClickListener(this)
        last_text_view!!.setOnClickListener(this)
        next_text_view!!.setOnClickListener(this)
        copy_text_view!!.setOnClickListener(this)
        delete_text_view!!.setOnClickListener(this)
        add_img_view!!.setOnClickListener(this)
        preview!!.setOnClickListener(this)
        create_view_tv!!.setOnClickListener(this)
        mPictureTrackAdapter = AdapterForPictureTrack(this)
        mPictureTrackAdapter!!.setOnClickListener(this)
        fragmentManager = supportFragmentManager
        mFragmentSelect = FragmentForSelect()
        mFragmentFlash = FragmentForFlash()
        mFragmentFont = FragmentForFont()
        mFragmentAnno = FragmentForAnno()
        thisActivity = this
        initTestData()
        initEditView()
        threadHandler()
    }

    fun getRightEditVIew(): FrameLayout? {
        return right_edit_view
    }

    private fun requestPermiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                createPath()
                Log.d("TAG", "已获取android读写权限")
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, 200)
            }
        } else {
            savePath()
        }
    }

    private fun savePath() {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 创建存储路径
     */
    private fun createPath() {
        try {
            val picFile = getExternalFilesDir("")
            val file = File(path)
            if (!picFile!!.exists()) {
                picFile.createNewFile()
            } else if (!file.exists()) {
                file.mkdirs()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                createPath()
                Log.d("TAG", "已获取android读写权限")
            } else {
                Log.d("TAG", "存储权限获取失败")
            }
        }
    }

    fun defaultViewBg(color: Int, status: Boolean) {
        if (status) {
            copy_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.copy_img, 0, 0)
            delete_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.delete_img, 0, 0)
            last_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.last_view_img, 0, 0)
            next_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.next_view_img, 0, 0)
        } else {
            copy_text_view.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.copy_img_0, 0, 0)
            delete_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.delete_img_0, 0, 0)
            last_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.last_view_img_0, 0, 0)
            next_text_view!!.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.next_view_img_0, 0, 0)
        }
        copy_text_view!!.setTextColor(resources.getColor(color))
        delete_text_view!!.setTextColor(resources.getColor(color))
        last_text_view!!.setTextColor(resources.getColor(color))
        next_text_view!!.setTextColor(resources.getColor(color))
        copy_text_view!!.isClickable = status
        delete_text_view!!.isClickable = status
        last_text_view!!.isClickable = status
        next_text_view!!.isClickable = status
        copy_text_view!!.isEnabled = status
        delete_text_view!!.isEnabled = status
        last_text_view!!.isEnabled = status
        next_text_view!!.isEnabled = status
    }

    /**
     * 初始化编辑区域
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initEditView() {
        setSelectStatu(select_text_view)
        mFragmentSelect!!.setData(mPreview!!.template.pages[0].objects)
        replaceFragment(mFragmentSelect)
        mPictureTrackAdapter!!.loadView(mPreview, bottom_jpg_view) //初始化底部海报状态
        for (i in mPreview!!.template.pages.indices) {
            try {
                val mLayout = getNewEditView(i)
                right_edit_view!!.addView(mLayout)
                val bean = mPreview!!.template.pages[i].objects
                for (index in bean.indices) {
                    val mItemBean = bean[index]
                    if (mItemBean.type == 0) {                     //0:文本
                        StickerTextView.init(
                            mItemBean.text,
                            Color.parseColor(mItemBean.color),
                            mItemBean.fontsize.toInt()
                        )
                        val tv = StickerTextView(baseContext)
                        tv.setTag(R.id.tag_widget_view, mItemBean.id)
                        tv.setTag(R.id.tag_text_size, mItemBean.fontsize)
                        tv.setTag(R.id.tag_view_type, mItemBean.type)
                        tv.setTag(R.id.tag_view_text_type, mItemBean.font)
                        tv.setTag(R.id.tag_view_x, mItemBean.x)
                        tv.setTag(R.id.tag_view_y, mItemBean.y)
                        tv.setViewListener(mStickerIml)
                        setViewLocation(mLayout, tv, mItemBean.x, mItemBean.y, 0, 0)
                    } else if (mItemBean.type == 1 || mItemBean.type == 2) {              //1：图片   2：动画
                        val img = StickerImageView(baseContext)
                        img.setTag(R.id.tag_img_url, mItemBean.img)
                        img.setTag(R.id.tag_widget_view, mItemBean.id)
                        img.setTag(R.id.tag_view_type, mItemBean.type)
                        img.setTag(R.id.tag_view_x, mItemBean.x)
                        img.setTag(R.id.tag_view_y, mItemBean.y)
                        Glide.with(baseContext).load(UrlConfig.BASE_URL + mItemBean.img).into(img.imgView)
                        img.setViewListener(mStickerIml)
                        setViewLocation(mLayout, img, mItemBean.x, mItemBean.y, mItemBean.width, mItemBean.height)
                    }
                }
            } catch (e: NullPointerException) {
                Toast.makeText(baseContext, "数据解析异常", Toast.LENGTH_SHORT).show()
            }
        }
        right_edit_view!!.findViewWithTag<View>(0).bringToFront()
    }

    /**
     * 创建新的编辑区域
     *
     * @param id
     * @return
     */
    private fun getNewEditView(id: Int): ConstraintLayout {
        val mLayout = ConstraintLayout(this)
        mLayout.setBackgroundColor(resources.getColor(R.color.colorMainBg))
        mLayout.tag = id
        return mLayout
    }

    /**
     * 设置View的显示位置
     *
     * @param view
     * @param x
     * @param y
     */
    private fun setViewLocation(parentView: ViewGroup, view: View, x: Int, y: Int, width: Int, hight: Int) {
        FloatingManager.getInstance()
            .setParentView(parentView)
            .setAnchorView(view)
            .setSize(width, hight)
            .setXY(x, y)
            .showCenterView()
    }

    /**
     * 底部海报图片点击事件
     *
     * @param position
     * @param bean
     * @param view
     */
    override fun onClick(position: Int, bean: PagesBean, view: View) {
        val viewPage = right_edit_view!!.findViewWithTag<View>(view.tag)
        if ((viewPage as ViewGroup).childCount == 0) {
            defaultViewBg(R.color.colorGray, false)
        }
        for (index in 0 until viewPage.childCount) {
            val child = viewPage.getChildAt(index)
            if ((child.findViewWithTag("iv_border") as View).visibility == View.VISIBLE || (child.findViewWithTag("iv_scale") as View).visibility == View.VISIBLE || (child.findViewWithTag(
                    "iv_delete"
                ) as View).visibility == View.VISIBLE
            ) {
                defaultViewBg(R.color.colorBlack, true)
                break
            }
            if (index == viewPage.childCount - 1) {
                defaultViewBg(R.color.colorGray, false)
            }
        }
        viewPage.bringToFront()
        refreshbottomImg(view)
        refushRightView(position, bean)
    }

    /**
     * 下载框事件
     */
    private val downloadIml = DialogListener {
        status = false
        num = 0
        mDownload!!.updateProgress(num)
        mDownload!!.dismiss()
    }

    /**
     * 刷新右侧界面
     */
    private fun refushRightView(position: Int, bean: PagesBean?) {
        val fg = fragmentManager!!.findFragmentById(R.id.material_view_id)
        if (fg is FragmentForSelect) {
            if (bean == null) {
                mFragmentSelect!!.setData(null)
            } else {
                mFragmentSelect!!.setData(mPreview!!.template.pages[position].objects)
            }
            mFragmentSelect!!.notifyDataChange()
        }
    }

    /**
     * 底部海报图片长按事件
     *
     * @param position
     * @param bean
     * @param view
     */
    override fun onLongClick(position: Int, bean: PagesBean, view: View) {
        DialogUtils.showConfirmationDialog(this@ActivityPoster, "确认删除？") { dialog: DialogInterface?, which: Int ->
            val tag = view.tag
            bottom_jpg_view!!.removeView(view.parent as View)
            right_edit_view!!.removeView(right_edit_view!!.findViewWithTag(tag))
            val imgView = (bottom_jpg_view!!.getChildAt(0) as ViewGroup).getChildAt(0)
            refreshbottomImg(imgView)
            right_edit_view!!.findViewWithTag<View>(imgView.tag).bringToFront()
            mPreview!!.template.pages.removeAt(position)
        }
    }

    override fun onClick(v: View) {
        val mGroupView = right_edit_view!!.getChildAt(right_edit_view!!.childCount - 1) as ViewGroup
        when (v.id) {
            R.id.select_text_view -> {
                setSelectStatu(select_text_view)
                mFragmentSelect!!.setData(mPreview!!.template.pages[bottomStatus].objects)
                replaceFragment(mFragmentSelect)
            }
            R.id.add_text_view -> {
                setSelectStatu(add_text_view)
                replaceFragment(mFragmentFlash)
            }
            R.id.change_text_view -> {
                setSelectStatu(change_text_view)
                replaceFragment(mFragmentFont)
            }
            R.id.create_view_tv -> {
                mDownload = DownloadDialog(this@ActivityPoster)
                mDownload!!.setDialogListener(downloadIml)
                savePath()
                createViewArr(mGroupView)
            }
            R.id.preview -> {}
            R.id.add_img_view -> {
                /**
                 * 新增一个编辑界面，并且移动到顶层
                 */
                val mTag = bottom_jpg_view!!.childCount - 1
                val mLayout = getNewEditView(mTag)
                right_edit_view!!.addView(mLayout)
                right_edit_view!!.findViewWithTag<View>(mTag).bringToFront()
                val childView = View.inflate(baseContext, R.layout.adapter_item_picture_track, null) as ConstraintLayout
                val itemView = childView.findViewById<ImageView>(R.id.item_img_view)
                itemView.id = R.id.tag_img_bottom_item
                itemView.setOnClickListener(this)
                itemView.setOnLongClickListener { v1: View? ->
                    onLongClick(bottom_jpg_view!!.indexOfChild(childView), null!!, itemView)
                    false
                }
                itemView.tag = mTag
                bottom_jpg_view!!.addView(childView, mTag)
                refreshbottomImg(itemView)
                refushRightView(0, null)
                defaultViewBg(R.color.colorGray, false)
                /**
                 * 追加一条数据
                 */
                val bean = PagesBean()
                mPreview!!.template.pages.add(bean)
            }
            R.id.tag_img_bottom_item -> onClick(0, null!!, v)
            R.id.copy_text_view -> {
                val viewId = rightItemMaxId
                val bottomId = bottomStatus
                val `object` = ObjectsBean()
                var index = 0
                while (index < mGroupView.childCount) {
                    val child = mGroupView.getChildAt(index)
                    val x = child.x.toInt() + 10
                    val y = child.y.toInt() + 10
                    `object`.type = child.getTag(R.id.tag_view_type).toString().toInt()
                    `object`.x = x
                    `object`.y = y
                    `object`.id = viewId + 1
                    if (child.getTag(R.id.tag_view_rotation) == null) {
                        `object`.angle = 0f
                    } else {
                        `object`.angle = child.getTag(R.id.tag_view_rotation).toString().toFloat()
                    }
                    if ((child.findViewWithTag("iv_border") as View).visibility == View.VISIBLE && (child.findViewWithTag(
                            "iv_scale"
                        ) as View).visibility == View.VISIBLE && (child.findViewWithTag("iv_delete") as View).visibility == View.VISIBLE
                    ) {
                        if (child is StickerTextView) {
                            val textView = child
                            StickerTextView.init(textView.text, textView.textColor, textView.textSize)
                            val tv = StickerTextView(baseContext)
                            tv.setTag(R.id.tag_widget_view, viewId + 1)
                            tv.setTag(R.id.tag_text_size, textView.getTag(R.id.tag_text_size))
                            tv.setTag(R.id.tag_view_type, textView.getTag(R.id.tag_view_type))
                            tv.setViewListener(mStickerIml)
                            setViewLocation(child.getParent() as ViewGroup, tv, x, y, 0, 0)
                            setBorder(tv)
                            `object`.text = textView.text
                            `object`.color = String.format("#%X", textView.textColor)
                            `object`.fontsize = "" + textView.textSize
                            `object`.font = textView.getTag(R.id.tag_view_text_type).toString()
                        } else {
                            val imgView = child as StickerImageView
                            val img = StickerImageView(baseContext)
                            img.setTag(R.id.tag_widget_view, viewId + 1)
                            img.setTag(R.id.tag_view_type, imgView.getTag(R.id.tag_view_type))
                            Glide.with(baseContext)
                                .load(UrlConfig.BASE_URL + imgView.getTag(R.id.tag_img_url).toString())
                                .into(img.imgView)
                            img.setViewListener(mStickerIml)
                            setViewLocation(child.getParent() as ViewGroup, img, x, y, imgView.width, imgView.height)
                            setBorder(img)
                            `object`.img = imgView.getTag(R.id.tag_img_url).toString()
                            `object`.width = imgView.width
                            `object`.height = imgView.height
                        }
                        break
                    }
                    index++
                }
                mPreview!!.template.pages[bottomId].objects.add(`object`)
                refushRightView(bottomId, mPreview!!.template.pages[bottomId])
            }
            R.id.delete_text_view -> {
                val pageIndex = bottomStatus
                val beanList = mPreview!!.template.pages[pageIndex].objects
                var index = 0
                while (index < mGroupView.childCount) {
                    val child = mGroupView.getChildAt(index)
                    if ((child.findViewWithTag("iv_border") as View).visibility == View.VISIBLE && (child.findViewWithTag(
                            "iv_scale"
                        ) as View).visibility == View.VISIBLE && (child.findViewWithTag("iv_delete") as View).visibility == View.VISIBLE
                    ) {
                        mGroupView.removeView(child)
                        var i = 0
                        while (i < beanList.size) {
                            if (beanList[i].id == child.getTag(R.id.tag_widget_view).toString().toInt()) {
                                mPreview!!.template.pages[pageIndex].objects.removeAt(i)
                            }
                            i++
                        }
                        refushRightView(pageIndex, mPreview!!.template.pages[pageIndex])
                        break
                    }
                    index++
                }
            }
            R.id.last_text_view -> {
                var index = 0
                while (index < mGroupView.childCount) {
                    val child = mGroupView.getChildAt(index)
                    if ((child.findViewWithTag("iv_border") as View).visibility == View.VISIBLE && (child.findViewWithTag(
                            "iv_scale"
                        ) as View).visibility == View.VISIBLE && (child.findViewWithTag("iv_delete") as View).visibility == View.VISIBLE
                    ) {
                        child.bringToFront()
                        Toast.makeText(baseContext, "移动完成", Toast.LENGTH_SHORT).show()
                        break
                    }
                    index++
                }
            }
            R.id.next_text_view -> {
                var index = 0
                while (index < mGroupView.childCount) {
                    val child = mGroupView.getChildAt(index)
                    if ((child.findViewWithTag("iv_border") as View).visibility == View.VISIBLE && (child.findViewWithTag(
                            "iv_scale"
                        ) as View).visibility == View.VISIBLE && (child.findViewWithTag("iv_delete") as View).visibility == View.VISIBLE
                    ) {
                        val childIndex = mGroupView.indexOfChild(child)
                        mGroupView.getChildAt(childIndex - 1).bringToFront()
                        Toast.makeText(baseContext, "移动完成", Toast.LENGTH_SHORT).show()
                        break
                    }
                    index++
                }
            }
            R.id.anno_text_view -> {
                setSelectStatu(anno_text_view)
                replaceFragment(mFragmentAnno)
            }
            R.id.right_edit_view -> if (right_material_view!!.isShown) {
                defaultViewBg(R.color.colorGray, false)
                right_material_view!!.visibility = View.GONE
                goneViewWidget(right_edit_view)
            } else {
                right_material_view!!.visibility = View.VISIBLE
            }
            R.id.float_bu_view -> if (float_view_layout!!.isShown) {
                float_view_layout!!.visibility = View.GONE
            } else {
                float_view_layout!!.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 获取右侧Item最大的Id
     */
    private val rightItemMaxId: Int
        private get() {
            val pageIndex = bottomStatus
            val beanList = mPreview!!.template.pages[pageIndex].objects
            val mBeanArray = arrayOfNulls<ObjectsBean>(beanList.size)
            for (i in beanList.indices) {
                mBeanArray[i] = beanList[i]
            }
            Arrays.sort(mBeanArray, BeanComparator())
            return mBeanArray[0]!!.id
        }

    /**
     * 将View创建为数组
     *
     * @param parent
     */
    private fun createViewArr(parent: ViewGroup) {
        val mViewBean = PreviewModel()
        val mTemplate = TemplateBean()
        val mPageArr: MutableList<PagesBean> = ArrayList()
        for (i in 0 until bottom_jpg_view!!.childCount - 1) {
            val pageBean = PagesBean()
            val tag = (bottom_jpg_view!!.getChildAt(i) as ViewGroup).getChildAt(0).tag
            val viewPage = right_edit_view!!.findViewWithTag<View>(tag)
            val beanArr: MutableList<ObjectsBean> = ArrayList()
            beanArr.add(getObjBean(viewPage, 0))
            for (viewIndex in 0 until (viewPage as ViewGroup).childCount) {
                beanArr.add(getObjBean(viewPage.getChildAt(viewIndex), 1))
            }
            pageBean.objects = beanArr
            mPageArr.add(pageBean)
        }
        mTemplate.pages = mPageArr
        mViewBean.template = mTemplate
        val msg = Message()
        msg.what = CREATE_GIF_FRAME_TAG
        msg.obj = mViewBean
        mHandler!!.sendMessage(msg)
        mDownload!!.show()
    }

    private fun threadHandler() {
        mHandlerThread = HandlerThread("my_handler")
        mHandlerThread!!.start()
        mHandler = object : Handler(mHandlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == CREATE_GIF_FRAME_TAG) {
                    for (file in File(path).listFiles()) {
                        file.delete()
                    }
                    CreateViewImg(msg.obj as PreviewModel)
                }
            }
        }
    }

    /**
     * 视图叠加
     *
     * @param model
     */
    private fun CreateViewImg(model: PreviewModel) {
        try {
            var name = 0
            val bean = model.template.pages
            val frameArr = frameProgress(bean)
            /**
             * 1、迭代每个图层
             * 2、以每个图层第一个视图为基础进行绘制
             * 3、取出每个图层1之后的视图，将其绘制在视图1上
             */
            for (i in bean.indices) {
                val pagesBean = bean[i]
                val replenishArr = replenishFrame(pagesBean.objects, frameArr[i])
                for (bgIndex in replenishArr[0]!!.indices) {
                    /**
                     * 更新ui
                     */
                    mainHandler.sendEmptyMessage(DIALOG_PROGRESS_TAG)
                    var bgFrame = replenishArr[0]!![bgIndex]
                    for (nextFrame in 1 until replenishArr.size) {
                        /**
                         * 取消时，直接退出
                         */
                        if (status == false) {
                            return
                        }
                        var nowImg = replenishArr[nextFrame]!![bgIndex]
                        bgFrame = mergeBitmap(bgFrame, nowImg, pagesBean.objects[nextFrame])
                        nowImg = null
                    }
                    val out = FileOutputStream(File(path, "" + autoGenericCode("000000", name) + ".jpg"))
                    bgFrame!!.compress(Bitmap.CompressFormat.JPEG, 80, out)
                    out.flush()
                    out.close()
                    name = name + 1
                    bgFrame = null
                    Log.d("TAG", "TAG")
                }
            }
            createVideo()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param code
     * @param num
     */
    fun autoGenericCode(code: String, num: Int): String {
        var result = ""
        result = String.format("%0" + code.length + "d", num + 1) //00014 . %05d
        return result
    }

    /**
     * 用帧计算进度
     *
     * @param bean
     * @return
     */
    private fun frameProgress(bean: List<PagesBean>): ArrayList<ArrayList<ArrayList<Bitmap?>?>?> {
        val frameArr: ArrayList<ArrayList<ArrayList<Bitmap?>?>?> = ArrayList<ArrayList<ArrayList<Bitmap?>?>?>()
        for (pagesBean in bean) {
            val allFrame = lowestCommonMultiple(pagesBean.objects)
            frameArr.add(allFrame)
            val lcmArr = IntArray(allFrame.size)
            for (i in allFrame.indices) {
                lcmArr[i] = allFrame[i]!!.size
            }
            all = all + MathUtils.nlcm(lcmArr)
        }
        return frameArr
    }

    /**
     * 创建视频动画
     */
    private fun createVideo() {
        Log.d("当前状态", "开始合并")
        mainHandler.sendEmptyMessage(DIALOG_PROGRESS_NEAR_TAG)
        val gifForImg = "-hide_banner -r 7 -i " + path + "/%06d.jpg " + path + "/" + gifName + ".gif"
        FFmpeg.executeAsync(gifForImg) { executionId: Long, returnCode: Int ->
            Log.d("当前状态", "合并状态：$returnCode")
            Config.printLastCommandOutput(Log.INFO)
            deleteFile()
            val msg = Message()
            msg.obj = path
            if (returnCode == 0) {
                msg.what = DIALOG_PROGRESS_OK_TAG
                Log.d("当前状态", "合并完成")
            } else {
                msg.what = DIALOG_PROGRESS_ERROR_TAG
                Log.d("当前状态", "合并失败")
            }
            mainHandler.sendMessage(msg)
        }
    }

    /**
     * 删除图片
     */
    private fun deleteFile() {
        Log.d("当前状态", "删除无用文件")
        for (file in File(path).listFiles()) {
            if (!file.name.contains(gifName)) {
                file.delete()
            }
        }
    }

    private fun mergeBitmap(firstBitmap: Bitmap?, secondBitmap: Bitmap?, objectsBean: ObjectsBean): Bitmap {
        val bgWidth = firstBitmap!!.width
        val bgHeight = firstBitmap.height
        val bitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRGB(255, 255, 255)
        val paint = Paint()
        paint.isAntiAlias = true
        val rect = Rect(0, 0, bgWidth, bgHeight)
        canvas.drawBitmap(firstBitmap, rect, rect, paint)
        val mWidth = objectsBean.width
        val mHeight = objectsBean.height
        val x = objectsBean.x
        val y = objectsBean.y
        val dst = Rect()
        dst.left = x
        dst.top = y
        dst.right = mWidth + x
        dst.bottom = mHeight + y
        if (objectsBean.font != null && objectsBean.font != "") {
            canvas.drawBitmap(drawTextView(objectsBean), null, dst, null)
        } else {
            canvas.rotate(objectsBean.angle)
            canvas.drawBitmap(secondBitmap!!, null, dst, null)
        }
        return bitmap
    }

    private fun drawTextView(objectsBean: ObjectsBean): Bitmap {
        val bitmap = Bitmap.createBitmap(objectsBean.width, objectsBean.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val content = objectsBean.text
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.color = Color.parseColor(objectsBean.color)
        paint.textSize = DensityUtils.sp2px(thisActivity, objectsBean.fontsize.toFloat()).toFloat()
        val centerX = ((objectsBean.width + objectsBean.x * 2) / 2).toFloat()
        val centerY = ((objectsBean.height + objectsBean.y * 2) / 2).toFloat()
        canvas.save()
        canvas.drawText(content, centerX, centerY, paint)
        canvas.restore()
        return bitmap
    }

    /**
     * 获取所有帧
     *
     * @param objects
     * @return
     */
    private fun lowestCommonMultiple(objects: List<ObjectsBean>): ArrayList<ArrayList<Bitmap?>?> {
        val frameArrs: ArrayList<ArrayList<Bitmap?>?> = ArrayList<ArrayList<Bitmap?>?>()
        for (`object` in objects) {
            val frameArr: ArrayList<Bitmap?> = ArrayList<Bitmap?>()
            if (`object`.img != null) {
                try {
                    val gifFromAssets = GifDrawable(`object`.img)
                    val totalCount = gifFromAssets.numberOfFrames
                    for (index in 0 until totalCount) {
                        frameArr.add(gifFromAssets.seekToFrameAndGet(index))
                    }
                    frameArrs.add(frameArr)
                } catch (e: IOException) {
                    if (e is GifIOException && e.reason.errorCode == 103) {
                        frameArr.add(BitmapFactory.decodeFile(`object`.img))
                        frameArrs.add(frameArr)
                    }
                }
            } else {
                val mBitmap = Bitmap.createBitmap(`object`.width, `object`.height, Bitmap.Config.ARGB_8888)
                mBitmap.eraseColor(Color.parseColor(`object`.color))
                frameArr.add(mBitmap)
                frameArrs.add(frameArr)
            }
        }
        return frameArrs
    }

    /**
     * 根据最小公倍数进行补帧
     *
     * @param objects
     * @param framsArr
     * @return
     */
    private fun replenishFrame(
        objects: List<ObjectsBean>,
        framsArr: ArrayList<ArrayList<Bitmap?>?>?
    ): ArrayList<ArrayList<Bitmap?>?> {
        val lcmArr = IntArray(framsArr!!.size)
        for (i in framsArr.indices) {
            lcmArr[i] = framsArr[i]!!.size
        }
        /**
         * 读取每一个图层
         */
        val lcm = MathUtils.nlcm(lcmArr)
        val framsImg = ArrayList<ArrayList<Bitmap?>?>()
        for (j in framsArr.indices) {
            var frameIndex = 0
            val frams = ArrayList<Bitmap?>()
            for (i in 0 until lcm - framsArr[j]!!.size) {
                frams.add(framsArr[j]!![frameIndex])
                frameIndex = frameIndex + 1
                if (frameIndex == framsArr[j]!!.size) {
                    frameIndex = 0
                }
            }
            val parentArr = framsArr[j]
            parentArr!!.addAll(frams)
            framsImg.add(parentArr)
        }
        return framsImg
    }

    /**
     * 得到 ObjectsBean
     *
     * @param view
     * @param status status==0:背景    status==1：顶层View
     * @return
     */
    private fun getObjBean(view: View, status: Int): ObjectsBean {
        val viewItem = ObjectsBean()
        if (view.getTag(R.id.tag_img_url) != null) {
            val url = UrlConfig.BASE_URL + view.getTag(R.id.tag_img_url) as String
            val cacheFile = GlideCache.getCacheFile(baseContext, url)
            if (cacheFile != null) {
                viewItem.img = cacheFile.absolutePath
            } else {
                viewItem.img = UrlConfig.BASE_URL + view.getTag(R.id.tag_img_url) as String
            }
        }
        if (status != 0) {
            viewItem.type = view.getTag(R.id.tag_view_type).toString().toInt()
            viewItem.x = view.getTag(R.id.tag_view_x).toString().toInt()
            viewItem.y = view.getTag(R.id.tag_view_y).toString().toInt()
            viewItem.id = view.getTag(R.id.tag_widget_view).toString().toInt()
        }
        if (view.background != null && view.background is ColorDrawable) {
            viewItem.color = String.format("#%X", (view.background as ColorDrawable).color)
        }
        if (view.getTag(R.id.tag_view_rotation) != null) {
            viewItem.angle = view.getTag(R.id.tag_view_rotation).toString().toFloat()
        } else {
            viewItem.angle = 0f
        }
        viewItem.density = resources.displayMetrics.scaledDensity
        viewItem.width = view.width
        viewItem.height = view.height
        if (view is StickerTextView) {
            viewItem.color = String.format("#%X", view.textColor)
            viewItem.text = view.text
            viewItem.fontsize = view.getTag(R.id.tag_text_size).toString()
            viewItem.font = view.getTag(R.id.tag_view_text_type).toString()
        }
        return viewItem
    }

    /**
     * 刷新底部img边框
     */
    private fun refreshbottomImg(view: View) {
        val parent = view.parent.parent as ViewGroup
        for (index in 0 until parent.childCount - 1) {
            parent.getChildAt(index).background = null
        }
        (view.parent as ViewGroup).background = resources.getDrawable(R.drawable.border_view)
    }

    private fun replaceFragment(fragment: Fragment?) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.material_view_id, fragment!!)
        transaction.commit()
    }

    /**
     * 素材View点击回调
     * 素材删除操作
     */
    private val mStickerIml: StickerViewIml = object : StickerViewIml {
        override fun onViewActionDown(view: View) {
            if (!right_material_view!!.isShown) {
                right_material_view!!.visibility = View.VISIBLE
            }
            defaultViewBg(R.color.colorBlack, true)
            setBorder(view)
        }

        override fun onDeleteView(view: View) {
            (view.parent.parent as ViewGroup).removeView(view.parent as View)
        }
    }

    /**
     * 获取底部状态
     */
    private val bottomStatus: Int
        private get() {
            for (i in 0 until bottom_jpg_view!!.childCount - 1) {
                if (bottom_jpg_view!!.getChildAt(i).background != null) {
                    return i
                }
            }
            return 0
        }

    /**
     * 设置边框
     *
     * @param view
     */
    fun setBorder(view: View) {
        val mParent = (view.parent as ViewGroup).parent as ViewGroup
        goneViewWidget(mParent)
        (view.findViewWithTag("iv_border") as View).visibility = View.VISIBLE
        (view.findViewWithTag("iv_scale") as View).visibility = View.VISIBLE
        (view.findViewWithTag("iv_delete") as View).visibility = View.VISIBLE
    }

    /**
     * 隐藏控件上的view
     */
    fun goneViewWidget(view: ViewGroup?) {
        val mTopView = view!!.getChildAt(view.childCount - 1) as ViewGroup
        for (index in 0 until mTopView.childCount) {
            val child = mTopView.getChildAt(index)
            (child.findViewWithTag("iv_border") as View).visibility = View.GONE
            (child.findViewWithTag("iv_scale") as View).visibility = View.GONE
            (child.findViewWithTag("iv_delete") as View).visibility = View.GONE
        }
    }

    /**
     * 改变按钮状态
     */
    private fun setSelectStatu(tv: View?) {
        select_text_view!!.setBackgroundColor(Color.WHITE)
        add_text_view!!.setBackgroundColor(Color.WHITE)
        change_text_view!!.setBackgroundColor(Color.WHITE)
        anno_text_view!!.setBackgroundColor(Color.WHITE)
        tv!!.setBackgroundColor(Color.GRAY)
    }

    /**
     * 测试数据
     */
    private fun initTestData() {
        val mData =
            "{\"msg\":\"成功！\",\"result\":\"01\",\"template\":{\"owner\":0,\"isbuy\":0,\"buyCount\":0,\"browseCount\":0,\"icon\":\"eb8becaaa39c440a9a3f98e8ce6be2f6\",\"description\":\"10\",\"createdate\":\"2017-03-21 16:37:13\",\"viewtype\":null,\"type\":5,\"tid\":84,\"pages\":[{\"objects\":[{\"img\":null,\"color\":\"#000000\",\"fontsize\":10,\"weight\":null,\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"oid\":580,\"type\":0,\"transparency\":0,\"width\":0,\"x\":0,\"y\":0,\"z\":50,\"text\":\"老坛酸菜牛肉面\",\"height\":643,\"font\":\"微软雅黑\",\"id\":123460,\"density\":10},{\"img\":\"123.jpg\",\"color\":null,\"fontsize\":null,\"weight\":null,\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"oid\":581,\"type\":1,\"transparency\":null,\"width\":300,\"x\":200,\"y\":100,\"z\":3,\"text\":null,\"height\":200,\"font\":null,\"id\":123457,\"density\":10,\"angle\":10},{\"img\":\"4.gif\",\"color\":null,\"fontsize\":null,\"weight\":null,\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"oid\":581,\"type\":2,\"transparency\":null,\"width\":100,\"x\":200,\"y\":300,\"z\":8,\"text\":null,\"height\":200,\"font\":null,\"id\":123458,\"density\":10,\"angle\":40}],\"icon\":\"123.jpg\",\"description\":\"0\",\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"type\":5,\"tid\":84,\"editZ\":0,\"price\":0,\"pageno\":1,\"name\":\"0\",\"width\":1280,\"height\":643,\"group\":null},{\"objects\":[{\"img\":null,\"color\":\"#000000\",\"fontsize\":10,\"weight\":null,\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"oid\":580,\"type\":0,\"transparency\":0,\"width\":0,\"x\":0,\"y\":0,\"z\":50,\"text\":\"老长沙臭豆腐\",\"height\":643,\"font\":\"微软雅黑\",\"id\":1234569,\"density\":10},{\"img\":\"123.jpg\",\"color\":null,\"fontsize\":null,\"weight\":null,\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"oid\":581,\"type\":1,\"transparency\":null,\"width\":100,\"x\":200,\"y\":100,\"z\":3,\"text\":null,\"height\":100,\"font\":null,\"id\":1234570,\"density\":10},{\"img\":\"4.gif\",\"color\":null,\"fontsize\":null,\"weight\":null,\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"oid\":581,\"type\":2,\"transparency\":null,\"width\":200,\"x\":300,\"y\":100,\"z\":8,\"text\":null,\"height\":200,\"font\":null,\"id\":1234571,\"density\":10}],\"icon\":\"1.jpg\",\"description\":\"0\",\"createdate\":\"2017-03-21 16:37:13\",\"pid\":148,\"type\":5,\"tid\":84,\"editZ\":0,\"price\":0,\"pageno\":1,\"name\":\"0\",\"width\":1280,\"height\":643,\"group\":null}],\"price\":10,\"name\":\"10\",\"width\":1280,\"listtype\":null,\"useCount\":0,\"height\":643,\"order\":0,\"status\":0}}"
        val mGson = Gson()
        mPreview = mGson.fromJson(mData, PreviewModel::class.java)
    }

    companion object {
        private const val path = "/storage/emulated/0/Android/data/com.poster/files/imgframe/"
        private const val gifName = "output"
        private var mDownload: DownloadDialog? = null
        private var thisActivity: Activity? = null
        private const val CREATE_GIF_FRAME_TAG = 200
        private const val DIALOG_PROGRESS_TAG = 201 //加载进度条
        private const val DIALOG_PROGRESS_NEAR_TAG = 202 //快要完成
        private const val DIALOG_PROGRESS_OK_TAG = 203 //完成
        private const val DIALOG_PROGRESS_ERROR_TAG = 204 //失败
        private var all = 0 //所有帧数，用于计算进度
        private var current = 0f //当前帧数
        private var status = true

        /**
         * 主线程handler,更新进度条
         */
        var num = 0
        private val mainHandler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    DIALOG_PROGRESS_TAG -> {
                        val value = current / (all + 1)
                        num = (value * 100).toInt()
                        mDownload!!.updateProgress(num)
                        current = current + 1
                    }
                    DIALOG_PROGRESS_NEAR_TAG -> {
                        num = 99
                        mDownload!!.updateProgress(num)
                    }
                    DIALOG_PROGRESS_ERROR_TAG -> {
                        Toast.makeText(thisActivity, "文件创建失败", Toast.LENGTH_SHORT).show()
                        num = 0
                        mDownload!!.updateProgress(num)
                        mDownload!!.dismiss()
                    }
                    DIALOG_PROGRESS_OK_TAG -> {
                        Log.d("当前状态", "文件已保存")
                        num = 100
                        val imgPath = msg.obj.toString()
                        mDownload!!.updateProgress(num)
                        mDownload!!.dismiss()
                        Toast.makeText(thisActivity, "文件保存至：$imgPath", Toast.LENGTH_SHORT).show()
                        val mIntent = Intent(thisActivity, GifActivity::class.java)
                        mIntent.putExtra("uri", path + "/" + gifName + ".gif")
                        thisActivity!!.startActivity(mIntent)
                    }
                }
            }
        }
    }
}