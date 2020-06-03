package com.hxz.banner.transform

import android.os.Build
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

/**
 * 画廊效果 transform  就是再两边得item view 绘制出来，并且进行相应得缩放，需要得话，可以加上alpha 或 rotate 变化
 */
class GalleryPageTransform(private val pageMargin: Int) : ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.7f

    override fun transformPage(page: View, position: Float) {
        val offset = position * pageMargin
        // 如果布局方向为RTL  则为 -offset
        page.translationX = offset           // viewpager2  中没有设置pagerMargin 得方法  用这个方法设置view 得偏移量，作为page 得间距
//        val scale = max(MIN_SCALE,1- abs(position))
//        val rotate = 10 * abs(position)
        when{
            position <= -1 -> {  // page 已滑至最左边
                page.apply {
//                    scaleX = MIN_SCALE
                    scaleY = MIN_SCALE
//                    rotationY = rotate
                }
            }
            position < 0 -> {  // page 逐渐向左滑动  position : -1 -> 0 scale : 0.8 -> 1  position 为0 则滑至中间位置
                page.apply {
//                    scaleX = scale
                    scaleY = MIN_SCALE + (1 + position) * (1 - MIN_SCALE)
//                    rotationY = rotate
                }
            }
            position < 1 -> {  // page 逐渐向右滑动  0 -> 1   1 -> 0.8
                page.apply {
//                    scaleX = scale
                    scaleY = MIN_SCALE + (1 - position) * (1 - MIN_SCALE)
//                    rotationY = -rotate
                }
            }
            else -> {  // page 位于最右边
                page.apply {
//                    scaleX = scale
                    scaleY = MIN_SCALE
//                    rotationY = -rotate
                }
            }
        }
    }
}