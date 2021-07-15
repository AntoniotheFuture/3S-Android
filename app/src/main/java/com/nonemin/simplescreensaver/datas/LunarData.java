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
