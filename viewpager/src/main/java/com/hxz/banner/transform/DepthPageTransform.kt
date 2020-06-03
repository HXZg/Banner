package com.hxz.banner.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class DepthPageTransform : ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.75F

    override fun transformPage(page: View, position: Float) {
        when{
            position < -1 -> {
                page.alpha = 0f
            }
            position <= 0 -> {
                page.apply {
                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
            }
            position <= 1 -> {
                page.apply {
                    alpha = 1 - position
                    translationX = width * -position
                    val scale = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))
                    scaleX = scale
                    scaleY = scale
                }
            }
            else -> page.alpha = 0f
        }
    }
}