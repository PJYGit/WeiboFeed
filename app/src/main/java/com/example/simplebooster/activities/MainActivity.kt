package com.example.simplebooster.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.simplebooster.R
import com.example.simplebooster.layouts.NineGridViewGroup
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setImageLoader()

        login.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
        }

//        val imgUrl = "https://i.loli.net/2020/07/28/seoInK2prHuEPg4.jpg"
//        Glide.with(image.context)
//            .load(imgUrl)
//            .apply(
//                RequestOptions()
//                    .placeholder(R.drawable.loading_animation)
//                    .error(R.drawable.ic_broken_image)
//            )
//            .into(image)

//        val messageService = ServiceCreator.create(MessageService::class.java)
//        messageService.getFeedMessage().enqueue(object : Callback<List<FeedMessage>> {
//            override fun onResponse(
//                call: Call<List<FeedMessage>>,
//                response: Response<List<FeedMessage>>
//            ) {
//                val list = response.body()
//                if (list != null){
//                    for (message in list){
//                        Log.d("message", message.toString())
//                    }
//                }
//            }
//            override fun onFailure(call: Call<List<FeedMessage>>, t: Throwable) {
//                t.printStackTrace()
//                Log.d("message", "json failed!")
//            }
//        })

//        test_network.setOnClickListener {
//            requestTest()
//        }
//
//        val temp: String =
//                    "1.我们总是在意别人的言论，不敢做自己喜欢的事情，害怕淹没在飞短流长之中。其实没有人真的在乎你在想什么，不要过高估量自己在他人心目中的地位。被别人议论甚至误解都没啥，谁人不被别人说，谁人背后不说人，你生活在别人的眼神里，就迷失在自己的心路上。\n" +
//                    "2.人和人之间就是一份情，一份缘，你珍惜我，我会加倍奉还，你不在意我，就让一切归零！" +
//                    "3.在大人面前，当然是孩子错了；在村干部面前，当然是村民错了；在裁判面前，当然是球员错了，在现实面前，当然是理想错了。位置决定对错！\n" +
//                    "4.管好自己的事情就好，别人做什么人家乐意，没威胁到你的利益就不要多嘴。"
//        etext.setContent(temp)
    }

    private fun setImageLoader() {
        NineGridViewGroup.setImageLoader(GlideImageLoader())
    }

    /**
     * Glide 加载图片
     */
    class GlideImageLoader : NineGridViewGroup.ImageLoader {
        override fun onDisplayImage(
            context: Context?,
            imageView: ImageView?,
            url: String?
        ) {
            Glide.with(context!!)
                .load(url)
                .placeholder(R.drawable.ic_default_color) // 图片未加载时的占位图或背景色
                .error(R.drawable.ic_broken_image) // 图片加载失败时显示的图或背景色
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 开启本地缓存
                .into(imageView!!)
        }

        override fun getCacheImage(url: String?): Bitmap? {
            return null
        }
    }

//    private fun requestTest() {
//        thread {
//            var connection: HttpURLConnection? = null
//            try {
//                val responseMsg = StringBuilder()
//                val url =
//                    URL("https://www.fastmock.site/mock/c48033378af9b17b4e1c13eda02f52cb/test/weibo")
//                connection = url.openConnection() as HttpURLConnection
//                connection.connectTimeout = 8000
//                connection.readTimeout = 8000
//                val input = connection.inputStream
//                val reader = BufferedReader(InputStreamReader(input))
//                reader.use { reader.forEachLine { responseMsg.append(it) } }
//                runOnUiThread { response.text = responseMsg.toString() }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                connection?.disconnect()
//            }
//        }
//    }
}