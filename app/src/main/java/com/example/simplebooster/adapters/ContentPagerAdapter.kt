package com.example.simplebooster.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.simplebooster.R
import com.example.simplebooster.data.FeedMessage
import com.example.simplebooster.network.MessageService
import com.example.simplebooster.network.ServiceCreator
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class ContentPagerAdapter(val messages: ArrayList<FeedMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var mAdapter: RecommendAdapter
    lateinit var mLayoutManager: LinearLayoutManager

    inner class RecommendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recommend_rv: RecyclerView = view.findViewById(R.id.recommend_page)
        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    }

    inner class OtherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.recommend, parent, false)
            return RecommendViewHolder(view).apply {
                recommend_rv.setUpRecyclerView(messages, RecyclerView.VERTICAL)
            }
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.others, parent, false)
            return OtherViewHolder(view)
        }
    }

    private fun RecyclerView.setUpRecyclerView(messages: ArrayList<FeedMessage>, orientation: Int) {
        layoutManager = LinearLayoutManager(context, orientation, false)
        mLayoutManager = layoutManager as LinearLayoutManager
        Log.d("adapter", "setting the adapter.")
        adapter = RecommendAdapter(messages)
        mAdapter = adapter as RecommendAdapter
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecommendViewHolder -> {
                // 下拉刷新
                holder.swipeRefreshLayout.setColorSchemeColors(Color.YELLOW)
                holder.swipeRefreshLayout.setOnRefreshListener {
                    val messageService = ServiceCreator.create(MessageService::class.java)
                    runBlocking {
                        val result = withContext(Dispatchers.IO) {
                            messageService.getRefreshMessage().execute()
                        }

                        if (result.isSuccessful) {
                            messages.addAll(0, result.body()!!)
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                    holder.swipeRefreshLayout.isRefreshing = false
                }

                // 上拉加载
                holder.recommend_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)

                        val last = mLayoutManager.findLastVisibleItemPosition()
                        val sum = mAdapter.itemCount

                        if (newState == RecyclerView.SCROLL_STATE_IDLE && last + 1 == sum) {
                            val messageService = ServiceCreator.create(MessageService::class.java)
                            runBlocking {
                                val result = withContext(Dispatchers.IO) {
                                    messageService.getRefreshMessage().execute()
                                }

                                if (result.isSuccessful) {
                                    messages.addAll(result.body()!!)
                                }
                            }
                            mAdapter.notifyDataSetChanged()
                            Toast.makeText(holder.recommend_rv.context, "加载完成", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }
            is OtherViewHolder -> doNothing()
            else -> throw IllegalArgumentException()
        }
    }

    private fun doNothing() {}


}