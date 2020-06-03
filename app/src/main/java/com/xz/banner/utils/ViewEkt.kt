package com.xz.banner.utils

import android.graphics.Color
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hxz.banner.Banner
import com.hxz.banner.adapter.BaseBannerAdapter

/**
 * @title com.xz.banner.utils  Banner
 * @author xian_zhong  admin
 * @version 1.0
 * @Des ViewEkt
 * @DATE 2020/6/1  10:37 星期一
 */

fun<DATA> Banner.initPage(adapter: BaseBannerAdapter<DATA>,transforme: ViewPager2.PageTransformer? = null,isGallery: Boolean = false,autoPlay: Boolean = false) {
//    setCanLoop(true)
    setAdapter(adapter)
//    setInDecorateColor(Color.BLACK,Color.RED)
    if (transforme != null) setPagerTransform(transforme)
    if (isGallery) setGallery(60,2)
//    setAutoPlay(autoPlay)
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).setDefaultRequestOptions(RequestOptions.centerCropTransform()).load(url).into(this)
}