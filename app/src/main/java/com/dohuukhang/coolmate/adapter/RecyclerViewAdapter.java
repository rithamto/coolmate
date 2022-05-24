package com.dohuukhang.coolmate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.R;
import com.dohuukhang.coolmate.activity.ProductActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext ;
    private List<Product> mData ;

    public RecyclerViewAdapter(Context mContext, List<Product> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_daxem,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_category_title.setText(mData.get(position).getTen());
        Glide.with(mContext).load(mData.get(position).getHinhAnh()).placeholder(R.drawable.noimage).into(holder.img_category_thumbnail);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductActivity.class);

                // passing data to the book activity
                intent.putExtra("Ten",mData.get(position).getTen());
                // start the activity
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_category_title;
        ImageView img_category_thumbnail;
        LinearLayout cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_category_title = (TextView) itemView.findViewById(R.id.category_title_id) ;
            img_category_thumbnail = (ImageView) itemView.findViewById(R.id.category_img_id);
            cardView = (LinearLayout) itemView.findViewById(R.id.cardview_id);
        }
    }
}
