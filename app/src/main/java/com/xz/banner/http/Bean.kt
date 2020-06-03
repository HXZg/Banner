package com.xz.banner.http

/**
 * @title com.xz.banner.http  Banner
 * @author xian_zhong  admin
 * @version 1.0
 * @Des Bean
 * @DATE 2020/6/1  10:18 星期一
 */

class BaseResponseBean<T> {

    companion object {
        private const val SUCCESS_STATUS = 100
        private const val CUSTOM_ERROR = -1

        fun<T> customError(e: Exception) : BaseResponseBean<T> {
            return BaseResponseBean<T>().apply {
                status = CUSTOM_ERROR
                errorMsg = e.message ?: ""
            }
        }
    }

    var status: Int = 0  // 100 success
    var errorMsg: String = ""

    var data: T? = null  // data 数据位

    var page: Int = 0
    var page_count : Int = 0
    var total_counts : Int = 0

    fun isSuccess() = status == SUCCESS_STATUS

    fun isCustomError() = status == CUSTOM_ERROR
}

// 文章列表
data class ArticleListBean(val _id: String,val author: String,val category: String,val createdAt: String,val desc: String,
                           val images: List<String>,val likeCounts: Int,val publishedAt: String,val stars: Int,val title: String,
                           val type: String,val url: String,val views: Int)

data class BannerBean(val image: String,val title: String,val url: String)