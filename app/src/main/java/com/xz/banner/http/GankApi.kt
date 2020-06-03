package com.xz.banner.http

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @title com.xz.banner.http  Banner
 * @author xian_zhong  admin
 * @version 1.0
 * @Des GankApi
 * @DATE 2020/6/1  10:17 星期一
 */
interface GankApi {

    /**
     * 获取分类下的数据
     * article 可接受参数 All(所有分类) | Article | GanHuo | Girl
    type 可接受参数 All(全部类型) | Android | iOS | Flutter | Girl ...，即分类API返回的类型数据
    count: [10, 50]
    page: >=1
     */
    @GET("data/category/{category}/type/{type}/page/{page}/count/{count}")
    suspend fun getArticleList(@Path("category") category: String, @Path("type") type: String,
                               @Path("page") page: Int, @Path("count") count: Int) :
            BaseResponseBean<ArrayList<ArticleListBean>>

    /**
     * 首页轮播
     */
    @GET("banners")
    suspend fun getBanners() : BaseResponseBean<ArrayList<BannerBean>>


}