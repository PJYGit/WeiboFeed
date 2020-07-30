package com.example.simplebooster.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.simplebooster.R
import com.example.simplebooster.adapters.ContentPagerAdapter
import com.example.simplebooster.adapters.TwoPagerAdapter
import com.example.simplebooster.data.FeedMessage
import com.example.simplebooster.network.MessageService
import com.example.simplebooster.network.ServiceCreator
import com.example.simplebooster.data.PageName
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.page_two_tab.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FeedActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var tabLayout: TabLayout
    private lateinit var job: Job
    lateinit var messageList: ArrayList<FeedMessage>
//    var messages: List<FeedMessage>? = null

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.activity_feed)

        //runBlocking { withContext(Dispatchers.IO) { getJson() } }
        val messageService = ServiceCreator.create(MessageService::class.java)
        runBlocking {
            val result = withContext(Dispatchers.IO){
                messageService.getFeedMessage().execute()
            }

            if (result.isSuccessful){
                messageList = result.body()!! as ArrayList<FeedMessage>
                two_page.adapter = TwoPagerAdapter(messageList, two_page)
            }
        }

//        if (messages != null)
//            content_pageAdapter.adapter = ContentPagerAdapter(messages!!)
//        else
//            Log.d("feedactivity", "no message")

        tabLayout = findViewById(R.id.two_tab)
        TabLayoutMediator(tabLayout, two_page) { tab, position ->
            tab.text = PageName().getName(position + 4)
        }.attach()

        two_page.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1){
                    content_pager.currentItem = 0
                    two_page.isUserInputEnabled = (two_page.currentItem < 1)
                } else {
                    two_page.isUserInputEnabled = true
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // TODO: operations
        }
        return true
    }

//    private fun getJson() {
//        val messageService = ServiceCreator.create(MessageService::class.java)
//        messageService.getFeedMessage().enqueue(object : Callback<List<FeedMessage>> {
//            override fun onResponse(
//                call: Call<List<FeedMessage>>,
//                response: Response<List<FeedMessage>>
//            ) {
//                val list = response.body()
//                if (list != null) {
//                    messages = list
//                }
//            }
//
//            override fun onFailure(call: Call<List<FeedMessage>>, t: Throwable) {
//                t.printStackTrace()
//                Log.d("message", "json failed!")
//            }
//        })
//    }
}