package com.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by marsor on 2017/5/9.
 */

public class SearchActivity extends AppCompatActivity {
    private Button startSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        init();
    }

    private void init(){
        startSearch= (Button) findViewById(R.id.startSearch);
        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchActivity.this,SearchFacesActivity.class);
                startActivity(intent);
            }
        });
    }
}
