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
