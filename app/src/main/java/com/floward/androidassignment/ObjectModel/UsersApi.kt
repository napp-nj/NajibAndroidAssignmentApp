package com.floward.androidassignment.ObjectModel

class UsersApiResp : ArrayList<UsersApiRespItem>()

data class UsersApiRespItem(
        val albumId: Int = 0,
        val name: String = "",
        val thumbnailUrl: String = "",
        val url: String = "",
        var postsCount: Int = 0,
        val userId: Int = 0
) : java.io.Serializable
