package com.example.simplebooster.adapters

import androidx.viewpager2.widget.ViewPager2
import com.example.simplebooster.data.PageName
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplebooster.R
import com.example.simplebooster.data.FeedMessage
import java.lang.IllegalArgumentException

class TwoPagerAdapter(val messages: ArrayList<FeedMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        val content_pager: ViewPager2 = view.findViewById(R.id.content_pager)
    }

    inner class ConcernViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.page_two_tab, parent, false)
            return HotViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.others, parent, false)
            return ConcernViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HotViewHolder -> {
                holder.content_pager.adapter = ContentPagerAdapter(messages)
                TabLayoutMediator(holder.tabs, holder.content_pager) { tab, position ->
                    tab.text = PageName().getName(position)
                }.attach()
                holder.content_pager.bringToFront()
            }
            is ConcernViewHolder -> doNothing()
            else -> throw IllegalArgumentException()
        }
    }

    private fun doNothing() {}


}