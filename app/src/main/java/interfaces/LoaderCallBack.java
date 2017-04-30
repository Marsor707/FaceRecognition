package interfaces;

import android.content.Context;
import android.util.Log;

import com.facerecognition.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seven on 2017/4/27.
 */

public class LoaderCallBack extends BaseLoaderCallback implements LoaderCallbackInterface {
    private static final String TAG="BaseLoaderCallback";
    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private Context context;
    public LoaderCallBack(Context AppContext) {
        super(AppContext);
        this.context=AppContext;
    }

    @Override
    public void onManagerConnected(int status) {
        switch (status) {
            case LoaderCallbackInterface.SUCCESS:
            {
                Log.i(TAG, "OpenCV loaded successfully");

                // Load native library after(!) OpenCV initialization
                //System.loadLibrary("detection_based_tracker");

                try {
                    // load cascade file from application resources
                    InputStream is = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
                    File cascadeDir = context.getApplicationContext().getDir("cascade", Context.MODE_PRIVATE);
                    mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                    FileOutputStream os = new FileOutputStream(mCascadeFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    os.close();

                    mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                    if (mJavaDetector.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier");
                        mJavaDetector = null;
                    } else
                        Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                }

            } break;
            default:
            {
                super.onManagerConnected(status);
            } break;
        }
    }

    public CascadeClassifier getMJavaDetector(){
        return mJavaDetector;
    }
}
