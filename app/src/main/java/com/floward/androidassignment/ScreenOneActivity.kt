package com.floward.androidassignment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.floward.androidassignment.ApiServices.RetrofitClient
import com.floward.androidassignment.Application.AssignmentApp
import com.floward.androidassignment.ObjectModel.PostsApiResp
import com.floward.androidassignment.ObjectModel.PostsApiRespItem
import com.floward.androidassignment.ObjectModel.UsersApiResp
import com.floward.androidassignment.ObjectModel.UsersApiRespItem
import com.floward.androidassignment.Util.AppConstants
import com.floward.androidassignment.Util.isNetworkConnected
import com.floward.androidassignment.Util.toast
import com.floward.androidassignment.Util.viewBinding
import com.floward.androidassignment.databinding.ActivityScreenOneBinding
import com.floward.androidassignment.databinding.RowUsersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScreenOneActivity : AppCompatActivity() {

    private val bindingView by viewBinding(ActivityScreenOneBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(bindingView.root)
        init()
    }

    private fun init() {

        doApiPosts()

    }

    private fun doApiPosts() {
        if (!isNetworkConnected(applicationContext)) {
            toast(applicationContext, getString(R.string.NoInternet))
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            bindingView.progressBarApi.visibility = View.VISIBLE
        }, AppConstants.PROGRESSBAR_API)

        RetrofitClient.apiFuntions!!.posts().enqueue(object : Callback<PostsApiResp> {

            override fun onResponse(call: Call<PostsApiResp>, response: Response<PostsApiResp>) {
                if (response.isSuccessful) {
                    val apiResp: PostsApiResp? = response.body()
                    if (apiResp != null) {
                        val list_Posts_Item_Api = apiResp as ArrayList<PostsApiRespItem>
                        if (list_Posts_Item_Api != null && list_Posts_Item_Api.size > 0) {
                            AssignmentApp.listPostsItem.clear()
                            AssignmentApp.listPostsItem.addAll(list_Posts_Item_Api)
                            doApiUsers()
                        }
                    }
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    bindingView.progressBarApi.visibility = View.VISIBLE
                }, AppConstants.PROGRESSBAR_API)
            }

            override fun onFailure(call: Call<PostsApiResp>, t: Throwable) {
                t.printStackTrace()
                Handler(Looper.getMainLooper()).postDelayed({
                    bindingView.progressBarApi.visibility = View.GONE
                    toast(applicationContext, getString(R.string.ApiFail))
                }, AppConstants.PROGRESSBAR_API)
            }
        })

    }

    private fun doApiUsers() {
        if (!isNetworkConnected(applicationContext)) {
            toast(applicationContext, getString(R.string.NoInternet))
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            bindingView.progressBarApi.visibility = View.VISIBLE
        }, AppConstants.PROGRESSBAR_API)

        RetrofitClient.apiFuntions!!.users().enqueue(object : Callback<UsersApiResp> {

            override fun onResponse(call: Call<UsersApiResp>, response: Response<UsersApiResp>) {
                if (response.isSuccessful) {
                    val apiResp: UsersApiResp? = response.body()
                    if (apiResp != null) {
                        val listUsers = apiResp as ArrayList<UsersApiRespItem>
                        if (listUsers != null && listUsers.size > 0) {

                            for (tempUsers in listUsers) {
                                for (tempPosts in AssignmentApp.listPostsItem) {
                                    if (tempUsers.userId == tempPosts.userId) {
                                        tempUsers.postsCount++
                                    }
                                }
                            }

                            bindingView.rvUsers.isNestedScrollingEnabled = false
                            bindingView.rvUsers.setHasFixedSize(true)
                            val lytManager = LinearLayoutManager(applicationContext)
                            lytManager.orientation = LinearLayoutManager.VERTICAL
                            bindingView.rvUsers.layoutManager = lytManager

                            val adpRatReason = AdapterUsers(listUsers)
                            bindingView.rvUsers.adapter = adpRatReason

                        } else {
                            toast(applicationContext, getString(R.string.NoDataFound))
                        }

                    } else {
                        toast(applicationContext, getString(R.string.ApiFail))
                    }
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    bindingView.progressBarApi.visibility = View.GONE
                }, AppConstants.PROGRESSBAR_API)
            }

            override fun onFailure(call: Call<UsersApiResp>, t: Throwable) {
                t.printStackTrace()
                Handler(Looper.getMainLooper()).postDelayed({
                    bindingView.progressBarApi.visibility = View.GONE
                    toast(applicationContext, getString(R.string.ApiFail))
                }, AppConstants.PROGRESSBAR_API)
            }
        })

    }

    inner class AdapterUsers(private val listUsers: ArrayList<UsersApiRespItem>) :
            RecyclerView.Adapter<AdapterUsers.ViewHolderItem>() {

        override fun getItemCount(): Int {
            return listUsers.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
            val binding = RowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolderItem(binding)
        }

        override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
            val dataResult: UsersApiRespItem = listUsers[position]
            holder.bind(dataResult, position)
        }

        inner class ViewHolderItem(private val bindingRow: RowUsersBinding) : RecyclerView.ViewHolder(bindingRow.root) {

            @SuppressLint("NotifyDataSetChanged")
            fun bind(dataResult: UsersApiRespItem, position: Int) {
                with(bindingRow) {

                    tvUsers.text = dataResult.name
                    tvUsersPostsCount.text = dataResult.postsCount.toString()

                    Glide.with(applicationContext).load(dataResult.thumbnailUrl)
                            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.vector_person)
                                    .dontAnimate()
                                    .error(R.drawable.vector_person))
//                            .thumbnail(0.1f)
                            .into(ivUsers)

                    viewRowUsers.setOnClickListener {
                        Intent(applicationContext, ScreenTwoActivity::class.java).apply {
                            putExtra("dataResult", dataResult)
                            startActivity(this)
                        }
                    }

                }
            }

        }
    }
}
