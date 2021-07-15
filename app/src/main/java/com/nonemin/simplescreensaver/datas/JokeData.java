package com.nonemin.simplescreensaver.datas;


import com.alibaba.fastjson.JSONObject;

import java.util.Random;

//笑话大全，每20分钟更新一次
public class JokeData {
    private final String hashId; //笑话唯一标识
    private final String content; //笑话内容

    public JokeData(JSONObject Joke){
        hashId = Joke.getString("hashId");
        content = Joke.getString("content");
    }

    //获取标题
    public String getTitle(){
        Random random = new Random();
        return "笑话#" + random.nextInt(9999999) + ":";
    }

    //获取内容
    public String getDetail() {
        return this.content;
    }
}
