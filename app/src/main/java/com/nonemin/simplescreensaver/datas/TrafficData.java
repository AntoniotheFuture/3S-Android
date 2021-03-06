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

import com.alibaba.fastjson.JSONObject;

//周边交通类，每10分钟更新一次
public class TrafficData {
    private String status_desc; //路况整体评价
    private String description; //路况描述

    public TrafficData(JSONObject Traffic){
        try {
            status_desc = Traffic.getJSONObject("evaluation").getString("status_desc");
            description = Traffic.getString("description");
        }catch (Exception e){
            status_desc = "路况获取失败";
            description = "路况获取失败";
            e.printStackTrace();

        }
    }

    //获取标题
    public String getTitle(){
        return "周边路况：" + this.status_desc;
    }

    //获取内容
    public String getDetail() {
        return this.description;
    }
}
