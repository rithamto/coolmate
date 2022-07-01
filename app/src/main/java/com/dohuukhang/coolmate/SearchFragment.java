package com.dohuukhang.coolmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dohuukhang.coolmate.activity.GioHangActivity;
import com.dohuukhang.coolmate.activity.SearchActivity;

public class SearchFragment extends Fragment implements View.OnClickListener{
    public View root;
    Intent intent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_search, container, false);
        loadData();
        return root;
    }

    @Override
    public void onClick(View view) {

    }
    private void loadData(){
        intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }
}
