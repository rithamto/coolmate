package com.dohuukhang.coolmate;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.dohuukhang.coolmate.Object.Category;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.adapter.RecyclerViewAdapterHomeCategory;
import com.dohuukhang.coolmate.adapter.ViewPagerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment  implements View.OnClickListener{
    ViewPager viewpager;
    public ArrayList<Product> lstNoibat;
    List<Category> lstBtnNoibat;
    public DatabaseReference reference, refDaxem;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1500;
    final long PERIOD_MS = 3000;
    public View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        viewpager = root.findViewById(R.id.viewPager);
        setViewPager();
        return root;
    }

    @Override
    public void onClick(View view) {
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
        lstBtnNoibat.add(new Category("Điện thoại - Máy tính"));
        lstBtnNoibat.add(new Category("Thời trang"));
        lstBtnNoibat.add(new Category("Sách"));
        lstBtnNoibat.add(new Category("Làm đẹp - Sức khỏe"));
        lstBtnNoibat.add(new Category("Đồ gia dụng"));

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewBtnNoibat = (RecyclerView) root.findViewById(R.id.recyclerView_home_button);
        recyclerViewBtnNoibat.setLayoutManager(layoutManager2);
        RecyclerViewAdapterHomeCategory myAdapterBtnNoibat = new RecyclerViewAdapterHomeCategory(this, lstBtnNoibat);
        recyclerViewBtnNoibat.setAdapter(myAdapterBtnNoibat);
    }
}
