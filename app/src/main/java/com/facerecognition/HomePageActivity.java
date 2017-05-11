package com.facerecognition;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

import fragments.AboutUsFragment;
import fragments.HomeFragment;
import fragments.NewsFragment;

/**
 * Created by marsor on 2017/5/11.
 */

public class HomePageActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private ArrayList<Fragment> fragments;
    private BottomNavigationBar navigation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        init();
    }

    private void init(){
        navigation= (BottomNavigationBar) findViewById(R.id.navigation);
        navigation.addItem(new BottomNavigationItem(R.mipmap.home,"Home"))
                .addItem(new BottomNavigationItem(R.mipmap.news,"News"))
                .addItem(new BottomNavigationItem(R.mipmap.about,"About Us"))
                .initialise();
        fragments=getFragments();
        setDefaultFragment();
        navigation.setTabSelectedListener(this);
    }

    private void setDefaultFragment(){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.replace(R.id.layFragment,HomeFragment.newInstance());
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments(){
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(NewsFragment.newInstance());
        fragments.add(AboutUsFragment.newInstance());
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if(fragments!=null){
            if(position<fragments.size()){
                FragmentManager fm=getFragmentManager();
                FragmentTransaction transaction=fm.beginTransaction();
                Fragment fragment=fragments.get(position);
                if(fragment.isAdded()){
                    transaction.replace(R.id.layFragment,fragment);
                }else {
                    transaction.add(R.id.layFragment,fragment);
                }
                transaction.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if(fragments!=null){
            if(position<fragments.size()){
                FragmentManager fm=getFragmentManager();
                FragmentTransaction transaction=fm.beginTransaction();
                Fragment fragment=fragments.get(position);
                transaction.remove(fragment);
                transaction.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }
}
