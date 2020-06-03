package com.xz.banner

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xz.banner.http.ArticleListBean
import com.xz.banner.http.RetrofitManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * @title com.xz.banner  Banner
 * @author xian_zhong  admin
 * @version 1.0
 * @Des MainViewModel
 * @DATE 2020/6/1  10:24 星期一
 */
class MainViewModel : ViewModel() {

    val dataLive = MutableLiveData<ArrayList<ArticleListBean>>()

    fun getImages() {
        viewModelScope.launch {
            try {
                val bean = async { RetrofitManager.getImages() }
                val data = bean.await()
                Log.i("main_activity","${data.page_count},,${data.isSuccess()},,${data.data}")
                dataLive.postValue(data.data)
            }catch (e : Exception) {
                dataLive.postValue(null)
            }
        }
    }
}