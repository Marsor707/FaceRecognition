package com.facerecognition;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.GlideImageLoader;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Banner banner;
    private GridView gridView;
    private BottomNavigationBar bottomNavigationBar;
    private List<Map<String,Object>> dataList;
    private SimpleAdapter adapter;
    private static Integer[] bannerImages={R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4};
    private static Integer[] gridImages={R.mipmap.track,R.mipmap.compare,R.mipmap.mode,R.mipmap.detect,R.mipmap.capture,R.mipmap.search};
    private static String[] gridTexts={"人像跟踪","人脸比对","人像建模","活体检测","人脸捕获","人脸搜索"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        banner= (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(Arrays.asList(bannerImages));
        banner.setDelayTime(5000);
        banner.start();
        gridView= (GridView) findViewById(R.id.gridView);
        dataList=new ArrayList<>();
        adapter=new SimpleAdapter(this,getDataList(),R.layout.gridview_item,new String[]{"image","text"},new int[]{R.id.gridView_img,R.id.gridView_text});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        bottomNavigationBar= (BottomNavigationBar) findViewById(R.id.bottomNavigation);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.home,"Home"))
                           .addItem(new BottomNavigationItem(R.mipmap.news,"News"))
                           .addItem(new BottomNavigationItem(R.mipmap.about,"About Us"))
                           .initialise();
    }

    private List<Map<String,Object>> getDataList() {
        for(int i=0;i<gridImages.length;i++){
            Map<String ,Object> map=new HashMap<>();
            map.put("image",gridImages[i]);
            map.put("text",gridTexts[i]);
            dataList.add(map);
        }
        return dataList;
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
