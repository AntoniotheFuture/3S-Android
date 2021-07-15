package com.nonemin.simplescreensaver.datas;


import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonemin.simplescreensaver.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//天气信息类，20分钟更新一次
public class WeatherData {
    private final int Temp;
    private final int Humidity;
    private final String Text;//天气描述
    private final String WindDir;
    private final double WindScale;
    private final int Pressure; //气压
    private final int Vis; //能见度
    private final String Icon;//天气图标

    public final static Map<String,String> weatherMap = new HashMap<>();
    static {
        weatherMap.put("100", "sunny");
        weatherMap.put("101", "cloudy");
        weatherMap.put("102", "cloudy");
        weatherMap.put("103", "cloudy");
        weatherMap.put("104", "cloudy");
        weatherMap.put("150", "sunny");
        weatherMap.put("153", "cloudy");
        weatherMap.put("154", "cloudy");
        weatherMap.put("300", "rain");
        weatherMap.put("301", "rain");
        weatherMap.put("302", "thunder");
        weatherMap.put("303", "thunder");
        weatherMap.put("304", "thunder");
        weatherMap.put("305", "rain");
        weatherMap.put("306", "rain");
        weatherMap.put("307", "rain");
        weatherMap.put("308", "rain");
        weatherMap.put("309", "rain");
        weatherMap.put("310", "rain");
        weatherMap.put("311", "rain");
        weatherMap.put("312", "rain");
        weatherMap.put("313", "rain");
        weatherMap.put("314", "rain");
        weatherMap.put("315", "rain");
        weatherMap.put("316", "rain");
        weatherMap.put("317", "rain");
        weatherMap.put("318", "rain");
        weatherMap.put("399", "rain");
        weatherMap.put("350", "rain");
        weatherMap.put("351", "rain");
        weatherMap.put("400", "snow");
        weatherMap.put("401", "snow");
        weatherMap.put("402", "snow");
        weatherMap.put("403", "snow");
        weatherMap.put("404", "snow");
        weatherMap.put("405", "snow");
        weatherMap.put("406", "rain");
        weatherMap.put("407", "snow");
        weatherMap.put("408", "snow");
        weatherMap.put("409", "snow");
        weatherMap.put("410", "snow");
        weatherMap.put("499", "snow");
        weatherMap.put("456", "snow");
        weatherMap.put("457", "snow");
        weatherMap.put("500", "foggy");
        weatherMap.put("501", "foggy");
        weatherMap.put("502", "foggy");
        weatherMap.put("503", "foggy");
        weatherMap.put("504", "foggy");
        weatherMap.put("507", "foggy");
        weatherMap.put("508", "foggy");
        weatherMap.put("509", "foggy");
        weatherMap.put("510", "foggy");
        weatherMap.put("511", "foggy");
        weatherMap.put("512", "foggy");
        weatherMap.put("513", "foggy");
        weatherMap.put("514", "foggy");
        weatherMap.put("515", "foggy");
        weatherMap.put("900", "sunny");
        weatherMap.put("901", "cloudy");
        weatherMap.put("999", "sunny");
    }


    private final Integer[] cloudyPic = new Integer[]{
            R.drawable.cloudy1,
            R.drawable.cloudy2,
            R.drawable.cloudy3,
            R.drawable.cloudy4,
            R.drawable.cloudy5,
            R.drawable.cloudy6,
            R.drawable.cloudy7,
            R.drawable.cloudy8
    };

    private final Integer[] foggyPic = new Integer[]{
            R.drawable.foggy1,
            R.drawable.foggy2,
            R.drawable.foggy3,
            R.drawable.foggy4,
            R.drawable.foggy5,
            R.drawable.foggy6
    };

    private final Integer[] rainPic = new Integer[]{
            R.drawable.rain1,
            R.drawable.rain2,
            R.drawable.rain3,
            R.drawable.rain4,
            R.drawable.rain5,
            R.drawable.rain6,
            R.drawable.rain7
    };

    private final Integer[] snowPic = new Integer[]{
            R.drawable.snow1,
            R.drawable.snow2,
            R.drawable.snow3,
            R.drawable.snow4,
            R.drawable.snow5,
            R.drawable.snow6
    };

    private final Integer[] sunnyPic = new Integer[]{
            R.drawable.sunny1,
            R.drawable.sunny2,
            R.drawable.sunny3,
            R.drawable.sunny4,
            R.drawable.sunny5,
            R.drawable.sunny6,
            R.drawable.sunny7,
            R.drawable.sunny8
    };

    private final Integer[] thunderPic = new Integer[]{
            R.drawable.thunder1,
            R.drawable.thunder2,
            R.drawable.thunder3,
            R.drawable.thunder4,
            R.drawable.thunder5,
            R.drawable.thunder6
    };

    private JSONArray warningInfo = null;


    public WeatherData(JSONObject weatherInfo, @Nullable JSONArray warningInfo){
        Temp = weatherInfo.getInteger("temp");
        Humidity = weatherInfo.getInteger("humidity");
        WindDir = weatherInfo.getString("windDir");
        WindScale = weatherInfo.getDouble("windScale") / 3.6;
        Text = weatherInfo.getString("text");
        Pressure = weatherInfo.getInteger("pressure");
        Vis = weatherInfo.getInteger("vis");
        Icon = weatherInfo.getString("icon");

        this.warningInfo = warningInfo;
    }

    //获取标题
    public String getTitle(){
        String result = "";
        if(warningInfo != null && warningInfo.size() > 0){
            JSONObject warning = warningInfo.getJSONObject(0);
            result = warning.getString("level") + warning.getString("typeName")  + "\n" + this.Temp + "°C  " + this.Humidity + "%";
        }else{
            result = this.Text + "  " + this.Temp + "°C  " + this.Humidity + "%";
        }
        return result;
    }

    //获取内容
    public String getDetail(){
        String result = "";
        if(warningInfo != null && warningInfo.size() > 0){
            JSONObject warning = warningInfo.getJSONObject(0);
            result = warning.getString("pubTime") + warning.getString("title") + "\n" + this.Text + "  " + "风：" + this.WindDir + this.WindScale + "级  气压" + Pressure + "百帕  能见度" + Vis + "公里";
        }else{
            result = "风：" + this.WindDir + this.WindScale + "级  气压" + Pressure + "百帕  能见度" + Vis + "公里";
        }
        return result;
    }

    //获取图片
    public int getPic(){
        String type = weatherMap.get(Icon);
        int i;
        Random random = new Random();
        if(type == null){
            i = random.nextInt(sunnyPic.length);
            return sunnyPic[i];
        }
        switch (type){
            case "sunny":
                i = random.nextInt(sunnyPic.length);
                return sunnyPic[i];
            case "cloudy":
                i = random.nextInt(cloudyPic.length);
                return cloudyPic[i];
            case "foggy":
                i = random.nextInt(foggyPic.length);
                return foggyPic[i];
            case "rain":
                i = random.nextInt(rainPic.length);
                return rainPic[i];
            case "snow":
                i = random.nextInt(snowPic.length);
                return snowPic[i];
            case "thunder":
                i = random.nextInt(thunderPic.length);
                return thunderPic[i];
            default:
                i = random.nextInt(sunnyPic.length);
                return sunnyPic[i];
        }

    }

}
