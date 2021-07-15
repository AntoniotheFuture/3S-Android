# SimpleScreensaver极简屏保

## 简介
个人练手项目，免费开放参考，用于亮屏设备在非锁屏状态下的屏幕保护.

## 适用于
- 安卓 7+
- 基于安卓的广告机、信息展示板
- 基于安卓的智能镜子
- 基于安卓的其他展示设备（比如说物尽其用的手机）

## 使用方法

1. 
- 将 /app/src/main/java/com/nonemin/simplescreensaver/common/ConstantsExample.java 中的内容改为自己的Api信息并另存为 /app/src/main/java/com/nonemin/simplescreensaver/common/Constants.java
- 修改app/src/main/AndroidManifestExample.xml 中关于百度SDK的秘钥 并另存为 app/src/main/AndroidManifest.xml

2. 将自己的天气图片资源文件放到/app/src/main/res/drawable-v24,对应 /app/src/main/java/com/nonemin/simplescreensaver/datas/WeatherData.java 图片太大了所以不传到git。

2. 使用Android Studio（4.2）和Gradle（6.7）修改App秘钥配置后编译成Apk文件。

3. 在安卓手机上安装本App和[Tasker](https://www.iplaysoft.com/tasker.html) (或其他自动化App），然后设定Tasker每20分钟启动一次本App。


## 功能：随机展示以下的信息
- 时间
- 天气
- 周边路况
- 笑话大全
- 热点新闻
- 黄历
- 动画（未完成）
- 整点报时（无声）
- App快速启动（Todo）


## 使用的技术
- Http
- Glide（图片显示）
- 数据绑定
- 属性动画

## 使用的数据来源（需要自己准备）
- 和风天气（实时）：https://dev.qweather.com/docs/api/weather/weather-now/
- 和风天气（预警）：https://dev.qweather.com/docs/api/warning/weather-warning/
- 百度定位SDK：https://lbsyun.baidu.com/index.php?title=android-locsdk
- 新闻头条：https://www.juhe.cn/docs/api/id/235
- 老黄历：https://www.juhe.cn/docs/api/id/65
- 随机笑话：https://www.juhe.cn/docs/api/id/95
- 随机图片：https://picsum.photos/900/1600?random
- 百度webApi（自己搭建服务端，用于查询周边路况）：https://lbsyun.baidu.com/index.php?title=webapi/traffic

## TODOs
- 优化动画
- 多语言
- 配置界面（字体大小、变换频率等）
- 保持屏幕亮度
- 天气背景动画化

## License
MIT