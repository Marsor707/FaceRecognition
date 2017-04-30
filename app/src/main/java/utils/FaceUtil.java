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
import static org.bytedeco.javacpp.opencv_core.CV_HIST_ARRAY;
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

    public static double compare(String fileName1,String fileName2){
        try {
            String pathFile1 = getFilePath(fileName1);
            String pathFile2 = getFilePath(fileName2);
            opencv_core.IplImage image1 = cvLoadImage(pathFile1, CV_LOAD_IMAGE_GRAYSCALE);
            opencv_core.IplImage image2 = cvLoadImage(pathFile2, CV_LOAD_IMAGE_GRAYSCALE);
            if (null == image1 || null == image2) {
                return -1;
            }

            int l_bins = 256;
            int hist_size[] = {l_bins};
            float v_ranges[] = {0, 255};
            float ranges[][] = {v_ranges};

            opencv_core.IplImage imageArr1[] = {image1};
            opencv_core.IplImage imageArr2[] = {image2};
            opencv_core.CvHistogram Histogram1 = opencv_core.CvHistogram.create(1, hist_size, CV_HIST_ARRAY, ranges, 1);
            opencv_core.CvHistogram Histogram2 = opencv_core.CvHistogram.create(1, hist_size, CV_HIST_ARRAY, ranges, 1);
            cvCalcHist(imageArr1, Histogram1, 0, null);
            cvCalcHist(imageArr2, Histogram2, 0, null);
            cvNormalizeHist(Histogram1, 100.0);
            cvNormalizeHist(Histogram2, 100.0);
            // 参考：http://blog.csdn.net/nicebooks/article/details/8175002
            double c1 = cvCompareHist(Histogram1, Histogram2, CV_COMP_CORREL) * 100;
            double c2 = cvCompareHist(Histogram1, Histogram2, CV_COMP_INTERSECT);
//            Log.i(TAG, "compare: ----------------------------");
//            Log.i(TAG, "compare: c1 = " + c1);
//            Log.i(TAG, "compare: c2 = " + c2);
//            Log.i(TAG, "compare: 平均值 = " + ((c1 + c2) / 2));
//            Log.i(TAG, "compare: ----------------------------");
            return (c1 + c2) / 2;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
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
