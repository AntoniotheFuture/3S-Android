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
package com.nonemin.simplescreensaver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AppButton extends ConstraintLayout {
    private ImageView AppIcon;
    private TextView AppName;
    private int IconRes;
    private String NameText;
    private int  NameColor;

    public AppButton(@NonNull @org.jetbrains.annotations.NotNull Context context) {
        super(context);
        AppIcon = (ImageView) findViewById(R.id.AppIcon);
        AppName = (TextView) findViewById(R.id.AppName);
    }

    public AppButton(@NonNull @org.jetbrains.annotations.NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.app_button_layout, this, true);

        TypedArray typeArray = context.obtainStyledAttributes(attrs,R.styleable.AppButton);
        IconRes = typeArray.getResourceId(R.styleable.AppButton_appIcon,R.drawable.ic_launcher_foreground);
        NameText = typeArray.getString(R.styleable.AppButton_appName);
        NameColor = typeArray.getColor(R.styleable.AppButton_NameColor,0XFFFFFFFF);
        typeArray.recycle();
    }

    public void setImageRes(int resId){
        AppIcon.setImageResource(resId);
    }

    public void setAppName(String text){
        AppName.setText(text);
    }

    public ImageView getAppIcon() {
        return AppIcon;
    }

    public TextView getAppName() {
        return AppName;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AppIcon.setImageResource(IconRes);
        AppName.setText(NameText);
        AppName.setTextColor(NameColor);
    }


}
