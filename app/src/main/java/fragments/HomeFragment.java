package fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.facerecognition.CaptureActivity;
import com.facerecognition.CompareActivity;
import com.facerecognition.ModeActivity;
import com.facerecognition.R;
import com.facerecognition.SearchActivity;
import com.facerecognition.TrackActivity;
import com.youth.banner.Banner;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.Arrays;

import utils.GlideImageLoader;
import utils.GridViewAdapter;

/**
 * Created by marsor on 2017/5/11.
 */

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{
    private Banner banner;
    private GridView gridView;
    private GridViewAdapter adapter;
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE = 0;
    private static final String[] permissions={
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    private static Integer[] bannerImages={R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4};
    private static Integer[] gridImages={R.mipmap.track,R.mipmap.compare,R.mipmap.mode,R.mipmap.detect,R.mipmap.capture,R.mipmap.search};
    private static String[] gridTexts={"人像跟踪","人脸比对","人像建模","活体检测","人脸捕获","人脸搜索"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_main,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        //MPermissions.requestPermissions(getActivity(), REQUEST_CODE, permissions);
    }

    private void init(){
        //图片轮播
        banner= (Banner) getActivity().findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(Arrays.asList(bannerImages));
        banner.setDelayTime(5000);
        banner.start();
        //gridView
        gridView= (GridView) getActivity().findViewById(R.id.gridView);
        adapter=new GridViewAdapter(getActivity(),gridImages,gridTexts);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:{
                Intent intent=new Intent(getActivity(),TrackActivity.class);
                startActivity(intent);
                break;
            }
            case 1:{
                Intent intent=new Intent(getActivity(),CompareActivity.class);
                startActivity(intent);
                break;
            }
            case 2:{
                Intent intent=new Intent(getActivity(),ModeActivity.class);
                startActivity(intent);
                break;
            }
            case 4:{
                Intent intent=new Intent(getActivity(),CaptureActivity.class);
                startActivity(intent);
                break;
            }
            case 5:{
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }

    //处理权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //授权成功
    @PermissionGrant(REQUEST_CODE)
    public void permissionSuccess() {
        Log.i(TAG, "permissionSuccess: 授权成功");
    }

    //授权失败
    @PermissionDenied(REQUEST_CODE)
    public void permissionFail() {
        Log.i(TAG, "permissionFail: 授权失败");
    }
}
