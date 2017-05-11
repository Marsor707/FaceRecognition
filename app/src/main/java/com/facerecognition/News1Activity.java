package com.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by marsor on 2017/5/11.
 */

public class News1Activity extends AppCompatActivity {
    private ImageView news1Click;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news1);
        news1Click= (ImageView) findViewById(R.id.news1Click);
        news1Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(News1Activity.this,News2Activity.class);
                startActivity(intent);
            }
        });
    }

}
