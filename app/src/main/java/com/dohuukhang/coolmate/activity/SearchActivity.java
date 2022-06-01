package com.dohuukhang.coolmate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.R;
import com.dohuukhang.coolmate.adapter.RecyclerViewAdapterGioHang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchActivity extends AppCompatActivity {
    EditText searchText;
    ImageView back, chonsize;
    DatabaseReference reference;
    ArrayList<Product> lstSearch;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.edt_search);
        back = findViewById(R.id.btn_search_back);
        chonsize = findViewById(R.id.btn_search_choose);
        recyclerView = findViewById(R.id.recyclerView_search);

        lstSearch = new ArrayList<Product>();
        loadData();

        searchText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        if (searchText != null) {
            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    search(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        chonsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ChonsizeActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void search(String s) {
        ArrayList<Product> myList = new ArrayList<>();
        for (Product object : lstSearch) {
            if (object.getTen().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
        }
        LinearLayoutManager layoutManagerGioHang = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        final RecyclerView recyclerViewGioHang = (RecyclerView) findViewById(R.id.recyclerView_search);
        recyclerViewGioHang.setLayoutManager(layoutManagerGioHang);
        RecyclerViewAdapterGioHang myAdapterGioHang = new RecyclerViewAdapterGioHang(SearchActivity.this, myList);
        recyclerViewGioHang.setAdapter(myAdapterGioHang);
    }

    private void loadData() {
        String search = "";

        Bundle getSearch = getIntent().getExtras();
        if (getSearch != null) {
            search = getSearch.getString("Search");
            searchText.setText(search);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerViewAdapterGioHang myAdapter = new RecyclerViewAdapterGioHang(SearchActivity.this, lstSearch);
        recyclerView.setAdapter(myAdapter);

        reference = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product");
        final String finalSearch = search;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product p = dataSnapshot1.getValue(Product.class);
                    lstSearch.add(p);
                }
                myAdapter.notifyDataSetChanged();
                search(finalSearch);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        Collections.sort(lstSearch, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return (p1.getTen().compareTo(p2.getTen()));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
