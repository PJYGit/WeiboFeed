package com.example.simplebooster.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.simplebooster.R
import com.example.simplebooster.adapters.ContentPagerAdapter
import com.example.simplebooster.data.FeedMessage
import com.example.simplebooster.network.MessageService
import com.example.simplebooster.network.ServiceCreator
import com.example.simplebooster.data.PageName
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_feed.*
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

        setSupportActionBar(toolbar)

        //runBlocking { withContext(Dispatchers.IO) { getJson() } }
        val messageService = ServiceCreator.create(MessageService::class.java)
        runBlocking {
            val result = withContext(Dispatchers.IO){
                messageService.getFeedMessage().execute()
            }

            if (result.isSuccessful){
                messageList = result.body()!! as ArrayList<FeedMessage>
                content_pageAdapter.adapter = ContentPagerAdapter(messageList)
            }
        }

//        if (messages != null)
//            content_pageAdapter.adapter = ContentPagerAdapter(messages!!)
//        else
//            Log.d("feedactivity", "no message")

        tabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabLayout, content_pageAdapter) { tab, position ->
            tab.text = PageName().getName(position)
        }.attach()


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