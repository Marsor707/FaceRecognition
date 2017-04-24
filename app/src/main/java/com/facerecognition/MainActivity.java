package com.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.youth.banner.Banner;

import java.util.Arrays;

import utils.GlideImageLoader;
import utils.GridViewAdapter;
import utils.SizeUtil;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Banner banner;
    private GridView gridView;
    private BottomNavigationBar bottomNavigationBar;
    private GridViewAdapter adapter;
    private static final String TAG="MainActivity";
    private static Integer[] bannerImages={R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4};
    private static Integer[] gridImages={R.mipmap.track,R.mipmap.compare,R.mipmap.mode,R.mipmap.detect,R.mipmap.capture,R.mipmap.search};
    private static String[] gridTexts={"人像跟踪","人脸比对","人像建模","活体检测","人脸捕获","人脸搜索"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Log.i(TAG, "onCreate: "+ SizeUtil.getDeviceHeight(MainActivity.this));
        Log.i(TAG, "onCreate: "+ SizeUtil.getDeviceWidth(MainActivity.this));
    }

    private void init(){
        //图片轮播
        banner= (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(Arrays.asList(bannerImages));
        banner.setDelayTime(5000);
        banner.start();
        //gridView
        gridView= (GridView) findViewById(R.id.gridView);
        adapter=new GridViewAdapter(MainActivity.this,gridImages,gridTexts);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        //底部导航栏
        bottomNavigationBar= (BottomNavigationBar) findViewById(R.id.bottomNavigation);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.home,"Home"))
                           .addItem(new BottomNavigationItem(R.mipmap.news,"News"))
                           .addItem(new BottomNavigationItem(R.mipmap.about,"About Us"))
                           .initialise();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position){
            case 0:{
                Intent intent=new Intent(MainActivity.this,TrackActivity.class);
                startActivity(intent);
                break;
            }
            case 1:{
                Intent intent=new Intent(MainActivity.this,CompareActivity.class);
                startActivity(intent);
                break;
            }
            case 2:{
                Intent intent=new Intent(MainActivity.this,ModeActivity.class);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }
}
