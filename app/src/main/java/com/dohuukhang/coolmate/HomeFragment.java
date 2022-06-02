package com.dohuukhang.coolmate;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.dohuukhang.coolmate.activity.CategoryActivity;
import com.dohuukhang.coolmate.activity.NoiBatActivity;
import com.dohuukhang.coolmate.activity.SearchActivity;
import com.dohuukhang.coolmate.activity.YeuThichActivity;
import com.dohuukhang.coolmate.adapter.RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dohuukhang.coolmate.Object.Category;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.adapter.RecyclerViewAdapterHomeCategory;
import com.dohuukhang.coolmate.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment  implements View.OnClickListener{
    LinearLayout layoutDoraemon;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Product> lstDaxem;
    Intent intent;
    ViewPager viewpager;
    public ArrayList<Product> lstNoibat, lstAo, lstQuan;
    List<Category> lstBtnNoibat;
    public DatabaseReference reference, refDaxem;
    RecyclerView recyclerViewNoibat;
    ProgressBar loadingView, loadingViewNoibat;
    public Query refAo, refQuan;
    FirebaseAuth mAuth;
    Random rd;
    Button search, Ao, Quan;
    TextView xemthemYeuThich, xemthemNoiBat;
    ViewPager viewPager;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1500;
    final long PERIOD_MS = 3000;
    public View root;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        viewpager = root.findViewById(R.id.viewPager);
        xemthemYeuThich = root.findViewById(R.id.home_daxem_xemthem);
        xemthemNoiBat = root.findViewById(R.id.home_noibat_xemthem);
        search = root.findViewById(R.id.home_search);
        swipeRefreshLayout = root.findViewById(R.id.refreshLayout);
        Ao = root.findViewById(R.id.btn_Ao);
        Quan = root.findViewById(R.id.btn_Quan);
        viewPager = root.findViewById(R.id.viewPager);
        loadingView = root.findViewById(R.id.loading_view);
        loadingViewNoibat = root.findViewById(R.id.loading_noibat);
        layoutDoraemon = root.findViewById(R.id.layout_doraemon);

        search.setOnClickListener(this);
        Ao.setOnClickListener(this);
        Quan.setOnClickListener(this);
        xemthemYeuThich.setOnClickListener(this);
        xemthemNoiBat.setOnClickListener(this);

        setViewPager();
        setLstBtnNoibat();
        loadData();
        setLoadMoreAction();
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_Ao:
                intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("DanhMuc", "Áo");
                startActivity(intent);
                break;
            case R.id.btn_Quan:
                intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("DanhMuc", "Quần");
                startActivity(intent);
                break;
            case R.id.home_daxem_xemthem:
                intent = new Intent(getActivity(), YeuThichActivity.class);
                startActivity(intent);
                break;
            case R.id.home_noibat_xemthem:
                intent = new Intent(getActivity(), NoiBatActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void setViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext());
        viewpager.setAdapter(viewPagerAdapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 3) {
                    currentPage = 0;
                }
                viewpager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
    private void setLstBtnNoibat() {
        lstBtnNoibat = new ArrayList<>();
        lstBtnNoibat.add(new Category("Tất cả"));
        lstBtnNoibat.add(new Category("Áo"));
        lstBtnNoibat.add(new Category("Quần"));
        lstBtnNoibat.add(new Category("Áo Khoác"));
        lstBtnNoibat.add(new Category("Phụ kiện"));
        lstBtnNoibat.add(new Category("Khác"));

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewBtnNoibat = (RecyclerView) root.findViewById(R.id.recyclerView_home_button);
        recyclerViewBtnNoibat.setLayoutManager(layoutManager2);
        RecyclerViewAdapterHomeCategory myAdapterBtnNoibat = new RecyclerViewAdapterHomeCategory(this, lstBtnNoibat);
        recyclerViewBtnNoibat.setAdapter(myAdapterBtnNoibat);
    }
    public void loadData() {
        rd = new Random();
        LinearLayoutManager layoutManagerNoibat = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNoibat = (RecyclerView) root.findViewById(R.id.recyclerView_home_noibat);
        recyclerViewNoibat.setLayoutManager(layoutManagerNoibat);

        reference = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstNoibat = new ArrayList<>();
                loadingViewNoibat.setVisibility(GONE);
                List<Product> full = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product p = dataSnapshot1.getValue(Product.class);
                    full.add(p);
                }
                Collections.shuffle(full);

                for (int i = 0; i < 3; ++i)
                    lstNoibat.add(full.get(i));

                RecyclerViewAdapter myAdapterNoibat = new RecyclerViewAdapter(getContext(), lstNoibat);
                recyclerViewNoibat.setAdapter(myAdapterNoibat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManagerAo = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerViewAo = (RecyclerView) root.findViewById(R.id.recyclerView_home_Ao);
        recyclerViewAo.setLayoutManager(layoutManagerAo);

        refAo = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product").orderByChild("danhMuc").equalTo("Áo");
        refAo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstAo = new ArrayList<Product>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product p = dataSnapshot1.getValue(Product.class);
                    lstAo.add(p);
                }
                RecyclerViewAdapter myAdapterAo = new RecyclerViewAdapter(getContext(), lstAo);
                recyclerViewAo.setAdapter(myAdapterAo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManagerQuan = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerViewQuan = (RecyclerView) root.findViewById(R.id.recyclerView_home_quan);
        recyclerViewQuan.setLayoutManager(layoutManagerQuan);

        refQuan = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product").orderByChild("danhMuc").equalTo("Quần");
        refQuan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstQuan = new ArrayList<Product>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product p = dataSnapshot1.getValue(Product.class);
                    lstQuan.add(p);
                }
                RecyclerViewAdapter myAdapterQuanao = new RecyclerViewAdapter(getContext(), lstQuan);
                recyclerViewQuan.setAdapter(myAdapterQuanao);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

//        LinearLayoutManager layoutManagerDaxem = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        final RecyclerView recyclerViewDaxem = (RecyclerView) root.findViewById(R.id.recyclerView_home_daxem);
//        recyclerViewDaxem.setLayoutManager(layoutManagerDaxem);
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        refDaxem = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Favourite").child(currentUser.getUid());
//        refDaxem.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                loadingView.setVisibility(GONE);
//                lstDaxem = new ArrayList<Product>();
//                if (dataSnapshot.exists()) layoutDoraemon.setVisibility(GONE);
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Product p = dataSnapshot1.getValue(Product.class);
//                    lstDaxem.add(p);
//                }
//                RecyclerViewAdapter myAdapterDaxem = new RecyclerViewAdapter(getContext(), lstDaxem);
//                recyclerViewDaxem.setAdapter(myAdapterDaxem);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    private void setLoadMoreAction() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

}
