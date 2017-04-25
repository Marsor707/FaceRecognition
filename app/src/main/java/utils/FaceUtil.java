package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvCalcHist;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_COMP_CORREL;
import static org.bytedeco.javacpp.opencv_imgproc.CV_COMP_INTERSECT;
import static org.bytedeco.javacpp.opencv_imgproc.cvCompareHist;
import static org.bytedeco.javacpp.opencv_imgproc.cvNormalizeHist;

/**
 * Created by Seven on 2017/4/18.
 */

public class FaceUtil {
    private static final String TAG="FaceUtil";

    private FaceUtil(){

    }

    public static boolean saveImage(Mat image, Rect rect, String fileName){
        Mat grayMat=new Mat();
        Imgproc.cvtColor(image,grayMat, Imgproc.COLOR_RGB2GRAY);
        Mat sub=grayMat.submat(rect);
        Mat mat=new Mat();
        Size size=new Size(100,100);
        Imgproc.resize(sub,mat,size);
        return Imgcodecs.imwrite(getFilePath(fileName),mat);
    }

    public static Bitmap getImage(String filename){
        String filePath=getFilePath(filename);
        if(TextUtils.isEmpty(filePath)){
            return null;
        }else{
            return BitmapFactory.decodeFile(filePath);
        }
    }

    public static double compare(String fileName){

        String filePath=getFilePath(fileName);
        opencv_core.IplImage image=cvLoadImage(filePath,CV_LOAD_IMAGE_GRAYSCALE);
        if(image==null){
            return -1;
        }else{
            double average=0;
            int l_bins = 256;
            int hist_size[] = {l_bins};
            float v_ranges[] = {0, 255};
            float ranges[][] = {v_ranges};
            opencv_core.IplImage imageArr[] ={image};
            opencv_core.CvHistogram Histogram= opencv_core.CvHistogram.create(1,hist_size,0,ranges,1);
            cvCalcHist(imageArr,Histogram,0,null);
            cvNormalizeHist(Histogram,100.0);
            for(int i=0;i<5;i++){
                String filePath2=getFilePath("face"+i);
                opencv_core.IplImage image2=cvLoadImage(filePath2,CV_LOAD_IMAGE_GRAYSCALE);
                opencv_core.IplImage imageArr2[] ={image2};
                opencv_core.CvHistogram Histogram2= opencv_core.CvHistogram.create(1,hist_size,0,ranges,1);
                cvCalcHist(imageArr2,Histogram2,0,null);
                cvNormalizeHist(Histogram2,100.0);
                double d1=cvCompareHist(Histogram,Histogram2,CV_COMP_CORREL)*100;
                double d2=cvCompareHist(Histogram,Histogram2,CV_COMP_INTERSECT);
                average+=(d1+d2)/2;
            }
            Log.i(TAG, "compare: 平均相似度"+average/5);
            return average/5;
        }
    }

    private static String getFilePath(String filename){
        if(TextUtils.isEmpty(filename)){
            return null;
        }else {
            return Environment.getExternalStorageDirectory() + "/FaceStorage/" + filename + ".jpg";
        }
    }
}
