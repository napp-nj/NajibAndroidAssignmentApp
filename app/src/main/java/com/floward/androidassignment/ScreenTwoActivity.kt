package com.floward.androidassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.floward.androidassignment.Application.AssignmentApp
import com.floward.androidassignment.ObjectModel.PostsApiRespItem
import com.floward.androidassignment.ObjectModel.UsersApiRespItem
import com.floward.androidassignment.Util.fromHtml
import com.floward.androidassignment.Util.logMessage
import com.floward.androidassignment.Util.viewBinding
import com.floward.androidassignment.databinding.ActivityScreenTwoBinding
import com.floward.androidassignment.databinding.RowPostsBinding

class ScreenTwoActivity : AppCompatActivity() {

    private val bindingView by viewBinding(ActivityScreenTwoBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(bindingView.root)
        init()

    }

    private fun init() {

        val usersApiRespItem = intent.extras!!.getSerializable("dataResult") as UsersApiRespItem
        logMessage("usersApiRespItem::GET:$usersApiRespItem")

        Glide.with(applicationContext).load(usersApiRespItem.url)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(bindingView.ivUsers)

        bindingView.rvContent.isNestedScrollingEnabled = false
        bindingView.rvContent.setHasFixedSize(true)
        val lytManager = LinearLayoutManager(applicationContext)
        lytManager.orientation = LinearLayoutManager.VERTICAL
        bindingView.rvContent.layoutManager = lytManager

        val listPostsFilter: ArrayList<PostsApiRespItem> = arrayListOf()
        for (tempPosts in AssignmentApp.listPostsItem) {
            if (tempPosts.userId == usersApiRespItem.userId) {
                listPostsFilter.add(tempPosts)
            }
        }

        val adpRatReason = AdapterPosts(listPostsFilter)
        bindingView.rvContent.adapter = adpRatReason

    }

    inner class AdapterPosts(private val listPostsFilter: ArrayList<PostsApiRespItem>) :
            RecyclerView.Adapter<AdapterPosts.ViewHolderItem>() {

        override fun getItemCount(): Int {
            return listPostsFilter.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
            val binding = RowPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolderItem(binding)
        }

        override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
            val dataResult: PostsApiRespItem = listPostsFilter[position]
            holder.bind(dataResult, position)
        }

        inner class ViewHolderItem(private val bindingRow: RowPostsBinding) : RecyclerView.ViewHolder(bindingRow.root) {

            fun bind(dataResult: PostsApiRespItem, position: Int) {
                with(bindingRow) {

                    tvPostsTitle.text = dataResult.title
                    tvPostsBody.text = fromHtml(dataResult.body.trim())


                }
            }

        }
    }
}
