package com.facerecognition;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;

/**
 * Created by marsor on 2017/5/9.
 */

public class CaptureActivity extends AppCompatActivity {
    private static final int REQUEST_CODE=1;
    private static final int NUMBER_OF_FACES=5;
    private static final String TAG = "CaptureActivity";
    private int imageWidth,imageHeight,detectedFaces;
    private FaceDetector faceDetector;
    private FaceDetector.Face[] faces;
    private ImageButton capturePhoto;
    private Button capture;
    private Bitmap myBitMap;
    private Paint paint;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture);
        init();
    }

    private void init(){
        paint=new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        capturePhoto= (ImageButton) findViewById(R.id.capturePhoto);
        capture= (Button) findViewById(R.id.capture);
        capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBitMap!=null){
                    imageHeight=myBitMap.getHeight();
                    imageWidth=myBitMap.getWidth();
                    faces=new FaceDetector.Face[NUMBER_OF_FACES];
                    faceDetector=new FaceDetector(imageWidth,imageHeight,NUMBER_OF_FACES);
                    detectedFaces=faceDetector.findFaces(myBitMap,faces);
                    if(detectedFaces<=0){
                        Toast.makeText(CaptureActivity.this,"找不到人脸",Toast.LENGTH_SHORT).show();
                    }else {
                        drawFace(faces);
                        capturePhoto.setImageBitmap(myBitMap);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK){
            if(data!=null){
                Uri uri=data.getData();
                ContentResolver contentResolver=getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    myBitMap=bitmap.copy(Bitmap.Config.RGB_565,true);
                    capturePhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void drawFace(FaceDetector.Face[] faces){
        float eyeDistance=0f;
        Canvas canvas=new Canvas(myBitMap);
        for(int i=0;i<faces.length;i++){
            FaceDetector.Face face=faces[i];
            if(face!=null){
                PointF pointF=new PointF();
                face.getMidPoint(pointF);
                eyeDistance=face.eyesDistance();
                canvas.drawRect(pointF.x-eyeDistance,pointF.y-eyeDistance,pointF.x+eyeDistance,pointF.y+eyeDistance,paint);
            }
        }
    }
}
