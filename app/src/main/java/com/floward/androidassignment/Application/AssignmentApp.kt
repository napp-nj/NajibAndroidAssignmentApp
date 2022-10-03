package com.floward.androidassignment.Application

import android.app.Application
import com.floward.androidassignment.ApiServices.RetrofitClient
import com.floward.androidassignment.ObjectModel.PostsApiRespItem
import com.floward.androidassignment.Util.glideCacheClear

class AssignmentApp : Application() {


    companion object {
        lateinit var instance: AssignmentApp
        val listPostsItem: ArrayList<PostsApiRespItem> = arrayListOf()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        RetrofitClient.apiFuntions = RetrofitClient.getClient()

        glideCacheClear(applicationContext)

    }

}
