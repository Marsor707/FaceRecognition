package com.facerecognition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

/**
 * Created by marsor on 2017/5/11.
 */

public class AboutUsActivity extends AppCompatActivity {
    private static Integer[] gridImages={R.mipmap.track,R.mipmap.compare,R.mipmap.mode,R.mipmap.detect,R.mipmap.capture,R.mipmap.search};
    private static String[] gridTexts={"人像跟踪","人脸比对","人像建模","活体检测","人脸捕获","人脸搜索"};
    private BottomNavigationBar aboutUsNavigation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        aboutUsNavigation= (BottomNavigationBar) findViewById(R.id.aboutUsNavigation);
        aboutUsNavigation.addItem(new BottomNavigationItem(R.mipmap.home,"Home"))
                .addItem(new BottomNavigationItem(R.mipmap.news,"News"))
                .addItem(new BottomNavigationItem(R.mipmap.about,"About Us"))
                .setFirstSelectedPosition(2)
                .initialise();
    }
}
