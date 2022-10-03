package com.floward.androidassignment.ApiServices

import com.floward.androidassignment.ObjectModel.PostsApiResp
import com.floward.androidassignment.ObjectModel.UsersApiResp
import retrofit2.Call
import retrofit2.http.GET

interface ApiFunctions {
    //puthawala
    @GET("users")
    fun users(): Call<UsersApiResp>

    @GET("posts")
    fun posts(): Call<PostsApiResp>

}