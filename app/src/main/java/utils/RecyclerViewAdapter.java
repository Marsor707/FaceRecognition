package utils;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facerecognition.R;

import org.w3c.dom.Text;

/**
 * Created by marsor on 2017/5/10.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Bitmap[] bitmaps;
    private int[] confidences;

    public RecyclerViewAdapter(Bitmap[] bitmaps,int[] confidences) {
        this.bitmaps = bitmaps;
        this.confidences=confidences;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.image_result_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageBitmap(bitmaps[position]);
        holder.confidence.setText("相似度: "+confidences[position]+"%");
    }

    @Override
    public int getItemCount() {
        return bitmaps.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView confidence;

        public ViewHolder(View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.image_result);
            confidence= (TextView) itemView.findViewById(R.id.txt_confidence);
        }
    }
}
