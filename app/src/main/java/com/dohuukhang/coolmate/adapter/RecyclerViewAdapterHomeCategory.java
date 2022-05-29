package com.dohuukhang.coolmate.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dohuukhang.coolmate.Object.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dohuukhang.coolmate.HomeFragment;
import com.dohuukhang.coolmate.Object.Category;
import com.dohuukhang.coolmate.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapterHomeCategory extends RecyclerView.Adapter<RecyclerViewAdapterHomeCategory.MyViewHolder>{
    private HomeFragment mContext;
    private List<Category> mData;
    List<LinearLayout> cardViewList = new ArrayList<>();
    public RecyclerViewAdapterHomeCategory(HomeFragment mContext, List<Category> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext.getActivity());
        view = mInflater.inflate(R.layout.button_home_noibat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_category_title.setText(mData.get(position).getTitle());
        cardViewList.add(holder.cardView);
        cardViewList.get(0).setBackgroundResource(R.drawable.khung2);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (LinearLayout cardView : cardViewList) {
                    cardView.setBackgroundResource(R.drawable.khung);
                }
                holder.cardView.setBackgroundResource(R.drawable.khung2);

                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext.getActivity(), LinearLayoutManager.HORIZONTAL, false);
                final RecyclerView recyclerView = mContext.root.findViewById(R.id.recyclerView_home_noibat);
                recyclerView.setLayoutManager(layoutManager);

                switch (mData.get(position).getTitle()) {
                    case "Tất cả":
                        mContext.reference = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product");
                        mContext.reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mContext.lstNoibat = new ArrayList<Product>();
                                List<Product> full = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Product p = dataSnapshot1.getValue(Product.class);
                                    full.add(p);
                                }
                                Collections.shuffle(full);

                                for (int i = 0; i < 3; ++i)
                                    mContext.lstNoibat.add(full.get(i));

                                RecyclerViewAdapter myAdapterNoibat = new RecyclerViewAdapter(mContext.getContext(), mContext.lstNoibat);
                                recyclerView.setAdapter(myAdapterNoibat);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(mContext.getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case "Áo":
                        mContext.refAo = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product").orderByChild("danhMuc").equalTo("Áo");
                        mContext.refAo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mContext.lstAo = new ArrayList<Product>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Product p = dataSnapshot1.getValue(Product.class);
                                    mContext.lstAo.add(p);
                                }
                                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(mContext.getContext(), mContext.lstAo);
                                recyclerView.setAdapter(myAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(mContext.getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case "Quần":
                        mContext.refQuan = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product").orderByChild("danhMuc").equalTo("Quần");
                        mContext.refQuan.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mContext.lstQuan = new ArrayList<Product>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Product p = dataSnapshot1.getValue(Product.class);
                                    mContext.lstQuan.add(p);
                                }
                                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(mContext.getContext(), mContext.lstQuan);
                                recyclerView.setAdapter(myAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(mContext.getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_category_title;
        LinearLayout cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_category_title = (TextView) itemView.findViewById(R.id.home_btn_title);
            cardView = (LinearLayout) itemView.findViewById(R.id.home_btn_noibat);
        }
    }
}
