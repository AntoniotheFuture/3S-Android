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
