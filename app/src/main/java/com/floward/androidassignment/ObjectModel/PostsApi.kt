package com.floward.androidassignment.ObjectModel

class PostsApiResp : ArrayList<PostsApiRespItem>()

data class PostsApiRespItem(
        val body: String = "",
        val id: Int = 0,
        val title: String = "",
        val userId: Int = 0
)