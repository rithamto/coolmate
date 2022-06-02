package com.dohuukhang.coolmate.activity;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.dohuukhang.coolmate.adapter.SwipeToDeleteCallback;
import com.dohuukhang.coolmate.MainActivity;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.R;
import com.dohuukhang.coolmate.adapter.RecyclerViewAdapterGioHang;

import java.util.ArrayList;

public class GioHangActivity extends AppCompatActivity {
    DatabaseReference reference, delete;
    ArrayList<Product> lstGioHang = new ArrayList<>();
    FirebaseAuth mAuth;
    ConstraintLayout constraintLayout;
    RecyclerView recyclerViewGioHang;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://coolmate-578b6.appspot.com");
    ProgressBar loadingView;
    LinearLayout linearLayout;
    Button tieptuc, thanhtoan;
    boolean check;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        back = findViewById(R.id.giohang_back);
        constraintLayout = findViewById(R.id.layout_giohang);
        recyclerViewGioHang = findViewById(R.id.recyclerView_giohang);
        loadingView = findViewById(R.id.loading_view_giohang);
        linearLayout = findViewById(R.id.layout_noProduct);
        tieptuc = findViewById(R.id.giohang_tieptuc);
        thanhtoan = findViewById(R.id.giohang_thanhtoan);

        loadData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GioHangActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GioHangActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewGioHang.setLayoutManager(layoutManager2);
        final RecyclerViewAdapterGioHang myAdapter = new RecyclerViewAdapterGioHang(GioHangActivity.this, lstGioHang);
        recyclerViewGioHang.setAdapter(myAdapter);

        reference = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Cart").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstGioHang.clear();
                loadingView.setVisibility(GONE);
                if (dataSnapshot.exists()) linearLayout.setVisibility(GONE);

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product p = dataSnapshot1.getValue(Product.class);
                    lstGioHang.add(p);
                }
                myAdapter.notifyDataSetChanged();

                SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(GioHangActivity.this) {
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        final int position = viewHolder.getAdapterPosition();
                        final Product item = myAdapter.getData().get(position);
                        check = true;

                        myAdapter.removeItem(position);
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                check = false;
                                myAdapter.restoreItem(item, position);
                                recyclerViewGioHang.scrollToPosition(position);
                            }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (check) {
                                    StorageReference photoRef = storage.getReferenceFromUrl(item.getHinhAnh());
                                    delete = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Cart").child(currentUser.getUid()).child(item.getTen());
                                    delete.removeValue();

                                    photoRef.delete();
                                }
                            }
                        }, 2900);
                    }
                };
                ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
                itemTouchhelper.attachToRecyclerView(recyclerViewGioHang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GioHangActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
