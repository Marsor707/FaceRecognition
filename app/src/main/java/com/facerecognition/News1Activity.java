package com.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

/**
 * Created by marsor on 2017/5/11.
 */

public class News1Activity extends AppCompatActivity {
    private static Integer[] gridImages={R.mipmap.track,R.mipmap.compare,R.mipmap.mode,R.mipmap.detect,R.mipmap.capture,R.mipmap.search};
    private static String[] gridTexts={"人像跟踪","人脸比对","人像建模","活体检测","人脸捕获","人脸搜索"};
    private BottomNavigationBar news1Navigation;
    private ImageView news1Click;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news1);
        news1Navigation= (BottomNavigationBar) findViewById(R.id.news1Navigation);
        news1Click= (ImageView) findViewById(R.id.news1Click);
        news1Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(News1Activity.this,News2Activity.class);
                startActivity(intent);
            }
        });
        news1Navigation.addItem(new BottomNavigationItem(R.mipmap.home,"Home"))
                .addItem(new BottomNavigationItem(R.mipmap.news,"News"))
                .addItem(new BottomNavigationItem(R.mipmap.about,"About Us"))
                .setFirstSelectedPosition(1)
                .initialise();
    }

}
