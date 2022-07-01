package com.dohuukhang.coolmate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dohuukhang.coolmate.NotificationFragment;
import com.dohuukhang.coolmate.Object.Notification;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.R;
import com.dohuukhang.coolmate.activity.ProductActivity;

import java.util.ArrayList;
import java.util.List;

public class ThongbaoAdapter extends RecyclerView.Adapter<ThongbaoAdapter.MyViewHolder>{
    private Context mContext ;
    private List<Notification> mData ;
    public ThongbaoAdapter(Context mContext, List<Notification> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ThongbaoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_notification,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tieude.setText(mData.get(position).getTieude());
        Glide.with(mContext).load(mData.get(position).getAnh()).placeholder(R.drawable.noimage).into(holder.anh);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tieude;
        ImageView anh;
        LinearLayout cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tieude = (TextView) itemView.findViewById(R.id.tieude);
            anh = (ImageView) itemView.findViewById(R.id.imageView_thongbao);
            cardView = (LinearLayout) itemView.findViewById(R.id.card_thongbao);
        }
    }
}
