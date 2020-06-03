package com.hxz.banner.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import androidx.annotation.DrawableRes

/**
 * @title com.hxz.banner.utils  GankIO
 * @author xian_zhong  admin
 * @version 1.0
 * @Des BannerUtils
 * @DATE 2020/5/30  14:50 星期六
 */

typealias PageClickListener = (position: Int) -> Unit

internal object BannerUtils {

        const val TOP = 0X01
        const val BOTTOM = 0X02
        const val CENTER = 0X04
        const val LEFT = 0X08
        const val RIGHT = 0X10

        const val DEFAULT_POINT_SIZE = 20

        const val MAX_VALUE_SIZE = 500

        fun createStateListDrawable(unSelect: Drawable,select: Drawable) : Drawable {
            val listDrawable = StateListDrawable()
            listDrawable.addState(intArrayOf(android.R.attr.state_selected),select)
            listDrawable.addState(intArrayOf(-android.R.attr.state_selected),unSelect)
            return listDrawable
        }

        fun createColorListDrawable(unSelect: Int,select: Int) : Drawable = createStateListDrawable(
            createDrawable(unSelect, DEFAULT_POINT_SIZE), createDrawable(select, DEFAULT_POINT_SIZE))

        fun createDrawable(color: Int,pointSize: Int) : Drawable{
            val drawable = GradientDrawable()
            drawable.setColor(color)
            drawable.setBounds(0,0, pointSize, pointSize)
            drawable.shape = GradientDrawable.OVAL
            drawable.cornerRadius = (pointSize / 2).toFloat()
            drawable.setSize(pointSize,pointSize)
            return drawable
        }

        fun getGravity(gravity: Int) : Int {
            var g = 0
            if (gravity and LEFT != 0) {
                g = g or Gravity.LEFT
            }
            if (gravity and RIGHT != 0) {
                g = g or Gravity.RIGHT
            }
            if (gravity and CENTER != 0) {
                g = g or Gravity.CENTER
            }
            if (gravity and TOP != 0) {
                g = g or Gravity.TOP
            }
            if (gravity and BOTTOM != 0) {
                g = g or Gravity.BOTTOM
            }
            return g
        }

        fun getRealPosition(position: Int,size: Int) : Int {
            return if (size <= 0) 0 else position % size
        }
}