package com.nonemin.simplescreensaver;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.nonemin.simplescreensaver.common.Constants;
import com.nonemin.simplescreensaver.databinding.ActivityMainBinding;
import com.nonemin.simplescreensaver.datas.HotNewsData;
import com.nonemin.simplescreensaver.datas.JokeData;
import com.nonemin.simplescreensaver.datas.LunarData;
import com.nonemin.simplescreensaver.datas.TrafficData;
import com.nonemin.simplescreensaver.datas.WeatherData;
import com.nonemin.simplescreensaver.helper.okHttpRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity  implements ViewTreeObserver.OnGlobalLayoutListener{

    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    ActivityMainBinding binding;

    public ObservableField<String> Title1 = new ObservableField<>();
    public ObservableField<String> Info = new ObservableField<>();

    public ObservableField<Integer> bgRes = new ObservableField<>();

    public ObservableField<Boolean> bgHide = new ObservableField<>();

    public ObservableField<Boolean> ShowGradient = new ObservableField<>();
    //上次定位的位置
    static BDLocation lastLocation;

    OkHttpClient HttpClient = null;
    okHttpRequest okHttpRequest = null;

    String[] dataList = {"时间","黄历","天气","热点新闻","动画","周边交通","笑话大全"};

    WeatherData weatherData = null;
    LunarData lunarData = null;
    HotNewsData hotNewsData = null;
    TrafficData trafficData = null;
    JokeData jokeData = null;

    String bgImageUrl = "";

    //当前显示内容
    String showType = "时间";

    //上下文
    Context context;

    TimerTask mainTimer = new TimerTask() {
        @Override
        public void run() {
            //随机切换

            //判断是否准点，是的话报时
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("mm");
            int mm = Integer.parseInt(sdf.format(d));
            if( mm == 59 || mm == 0) {
                showTime();
                return;
            }
            Random rand = new Random();
            int i = rand.nextInt(dataList.length);
            showType = dataList[i];
            Log.i("showType",showType);
            boolean showOther = false;
            switch (showType){
                case "时间":
                    showTime();
                    break;
                case "黄历":
                    if(lunarData != null) {
                        showLunar();
                        showOther = true;
                    }
                    break;
                case "天气":
                    if(weatherData != null) {
                        showWeather();
                        showOther = true;
                    }
                    break;
                case "热点新闻":
                    if(hotNewsData != null){
                        showHotNews();
                        showOther = true;
                    }
                    break;
                case "动画":
                    showAnimation();
                    showOther = true;
                    break;
                case "周边交通":
                    if(trafficData != null) {
                        showTraffic();
                        showOther = true;
                    }
                    break;
                case "笑话大全":
                    if(jokeData != null) {
                        showJoke();
                        showOther = true;
                    }
                    break;
                default:
                    showTime();
                    break;
            }
            if(!showOther){
                showTime();
                showType = "时间";
            }
        }
    };
    Timer timer = new Timer();

    private LocationClient mLocationClient = null;

    //打开抽屉的动画
    private void openDrawerAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(1f,600f);
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.AppDrawer.getLayoutParams();
                layoutParams.height = (int) currentValue;
                binding.AppDrawer.setLayoutParams(layoutParams);
            }


        });
        anim.start();
    }

    //关闭抽屉的动画
    private void closeDrawerAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(600f,1f);
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.AppDrawer.getLayoutParams();
                layoutParams.height = (int) currentValue;
                binding.AppDrawer.setLayoutParams(layoutParams);
            }
        });
        anim.start();
    }

    @BindingAdapter("BgVisible")
    public static void setBgVisible(final View view, boolean visible) {
        view.animate().cancel();

        if (visible) {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0);
            view.animate().alpha(1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setAlpha(1);
                }
            });
        } else {
            view.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setAlpha(1);
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

    //注册LocationListener监听器
    static class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null){
                Log.e("定位传感器","没有获取到位置");
                return;
            }
            Log.i("定位","定位成功："+location.toString());
            lastLocation = location;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);// ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.setMainData(this);

        bgHide.set(false);
        bgHide.notifyChange();

        ShowGradient.set(true);
        ShowGradient.notifyChange();

        binding.bg.setImageResource(R.drawable.ic_launcher_background);
        binding.bg.setAlpha(1.0f);

        closeDrawerAnimator();

        mLocationClient = new LocationClient(this.getApplicationContext());
        MyLocationListener myLocationListener = new MyLocationListener();
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myLocationListener);



        HttpClient = new OkHttpClient.Builder().build();

        okHttpRequest = new okHttpRequest(HttpClient);

        if(!mLocationClient.isStarted()) {
            mLocationClient.start();
        }


        //TODO 申请权限
        initClock();
        initDatas();
        timer.schedule(mainTimer,3000,10000);
        //定时清除glide缓存
        context = this.getApplicationContext();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
                //Glide.get(context).clearMemory();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearMemory();
                    }
                });
            }
        },5000,3600000);

    }

    //初始化所有数据的定时器
    void initDatas(){
        //天气
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getWeatherData();
            }
        },1000,1200000);

        //黄历
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getLunarData();
            }
        },1000,21600000);

        //热点新闻
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getHotNewsData();
            }
        },1000,1800000);

        //交通信息
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getTrafficData();
            }
        },1000,600000);

        //笑话
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getJokeData();
            }
        },1000,1200000);
    }


    TimerTask clockTask = new TimerTask() {
        @Override
        public void run() {
            if(Objects.equals(showType, "时间")){
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                Title1.set(timeFormat.format(date));
                Info.set(dayFormat.format(date));
                Title1.notifyChange();
                Info.notifyChange();
            }
        }
    };

    //初始化时钟
    @SuppressLint("SimpleDateFormat")
    void initClock(){
        Date date = new Date();
        SimpleDateFormat msFormat =new SimpleDateFormat("SSS");
        //获取当前的毫秒数，设置相应的延时
        int ms = Integer.parseInt(msFormat.format(date));
        timer.schedule(clockTask,1000 - ms, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //timer.purge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mainLayout.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        //timer.schedule(mainTimer,10000);
    }

    //随机获取图片
    void randomImage(){
        try {
            bgImageUrl = getRedirectUrl(Constants.bg_Url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**

     * 获取重定向地址

     * @param path

     * @return

     * @throws Exception

     */

    private String getRedirectUrl(String path) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        return conn.getHeaderField("Location");
    }


    //更换背景
    void changeBG(){
        if(Objects.equals(bgImageUrl, "")) {
            randomImage();
        }

        if(!Objects.equals(bgImageUrl, "")) {
            RequestBuilder<Drawable> builder = Glide.with(MainActivity.this).load(bgImageUrl);
            builder.preload();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bgHide.set(false);
                    bgHide.notifyChange();
                    builder.into(binding.bg);
                    bgHide.set(true);
                    bgHide.notifyChange();
                }
            });


            //RequestOptions options = new RequestOptions();
            //options.set()
            /*
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(bgHide!=null) {
                        bgHide.set(!bgHide.get());
                        bgHide.notifyChange();
                    }

                }
            });

             */
        }
    }

    //时钟
    void showTime(){
        randomImage();
        changeBG();
    }

    void getLunarData(){
        JSONObject DayLunarJson;
        JSONArray HourLunarJson;
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("key", Constants.Lunar_Key);
        parameters.put("date", sdf.format(date));

        String responseString = "";
        try {
            responseString = okHttpRequest.Request(Constants.Lunar_d_url,parameters);
        } catch (Exception e) {
            Log.e("LunarApi","请求失败");
            e.printStackTrace();
            return;
        }
        if(!JSONValidator.from(responseString).validate()){
            Log.e("LunarApi","Api返回错误");
            return;
        }
        JSONObject DayLunarRes = JSONObject.parseObject(responseString);

        DayLunarJson = DayLunarRes.getJSONObject("result");

        parameters = new HashMap<>();
        parameters.put("key", Constants.Lunar_Key);
        parameters.put("date", sdf.format(date));

        responseString = "";
        try {
            responseString = okHttpRequest.Request(Constants.Lunar_h_url,parameters);
            Log.i("LunarApi",responseString);
        } catch (Exception e) {
            Log.e("LunarApi","请求失败");
            e.printStackTrace();
            return;
        }
        if(!JSONValidator.from(responseString).validate()){
            Log.e("LunarApi","Api返回错误");
            return;
        }
        JSONObject HourLunarRes = JSONObject.parseObject(responseString);
        HourLunarJson = HourLunarRes.getJSONArray("result");
        lunarData = new LunarData(DayLunarJson,HourLunarJson);
    }
    //黄历
    void showLunar(){
        randomImage();
        changeBG();
        Title1.set(lunarData.getTitle());
        Title1.notifyChange();
        Info.set(lunarData.getDetail());
        Info.notifyChange();
    }

    //获取天气数据
    void getWeatherData(){
        JSONObject nowWeather;
        JSONArray nowWarning;
        if(lastLocation == null){return;}
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("key", Constants.Weather_KEY);
        parameters.put("location", lastLocation.getLongitude() + "," + lastLocation.getLatitude());
        parameters.put("gzip", "n");
        String responseString = "";
        try {
            responseString = okHttpRequest.Request(Constants.Weather_Url,parameters);
        } catch (Exception e) {
            Log.e("WeatherApi","请求失败");
            e.printStackTrace();
            return;
        }
        if(!JSONValidator.from(responseString).validate()){
            Log.e("WeatherApi","Api返回错误");
            return;
        }
        JSONObject WeatherRes = JSONObject.parseObject(responseString);
        if (!WeatherRes.getString("code").equals("200")) {
            Log.i("WeatherApi",WeatherRes.getString("code"));
            return;
        }
        nowWeather = WeatherRes.getJSONObject("now");

        parameters = new HashMap<>();
        parameters.put("key", Constants.Weather_KEY);
        parameters.put("location", lastLocation.getLongitude() + "," + lastLocation.getLatitude());

        responseString = "";
        try {
            responseString = okHttpRequest.Request(Constants.Weather_Alarm_Url,parameters);
            Log.i("WeatherApi",responseString);
        } catch (Exception e) {
            Log.e("WeatherApi","请求失败");
            e.printStackTrace();
            return;
        }
        if(!JSONValidator.from(responseString).validate()){
            Log.i("WeatherApi2","Api返回错误");
            return;
        }
        JSONObject WarningRes = JSONObject.parseObject(responseString);
        if (!WarningRes.getString("code").equals("200")) {
            Log.i("WeatherApi2",WarningRes.getString("code"));
            return;
        }
        try {
            nowWarning = WarningRes.getJSONArray("warning");
            weatherData = new WeatherData(nowWeather, nowWarning);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void showWeather(){
        Title1.set(weatherData.getTitle());
        Title1.notifyChange();
        Info.set(weatherData.getDetail());
        Info.notifyChange();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.bg.setImageResource(weatherData.getPic());

            }
        });

    }

    void getHotNewsData(){
        JSONArray HotNews;
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("key", Constants.HotNews_Key);
        String responseString = "";
        try {
            responseString = okHttpRequest.Request(Constants.HotNews_Url,parameters);
            Log.i("HotNewsApi",responseString);
        } catch (Exception e) {
            Log.e("HotNewsApi","请求失败");
            e.printStackTrace();
            return;
        }
        if(!JSONValidator.from(responseString).validate()){
            Log.e("HotNewsApi","Api返回错误");
            return;
        }
        try {
            JSONObject HotNewsRes = JSONObject.parseObject(responseString);
            HotNews = HotNewsRes.getJSONObject("result").getJSONArray("data");
            hotNewsData = new HotNewsData(HotNews);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //热点新闻
    void showHotNews(){
        Title1.set(hotNewsData.getTitle());
        Title1.notifyChange();
        Info.set(hotNewsData.getDetail());
        Info.notifyChange();
        bgImageUrl = hotNewsData.getHeadImg();
        changeBG();
    }

    //动画
    void showAnimation(){

    }

    //周边交通
    void showTraffic(){
        randomImage();
        changeBG();
        Title1.set(trafficData.getTitle());
        Title1.notifyChange();
        Info.set(trafficData.getDetail());
        Info.notifyChange();
    }

    void getTrafficData(){
        if(lastLocation == null){
            return;
        }
        JSONObject TrafficDataJson = null;
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("mak", Constants.Traffic_Key);
        parameters.put("center", "" + lastLocation.getLatitude() + "," + lastLocation.getLongitude());
        parameters.put("radius", "2000");
        String responseString = "";
        try {
            responseString = okHttpRequest.Request(Constants.Traffic_Url,parameters);
            Log.i("TrafficApi",responseString + ":" + parameters.toString());
        } catch (Exception e) {
            Log.e("TrafficApi","请求失败");
            e.printStackTrace();
            return;
        }
        if(!JSONValidator.from(responseString).validate()){
            Log.e("TrafficApi","Api返回错误");
            return;
        }
        try {
            JSONObject TrafficRes = JSONObject.parseObject(responseString);
            TrafficDataJson = TrafficRes.getJSONObject("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(TrafficDataJson == null){
            Log.e("TrafficApi","请求失败(返回空)");
            return;
        }
        trafficData = new TrafficData(TrafficDataJson);
    }


    //笑话
    void showJoke(){
        randomImage();
        changeBG();
        Title1.set(jokeData.getTitle());
        Title1.notifyChange();
        Info.set(jokeData.getDetail());
        Info.notifyChange();
    }

    void  getJokeData(){
        JSONObject JokeJson;
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("key", Constants.Joke_Key);
        String responseString = "";
        try {
            responseString = okHttpRequest.Request(Constants.Joke_Url,parameters);
        } catch (Exception e) {
            Log.e("JokeApi","请求失败");
            e.printStackTrace();
            return;
        }
        if(!JSONValidator.from(responseString).validate()){
            Log.e("JokeApi","Api返回错误");
            return;
        }
        try {
            JSONObject JokeRes = JSONObject.parseObject(responseString);
            JokeJson = JokeRes.getJSONArray("result").getJSONObject(0);
            jokeData = new JokeData(JokeJson);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //热门电影
    void showMovieNews(){

    }

    //展开底栏，如果已展开则收起
    public void openDrawer(View view){
        if(binding.AppDrawer.getLayoutParams().height <= 1){
            openDrawerAnimator();
        }else{
            closeDrawerAnimator();
        }
    }

    //图标长按事件


    //点击回到桌面
    public void goBack(View view){
        binding.mainLayout.animate().translationY(2000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Intent home = new Intent(Intent.ACTION_MAIN);
                //home.addCategory(Intent.CATEGORY_HOME);
                //startActivity(home);
                finish();
            }
        });

    }

    //重新加载已保存的App列表
    public void loadApps(){

    }

    //图标点击事件,如果没有则选择一个App
    public void AppClick(View view){

    }

    void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == 0x001) {
            if (isAllGranted) {
                //initCamera();
                initLocation();
            } else {
                showToast("拒绝权限");
                //finish();
            }
        }
    }

    /**
     * 权限检查
     *
     * @param neededPermissions 需要的权限
     * @return 是否全部被允许
     */
    protected boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllGranted = true;
        for (int grantResult : grantResults) {
            isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
        }
        afterRequestPermission(requestCode, isAllGranted);
    }


    protected void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 在第一次布局完成后，去除该监听，并且进行引擎和相机的初始化
     */
    @Override
    public void onGlobalLayout() {
        //this.getCon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, 0x001);
        } else {
            initLocation();
        }
    }

    void initLocation(){
        //设置定时每十分钟获取一次定位
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mLocationClient.requestLocation();
            }
        },1000,10000);
    }

    //退出时取消所有timer


    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}