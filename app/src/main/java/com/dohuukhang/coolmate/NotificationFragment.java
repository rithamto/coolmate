package com.dohuukhang.coolmate;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dohuukhang.coolmate.Object.Category;
import com.dohuukhang.coolmate.Object.Notification;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.activity.SearchActivity;
import com.dohuukhang.coolmate.adapter.RecyclerViewAdapter;
import com.dohuukhang.coolmate.adapter.ThongbaoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment implements View.OnClickListener{
    public View root;
    RecyclerView recyclerViewThongbao;
    public DatabaseReference reference;
    public ArrayList<Notification> Listthongbao;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.notification_fragment, container, false);
        Loaddata();
        return root;
    }

    @Override
    public void onClick(View view) {

    }

    public void Loaddata(){
        LinearLayoutManager layoutManagerThongbao = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewThongbao = (RecyclerView) root.findViewById(R.id.recyclerView_thongbao);
        recyclerViewThongbao.setLayoutManager(layoutManagerThongbao);

        reference = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Listthongbao = new ArrayList<>();
                List<Notification> full = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Notification n = dataSnapshot1.getValue(Notification.class);
                    full.add(n);
                }
                Collections.shuffle(full);

                for (int i = 0; i < 2; ++i) {
                    Listthongbao.add(full.get(i));
                }

                ThongbaoAdapter myAdapterthongbao = new ThongbaoAdapter(getContext(), Listthongbao);
                recyclerViewThongbao.setAdapter(myAdapterthongbao);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
