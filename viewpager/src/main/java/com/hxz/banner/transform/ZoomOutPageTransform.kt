package com.hxz.banner.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

/**
 * [-∞ , -1)  :
     表示左边 的View 且已经看不到了
[-1 ,   0]  :
     表示左边的 View ,且可以看见
( 0 ,   1]  :
     表示右边的VIew , 且可以看见了
( 1 , -∞)  :     
     表示右边的 View 且已经看不见了
 */
class ZoomOutPageTransform :  ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f

    override fun transformPage(page: View, position: Float) {
        when{
            position < -1f -> {
                page.alpha = 0f
            }
            position <= 1f -> {
                val scale = max(MIN_SCALE, 1 - abs(position))
                val verMargin = page.height * (1 - scale) / 2
                val horMargin = page.width * (1 - scale) / 2

                if (position < 0) {
                    page.translationX = horMargin - verMargin / 2
                } else {
                    page.translationX = -horMargin + verMargin / 2
                }

                page.scaleX = scale
                page.scaleY = scale

                page.alpha = MIN_ALPHA + (scale - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            }
            else -> {
                page.alpha = 0f
            }
        }
    }
}