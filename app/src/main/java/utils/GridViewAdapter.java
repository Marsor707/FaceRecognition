package utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facerecognition.R;

/**
 * Created by Seven on 2017/4/21.
 */

public class GridViewAdapter extends BaseAdapter {
    private static String TAG="GridViewAdapter";
    private Context context;
    private Integer[] images;
    private String[] names;

    public  GridViewAdapter(Context context,Integer[] images,String[] names){
        this.context=context;
        this.images=images;
        this.names=names;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view=View.inflate(context, R.layout.gridview_item,null);
            holder=new ViewHolder();
            holder.item_image= (ImageView) view.findViewById(R.id.gridView_img);
            holder.item_name= (TextView) view.findViewById(R.id.gridView_text);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.item_image.setImageResource(images[i]);
        holder.item_name.setText(names[i]);
//        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                SizeUtil.px2dip(context,SizeUtil.getDeviceHeight(context))*3/10);
//        view.setLayoutParams(layoutParams);
        return view;
    }

    class ViewHolder{
        private ImageView item_image;
        private TextView item_name;
    }
}
