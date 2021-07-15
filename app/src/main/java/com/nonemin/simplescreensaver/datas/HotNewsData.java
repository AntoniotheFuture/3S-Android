package com.nonemin.simplescreensaver.datas;

import com.alibaba.fastjson.JSONArray;
import java.util.Random;

//热点信息类,每半小时更新一次，每次10条，随机出现
public class HotNewsData {
    private JSONArray newsList = null;


    private String author_name;//新闻作者
    private String title;//新闻标题
    private String category;//新闻类型
    private String headImg;//新闻图片

    public HotNewsData(JSONArray newsList){
        this.newsList = newsList;
    }

    //随机选择一条作为本次显示的新闻
    private void randomPick(){
        Random random = new Random();
        int i = random.nextInt(newsList.size());
        title = newsList.getJSONObject(i).getString("title");
        author_name = newsList.getJSONObject(i).getString("author_name");
        category = newsList.getJSONObject(i).getString("category");
        headImg = newsList.getJSONObject(i).getString("thumbnail_pic_s");
    }

    //获取标题
    public String getTitle(){
        randomPick();
        String result = "";
        result = this.author_name + this.category;
        return result;
    }

    //获取内容
    public String getDetail() {
        return this.title;
    }

    //获取图片

    public String getHeadImg() {
        return headImg;
    }
}
