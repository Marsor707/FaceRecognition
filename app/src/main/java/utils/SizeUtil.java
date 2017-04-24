package utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Seven on 2017/4/24.
 */

public class SizeUtil {
    public static int getDeviceHeight(Context context){
        Resources resources=context.getResources();
        DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        return  displayMetrics.heightPixels;
    }

    public static int getDeviceWidth(Context context){
        Resources resources=context.getResources();
        DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int dip2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale+0.5f);
    }

    public static int px2dip(Context context,float pxValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
//        DisplayMetrics metrics=Resources.getSystem().getDisplayMetrics();
//        return Math.round(pxValue/((float)metrics.densityDpi/DisplayMetrics.DENSITY_DEFAULT));
    }
}
