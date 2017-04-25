package view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import com.facerecognition.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraGLSurfaceView;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import interfaces.OnFaceDetectorListener;

/**
 * Created by Seven on 2017/4/16.
 */

public class CameraView extends JavaCameraView implements CameraBridgeViewBase.CvCameraViewListener2{
    private static final String TAG="CameraView";
    private OnFaceDetectorListener onFaceDetectorListener;
    private CascadeClassifier mJavaDetector;
    private Mat mRgba;
    private Mat mGray;
    private float mRelativeFaceSize=0.2f;
    private int mAbsoluteFaceSize=0;
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);

    public CameraView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public boolean loadOpenCV(Context context){
        boolean isLoaded= OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,context,mLoaderCallback);
        if(isLoaded){
            setCvCameraViewListener(this);
        }else{
            Log.i(TAG, "loadOpenCV: 加载失败");
        }
        return isLoaded;
    }

    private boolean isLoadSuccess=false;
    private BaseLoaderCallback mLoaderCallback=new BaseLoaderCallback(getContext().getApplicationContext()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:{
                    Log.i(TAG, "onManagerConnected: 加载成功");
                    isLoadSuccess=true;
                    try{
                        InputStream is=getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir=getContext().getApplicationContext().getDir("cascade",Context.MODE_PRIVATE);
                        File cascadeFile=new File(cascadeDir,"lbpcascade_frontalface.xml");
                        FileOutputStream os=new FileOutputStream(cascadeFile);
                        byte[] buffer=new byte[4096];
                        int bytesRead;
                        while((bytesRead=is.read(buffer))!=-1){
                            os.write(buffer,0,bytesRead);
                        }
                        is.close();
                        os.close();
                        mJavaDetector=new CascadeClassifier(cascadeFile.getAbsolutePath());
                        if(mJavaDetector.empty()){
                            Log.e(TAG, "onManagerConnected: 级联分类器加载失败");
                            mJavaDetector=null;
                        }else{
                            Log.i(TAG, "onManagerConnected: 导入级联分类器成功"+cascadeFile.getAbsolutePath());
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                        Log.e(TAG, "onManagerConnected: 找不到文件");
                    }
                    enableView();
                    break;
                }
            }
            super.onManagerConnected(status);
        }
    };

    @Override
    public void enableView() {
        if(isLoadSuccess){
            super.enableView();
        }
    }

    @Override
    public void disableView() {
        if(isLoadSuccess){
            super.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray=new Mat();
        mRgba=new Mat();
        //this.setRotation(90);
        Log.i(TAG, "onCameraViewStarted: 打开摄像头");
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
        Log.i(TAG, "onCameraViewStopped: 关闭摄像头");
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba=inputFrame.rgba();
        mGray=inputFrame.gray();

        if(mAbsoluteFaceSize==0){
            int height=mGray.rows();
            if(Math.round(height*mRelativeFaceSize)>0){
                mAbsoluteFaceSize=Math.round(height*mRelativeFaceSize);
            }
        }
        if(mJavaDetector!=null){
            MatOfRect faces=new MatOfRect();
            mJavaDetector.detectMultiScale(mGray,faces,1.1,3,2,new Size(mAbsoluteFaceSize,mAbsoluteFaceSize),new Size(mGray.width(),mGray.height()));

            Rect[] facesArray=faces.toArray();
            for (Rect aFacesArray:facesArray){
                Imgproc.rectangle(mRgba, aFacesArray.tl(), aFacesArray.br(), FACE_RECT_COLOR, 3);
                if(onFaceDetectorListener!=null){
                    onFaceDetectorListener.onFace(mRgba,aFacesArray);
                }
            }
        }
        return mRgba;
    }

    //设置回调方法
    public void setOnFaceDetectorListener(OnFaceDetectorListener listener){
        onFaceDetectorListener=listener;
    }

    //切换前置摄像头
    public void switchCamera() {
        int numberOfCameras = 0;
        numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraIdx = 0; cameraIdx < numberOfCameras; cameraIdx++) {
            Camera.getCameraInfo(cameraIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.i(TAG, "switchCamera: "+cameraIdx);
                super.setCameraIndex(cameraIdx);
                break;
            }
        }
    }
}
