package com.xz.banner

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.xz.banner.utils.loadUrl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.hxz.banner.adapter.BaseBannerAdapter
import com.hxz.banner.transform.DepthPageTransform
import com.hxz.banner.transform.GalleryPageTransform
import com.hxz.banner.transform.ZoomOutPageTransform
import com.xz.banner.http.ArticleListBean
import com.xz.banner.utils.initPage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = object : BaseBannerAdapter<ArticleListBean>() {
        override fun createView(parent: ViewGroup, viewType: Int): View {
            return ImageView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(-1,-1)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        override fun convertHolder(holder: RecyclerView.ViewHolder, item: ArticleListBean) {
            (holder.itemView as ImageView).loadUrl(item.url)
        }
    }

    private val viewmodel by lazy {
        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bn_main_zoom.initPage(adapter,ZoomOutPageTransform())
        bn_main_gallery.initPage(adapter,GalleryPageTransform(20),true)
        bn_main_depth.initPage(adapter,DepthPageTransform())  // adapter 的 itemCount 被 canLoop 影响  共用一个adapter itemCount 返回值被覆盖
        viewmodel.getImages()
        initObserver()
    }

    private fun initObserver() {
        viewmodel.dataLive.observe(this, Observer {
            bn_main_rv.setAdapter(object : BaseBannerAdapter<ArticleListBean>(it) { // 这个 adapter 的 item count 返回真实数据
                override fun createView(parent: ViewGroup, viewType: Int): View {
                    return ImageView(parent.context).apply {
                        layoutParams = ViewGroup.LayoutParams(-1,-1)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                }

                override fun convertHolder(holder: RecyclerView.ViewHolder, item: ArticleListBean) {
                    (holder.itemView as ImageView).loadUrl(item.url)
                }

                override fun getItemCount(): Int = it.size
            })
            (bn_main_zoom).refreshData(it)
            (bn_main_gallery).refreshData(it)
            (bn_main_depth).refreshData(it)
        })
    }
}
