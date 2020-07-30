package com.example.simplebooster.data

// 为方便使用 TODO: 待抽离到strings.xml
class PageName {
    companion object{
        val temp: String =
            "1.我们总是在意别人的言论，不敢做自己喜欢的事情，害怕淹没在飞短流长之中。其实没有人真的在乎你在想什么，不要过高估量自己在他人心目中的地位。被别人议论甚至误解都没啥，谁人不被别人说，谁人背后不说人，你生活在别人的眼神里，就迷失在自己的心路上。\n" +
                    "2.人和人之间就是一份情，一份缘，你珍惜我，我会加倍奉还，你不在意我，就让一切归零！" +
                    "3.在大人面前，当然是孩子错了；在村干部面前，当然是村民错了；在裁判面前，当然是球员错了，在现实面前，当然是理想错了。位置决定对错！\n" +
                    "4.管好自己的事情就好，别人做什么人家乐意，没威胁到你的利益就不要多嘴。"
    }
    private val names : ArrayList<String> = ArrayList<String>()

    constructor(){
        names.add("推荐")
        names.add("同城")
        names.add("抽奖")
        names.add("搞笑")
        names.add("关注")
        names.add("热门")
    }


    fun getName(pos : Int) = names[pos]
}