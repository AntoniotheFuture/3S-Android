package com.nonemin.simplescreensaver.datas;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

//黄历信息类，半天更新一次
public class LunarData {
    private final String yinli; //阴历
    private final String chongsha; //冲煞
    private final String yi;//宜
    private final String ji; //忌

    private final JSONArray hours;

    private final String[] shichenList = {"丑时","寅时","卯时","辰时","巳时","午时","未时","申时","酉时","戌时","亥时"};


    public LunarData(JSONObject Day, JSONArray Hours){
        yinli = Day.getString("yinli");
        chongsha = Day.getString("chongsha");
        yi = Day.getString("yi");
        ji = Day.getString("ji");
        this.hours = Hours;
    }

    //获取标题
    public String getTitle(){
        String result = "";
        GregorianCalendar calendar = new GregorianCalendar();
        int shichen = (calendar.get(Calendar.HOUR_OF_DAY) + 1) / 2 - 1;
        result = this.yinli + "\n" + shichenList[shichen];
        return result;
    }

    //获取内容
    public String getDetail() {
        String result = "";
        GregorianCalendar calendar = new GregorianCalendar();
        int shichen = (calendar.get(Calendar.HOUR_OF_DAY) + 1) / 2 - 1;
        result = "冲煞：" + this.chongsha + "\n\n宜：" + this.yi + "\n\n忌：" + this.ji + "\n\n" + shichenList[shichen] + "：" + this.hours.getJSONObject(shichen).getString("des");
        return result;
    }
}
