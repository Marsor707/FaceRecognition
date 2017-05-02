package com.facerecognition;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import interfaces.LoaderCallBack;
import utils.FaceUtil;
import youtu.Youtu;


/**
 * Created by Seven on 2017/4/22.
 */

public class CompareActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton addPhoto1;
    private ImageButton addPhoto2;
    private Button compare;
    private Bitmap myBitMap1,myBitMap2;
    private static final String APP_ID="10081316";
    private static final String SECRET_ID="AKIDlqLgq4VynWnwufxRYqDsQqLhr8GEqQel";
    private static final String SECRET_KEY="Ed5MXcWHrGoG3hAoS0NZcT9d0WRDRlt3";
    private float mRelativeFaceSize=0.2f;
    private int mAbsoluteFaceSize1=0;
    private int mAbsoluteFaceSize2=0;
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    private CascadeClassifier mJavaDetection;
    private static final String TAG="CompareActivity";
    private static final int REQUEST_CODE1=1;
    private static final int REQUEST_CODE2=2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare);
        init();
    }

    private void init() {
        addPhoto1= (ImageButton) findViewById(R.id.addPhoto1);
        addPhoto2= (ImageButton) findViewById(R.id.addPhoto2);
        compare= (Button) findViewById(R.id.compare);
        addPhoto1.setOnClickListener(this);
        addPhoto2.setOnClickListener(this);
        compare.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addPhoto1:{
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE1);
                break;
            }
            case R.id.addPhoto2:{
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE2);
                break;
            }
            case R.id.compare:{
                if(myBitMap1!=null&&myBitMap2!=null){
//                    Mat rgbMat1=new Mat();
//                    Mat rgbMat2=new Mat();
//                    Utils.bitmapToMat(myBitMap1,rgbMat1);
//                    Utils.bitmapToMat(myBitMap2,rgbMat2);
//                    Mat grayMat1=new Mat();
//                    Mat grayMat2=new Mat();
//                    Imgproc.cvtColor(rgbMat1,grayMat1,Imgproc.COLOR_RGB2GRAY);
//                    Imgproc.cvtColor(rgbMat2,grayMat2,Imgproc.COLOR_RGB2GRAY);
//                    if(mAbsoluteFaceSize1==0){
//                        int height=grayMat1.rows();
//                        if(Math.round(height*mRelativeFaceSize)>0){
//                            mAbsoluteFaceSize1=Math.round(height*mRelativeFaceSize);
//                        }
//                    }
//                    if(mAbsoluteFaceSize2==0){
//                        int height=grayMat1.rows();
//                        if(Math.round(height*mRelativeFaceSize)>0){
//                            mAbsoluteFaceSize2=Math.round(height*mRelativeFaceSize);
//                        }
//                    }
//                    if(mJavaDetection!=null){
//                        MatOfRect faces1=new MatOfRect();
//                        MatOfRect faces2=new MatOfRect();
//                        mJavaDetection.detectMultiScale(grayMat1,faces1,1.1,3,0,new Size(mAbsoluteFaceSize1,mAbsoluteFaceSize1),new Size(grayMat1.width(),grayMat1.height()));
//                        mJavaDetection.detectMultiScale(grayMat2,faces2,1.1,3,0,new Size(mAbsoluteFaceSize2,mAbsoluteFaceSize2),new Size(grayMat2.width(),grayMat2.height()));
//                        mAbsoluteFaceSize1=0;
//                        mAbsoluteFaceSize2=0;
//                        Rect[] facesArray1=faces1.toArray();
//                        Rect[] facesArray2=faces2.toArray();
//                        for (Rect aFacesArray:facesArray1){
//                            Imgproc.rectangle(rgbMat1, aFacesArray.tl(), aFacesArray.br(), FACE_RECT_COLOR, 3);
//                            FaceUtil.saveImage(rgbMat1,aFacesArray,"face1");
//                        }
//                        for (Rect aFacesArray:facesArray2){
//                            Imgproc.rectangle(rgbMat2, aFacesArray.tl(), aFacesArray.br(), FACE_RECT_COLOR, 3);
//                            FaceUtil.saveImage(rgbMat2,aFacesArray,"face2");
//                        }
//                    }else {
//                        Log.i(TAG, "onClick: 级联分类器为空");
//                    }
//                    Toast.makeText(this,"匹配率"+FaceUtil.compare("face1","face2"),Toast.LENGTH_SHORT).show();
                    new Thread(){
                        public void run(){
                            Youtu faceYoutu=new Youtu(APP_ID,SECRET_ID,SECRET_KEY,Youtu.API_TENCENTYUN_END_POINT);
                            try {
                                JSONObject json=faceYoutu.FaceCompare(myBitMap1,myBitMap2);
                                Log.i(TAG, "onClick: json数据"+json.toString());
                                Looper.prepare();
                                if(json.getInt("errorcode")!=0){
                                    Toast.makeText(CompareActivity.this,"无法识别,请换一组试试",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(CompareActivity.this,"相似度:"+String.format("%.2f",json.getDouble("similarity")),Toast.LENGTH_SHORT).show();
                                }
                                Looper.loop();
//                                if(myBitMap1!=null){
//                                    myBitMap1.recycle();
//                                }
//                                if(myBitMap2!=null){
//                                    myBitMap2.recycle();
//                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (KeyManagementException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
            }
            default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE1&&resultCode==RESULT_OK){
            if(data!=null) {
                Uri uri = data.getData();
                Log.i(TAG, "onActivityResult: "+uri.toString());
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    myBitMap1=bitmap;
                    addPhoto1.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                return;
            }
        }else if(requestCode==REQUEST_CODE2&&resultCode==RESULT_OK){
            if(data!=null) {
                Uri uri = data.getData();
                Log.i(TAG, "onActivityResult: "+uri.toString());
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    myBitMap2=bitmap;
                    addPhoto2.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        if(!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,this,mLoaderCallBack);
        }
        mJavaDetection=mLoaderCallBack.getMJavaDetector();
        super.onResume();
    }

    private LoaderCallBack mLoaderCallBack=new LoaderCallBack(this);
}
