/*
MIT License

Copyright (c) 2021 梁小蜗

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
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
