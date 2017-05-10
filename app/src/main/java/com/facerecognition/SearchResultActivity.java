package com.facerecognition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by marsor on 2017/5/10.
 */

public class SearchResultActivity extends AppCompatActivity {
    private LinearLayoutManager mLinearManager;
    private RecyclerView searchResult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        init();
    }

    private void init(){
        searchResult= (RecyclerView) findViewById(R.id.searchResult);
        mLinearManager=new LinearLayoutManager(this);

    }
}
