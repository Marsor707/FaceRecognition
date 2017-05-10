package com.facerecognition;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.FileNotFoundException;

/**
 * Created by marsor on 2017/5/9.
 */

public class SearchFacesActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final String TAG = "SearchFacesActivity";
    private Bitmap myBitMap;
    private ImageButton addPhotoSearch;
    private Button search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facesearch);
        init();
    }

    private void init() {
        addPhotoSearch = (ImageButton) findViewById(R.id.addPhotoSearch);
        search= (Button) findViewById(R.id.search);
        addPhotoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchFacesActivity.this,SearchResultActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Log.i(TAG, "onActivityResult: " + uri.toString());
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    myBitMap = bitmap;
                    addPhotoSearch.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}