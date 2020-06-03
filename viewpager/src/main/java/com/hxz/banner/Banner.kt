package com.hxz.banner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.viewpager2.widget.ViewPager2
import com.hxz.banner.adapter.BaseBannerAdapter
import com.hxz.banner.utils.BannerUtils
import com.hxz.banner.utils.PageClickListener

class Banner @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, style: Int = 0) :
    FrameLayout(context,attributes,style) {

    var pointSize = BannerUtils.DEFAULT_POINT_SIZE

    private var isCanLoop = false

    private var isAutoPlay = false

    private var clickListener: PageClickListener? = null

    private val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            pageChangeCallback?.onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            pageChangeCallback?.onPageScrolled(BannerUtils.getRealPosition(position,mBannerSize),positionOffset,positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val realPosition = BannerUtils.getRealPosition(position,mBannerSize)
            if (position == 0 || position == BannerUtils.MAX_VALUE_SIZE - 1) {
                resetCurrentItem(realPosition)
            }
            if (inDecorateViews.isNotEmpty()) {
                inDecorateViews[currentPosition].isSelected = false
                inDecorateViews[realPosition].isSelected = true
            }
            currentPosition = realPosition
            pageChangeCallback?.onPageSelected(realPosition)
        }
    }

    private val loopRun = Runnable {
        var item = mViewPager.currentItem
        if (++item < adapter.itemCount) {
            mViewPager.setCurrentItem(item,true)
            delayPostLoop()
        }
    }

    val mViewPager by lazy { ViewPager2(context) }

    private var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    private lateinit var adapter : BaseBannerAdapter<*>

    private var AUTO_PLAY_TIME = 2000

    private var isStartLoop = false

    private val inDecorateViews = arrayListOf<View>()

    private var currentPosition = 0

    private var mBannerSize = 0

    // 指示器
    private var inDecorateOrientation = LinearLayout.HORIZONTAL
    private var inDecorateGravity = Gravity.BOTTOM or Gravity.CENTER
    private var unSelectDrawable: Drawable? = null
    private var selectDrawable: Drawable? = null
    private var inDecorateRgMargin = 0
    private var inDecorateLgMargin = 0
    private var inDecorateTopMargin = 0
    private var inDecorateBtMargin = 10
    private var decorateRgMargin = 20

    init {
        initViewPager()
        if (attributes != null) initAttribute(context,attributes,0)
    }

    private fun initViewPager() {
        mViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val params = LayoutParams(-1,-1)  // 完全填充父布局
        addView(mViewPager,params)
    }

    @SuppressLint("ResourceType")
    private fun initAttribute(context: Context, attributes: AttributeSet, style: Int) {
        val type = context.obtainStyledAttributes(attributes, R.styleable.Banner,style,0)
        isAutoPlay = type.getBoolean(R.styleable.Banner_banner_autoPlaying,false)
        AUTO_PLAY_TIME = type.getInt(R.styleable.Banner_banner_time, AUTO_PLAY_TIME)
        isCanLoop = type.getBoolean(R.styleable.Banner_banner_canLoop,false)
        pointSize = type.getDimensionPixelSize(R.styleable.Banner_banner_indicatorSize,pointSize)
        selectDrawable = type.getDrawable(R.styleable.Banner_banner_SelectedSrc)
        unSelectDrawable = type.getDrawable(R.styleable.Banner_banner_UnselectedSrc)
        val sc = type.getColor(R.styleable.Banner_banner_SelectedColor,0)
        val usc = type.getColor(R.styleable.Banner_banner_UnselectedColor,0)
        if (selectDrawable == null && sc != 0) {
            selectDrawable = BannerUtils.createDrawable(sc,pointSize)
        }
        if (unSelectDrawable == null && usc != 0) {
            unSelectDrawable = BannerUtils.createDrawable(usc,pointSize)
        }

        decorateRgMargin = type.getDimensionPixelSize(R.styleable.Banner_banner_indicatorSpace,decorateRgMargin)
        inDecorateOrientation = type.getInt(R.styleable.Banner_banner_idOrientation,0)
        val gravity = type.getInt(R.styleable.Banner_banner_indicatorGravity,BannerUtils.BOTTOM or BannerUtils.CENTER)
        inDecorateGravity = BannerUtils.getGravity(gravity)
        inDecorateRgMargin = type.getDimensionPixelSize(R.styleable.Banner_banner_MarginRight,inDecorateRgMargin)
        inDecorateBtMargin = type.getDimensionPixelSize(R.styleable.Banner_banner_MarginBottom,inDecorateBtMargin)
        inDecorateTopMargin = type.getDimensionPixelSize(R.styleable.Banner_banner_MarginTop,inDecorateTopMargin)
        inDecorateLgMargin = type.getDimensionPixelSize(R.styleable.Banner_banner_MarginTop,inDecorateLgMargin)

        type.recycle()
    }

    /**
     * @param orientation 0: HORIZONTAL 1: VERTICAL
     * @param gravity  Gravity.LEFT/RIGHT/BOTTOM/TOP
     */
    private fun initInDecoration(drawables: Array<Drawable>) {  // 指示器
        val inLinear = LinearLayout(context)
        inLinear.orientation = inDecorateOrientation
        val params = LayoutParams(-2,-2).apply {
            this.gravity = inDecorateGravity
            bottomMargin = inDecorateBtMargin
            topMargin = inDecorateTopMargin
            leftMargin = inDecorateLgMargin
            rightMargin = inDecorateRgMargin
        } // 自适应布局
        addInDecorateIv(inLinear,drawables)
        if (inDecorateViews.size > currentPosition) inDecorateViews[currentPosition].isSelected = true
        addView(inLinear,params)
    }

    private fun addInDecorateIv(ll: LinearLayout,drawable: Array<Drawable>) {
        drawable.forEach {
            val iv = ImageView(context)
            iv.background = it
            val params = LinearLayout.LayoutParams(-2,-2).apply {
                rightMargin = decorateRgMargin
            }
            inDecorateViews.add(iv)
            ll.addView(iv,params)
        }
    }

    fun setPageClickListener(listener: PageClickListener) {
        clickListener = listener
        if (::adapter.isInitialized) {
            adapter.clickListener = listener
        }
    }

    fun setPageChangeCallback(listener: ViewPager2.OnPageChangeCallback) {
        this.pageChangeCallback = listener
    }

    fun setAdapter(adapter: BaseBannerAdapter<*>) {
        if (clickListener != null) adapter.clickListener = clickListener
        adapter.isCanLoop = isCanLoop
        this.adapter = adapter
        mViewPager.adapter = adapter
        mBannerSize = adapter.dataList.size
        initViewPagerChange()
    }

    fun<T> refreshData(list: MutableList<T>) {
        if (::adapter.isInitialized) {
            (adapter as BaseBannerAdapter<T>).setData(list)
            mBannerSize = list.size
            initViewPagerChange()
            if (isAutoPlay) startLoop()
        }
    }

    private fun initViewPagerChange() {
        resetCurrentItem(0)
        mViewPager.unregisterOnPageChangeCallback(pageChangeListener)
        mViewPager.registerOnPageChangeCallback(pageChangeListener)

        if (unSelectDrawable != null && selectDrawable != null) {
            setInDecorate(drawables = Array(mBannerSize){BannerUtils.createStateListDrawable(unSelectDrawable!!,selectDrawable!!)})
        }
    }

    fun setInDecorateParams(orientation: Int = LinearLayout.HORIZONTAL, gravity: Int = Gravity.BOTTOM, rightMargin: Int = 0, leftMargin: Int = 0, topMargin: Int = 0, bottomMargin: Int = 10,margin: Int = 20) {
        inDecorateOrientation = orientation
        inDecorateGravity = gravity
        inDecorateRgMargin = rightMargin
        inDecorateLgMargin = leftMargin
        inDecorateTopMargin = topMargin
        inDecorateBtMargin = bottomMargin
        decorateRgMargin = margin
    }

    fun setInDecorate(drawables: Array<Drawable>) {
        initInDecoration(drawables)
    }

    fun setInDecorate(@DrawableRes unSelect: Int,@DrawableRes select: Int) {
        unSelectDrawable = resources.getDrawable(unSelect)
        selectDrawable = resources.getDrawable(select)
        if (mBannerSize > 0 ) {
            val drawables = Array(mBannerSize){BannerUtils.createStateListDrawable(unSelectDrawable!!,selectDrawable!!)}
            setInDecorate(drawables = drawables)
        }
    }

    fun setInDecorateColor(unSelectColor: Int,selectColor: Int) {
        unSelectDrawable = BannerUtils.createDrawable(unSelectColor,pointSize)
        selectDrawable = BannerUtils.createDrawable(selectColor,pointSize)
        if (mBannerSize > 0) {
            val drawables = Array(mBannerSize){BannerUtils.createStateListDrawable(unSelectDrawable!!,selectDrawable!!)}
            setInDecorate(drawables = drawables)
        }
    }

    fun setPagerTransform(transform: ViewPager2.PageTransformer) {
        mViewPager.setPageTransformer(transform)
    }

    fun setCanLoop(loop: Boolean) {
        isCanLoop = loop
        if (::adapter.isInitialized) {
            adapter.isCanLoop = loop
            adapter.notifyDataSetChanged()
        }
    }

    fun setGallery(margin: Int,limit: Int = 2) {
        clipChildren = false
        mViewPager.apply {
            offscreenPageLimit = limit
            clipChildren = false
            (layoutParams as LayoutParams).apply {
                leftMargin = margin
                rightMargin = margin
            }
        }
    }

    fun setAutoPlay(auto: Boolean) {
        isAutoPlay = auto
        if (auto) startLoop()
    }

    fun startLoop(time: Int = AUTO_PLAY_TIME) {
        if (!isStartLoop && isAutoPlay && ::adapter.isInitialized) {
            isStartLoop = true
            AUTO_PLAY_TIME = time
            delayPostLoop()
        }
    }

    private fun delayPostLoop() {
        postDelayed(loopRun,AUTO_PLAY_TIME.toLong())
    }

    fun stopLoop() {
        if (isStartLoop) {
            isStartLoop = false
            removeCallbacks(loopRun)
        }
    }

    override fun onAttachedToWindow() {
        startLoop()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        stopLoop()
        super.onDetachedFromWindow()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.VISIBLE) {
            startLoop()
        } else {
            stopLoop()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                isStartLoop = true
                stopLoop()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                isStartLoop = false
                startLoop()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun resetCurrentItem(item: Int) {
        mViewPager.setCurrentItem(getResetItem(item,adapter.dataList.size),false)
    }

    private fun getResetItem(item: Int, pageSize: Int) : Int {  // 轮播从中间部分开始
        return if (!isCanLoop || pageSize <=  0) item
        else BannerUtils.MAX_VALUE_SIZE / 2 - BannerUtils.MAX_VALUE_SIZE / 2 % pageSize + item
    }

}