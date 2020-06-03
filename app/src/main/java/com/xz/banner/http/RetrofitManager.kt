package com.xz.banner.http

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @title com.xz.banner.http  Banner
 * @author xian_zhong  admin
 * @version 1.0
 * @Des RetrofitManager
 * @DATE 2020/6/1  10:19 星期一
 */
object RetrofitManager {

    private val mRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://gank.io/api/v2/")
            .client(OkHttpClient.Builder()
                .connectTimeout(30L,TimeUnit.SECONDS)
                .readTimeout(30L,TimeUnit.SECONDS)
                .addInterceptor {
                    Log.i("main_activity",it.request().url().toString())
                    it.proceed(it.request())
                }
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api by lazy { mRetrofit.create(GankApi::class.java) }

    suspend fun getImages() = api.getArticleList("Girl","Girl",1,10)

    suspend fun getBanner() = api.getBanners()
}