package interfaces;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Created by Seven on 2017/4/19.
 */

public interface OnFaceDetectorListener {
    void onFace(Mat mat, Rect rect);
}
