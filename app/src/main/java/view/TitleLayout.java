package view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facerecognition.R;

/**
 * Created by Seven on 2017/4/21.
 */

public class TitleLayout extends RelativeLayout {
    private TextView titleView;
    private Button backButton;
    private String title;
    private Boolean visible;
    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        title=typedArray.getString(R.styleable.TopBar_title);
        visible=typedArray.getBoolean(R.styleable.TopBar_backButtonVisible,true);
        typedArray.recycle();
        LayoutInflater.from(context).inflate(R.layout.title,this);
        titleView= (TextView) findViewById(R.id.title);
        backButton= (Button) findViewById(R.id.back);
        if(title!=null){
            titleView.setText(title);
        }
        if(visible==true){
            backButton.setVisibility(View.VISIBLE);
        }else{
            backButton.setVisibility(View.INVISIBLE);
        }
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });
    }
}
