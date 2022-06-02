package com.dohuukhang.coolmate.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dohuukhang.coolmate.Object.Comment;
import com.dohuukhang.coolmate.Object.Product;
import com.dohuukhang.coolmate.R;
import com.dohuukhang.coolmate.adapter.LoadingDialog;
import com.dohuukhang.coolmate.adapter.RecyclerViewAdapterComment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tensp, gia, danhmuc,  mota, currentUserName;
    private ImageView img;
    ImageView back, like, gioHang, vietnhanxet, currentUserImage;
    EditText edtComment;
    Button chonmua, xemthemmota;
//    public Query ref;
    DatabaseReference mData, ref;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://coolmate-578b6.appspot.com");
    FirebaseAuth mAuth;
    BottomSheetDialog bottomDialod1;
    String nhacc, noisanxuat, name, nguoiBan, id, tenkhachhang, imageLink, imageUser;
    ArrayList<Comment> lstComment;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tensp = findViewById(R.id.txt_tensp);
        gia = findViewById(R.id.txt_giasp);
        danhmuc = findViewById(R.id.txt_danhmuc);
        mota = findViewById(R.id.txt_mota);
        recyclerView = findViewById(R.id.recyclerView_comment);
        edtComment = findViewById(R.id.edt_currentuser_comment);
        chonmua = findViewById(R.id.btn_chonmua);
        img = findViewById(R.id.category_thumbnail);
        back = findViewById(R.id.btn_product_back);
        like = findViewById(R.id.btn_product_like);
        gioHang = findViewById(R.id.btn_choose);
        vietnhanxet = findViewById(R.id.btn_post);
        currentUserName = findViewById(R.id.currentuser_name);
        currentUserImage = findViewById(R.id.currentuser_comment);
        xemthemmota = findViewById(R.id.xemthem_mota);

        // Recieve data
        Intent intent = getIntent();
        name = intent.getExtras().getString("Ten");

        loadData();
//        loadComment();
//        getCurrentUser();

        back.setOnClickListener(this);
        like.setOnClickListener(this);
        gioHang.setOnClickListener(this);
        chonmua.setOnClickListener(this);
        vietnhanxet.setOnClickListener(this);
        xemthemmota.setOnClickListener(this);
    }

    private void createComment() {
        if (edtComment.getText().equals("")) {
            Toast.makeText(ProductActivity.this, "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
        } else {
            mData = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Comment comment = new Comment(edtComment.getText().toString(), id, tenkhachhang, imageUser);
            mData.child("Product").child(name).child("Comment").child(dateFormat.format(date)).setValue(comment);
            edtComment.setText(null);
            Toast.makeText(ProductActivity.this, "Đã gửi nhận xét", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {

        ref = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product").child(name);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    finish();
                    Toast.makeText(ProductActivity.this, "Sản phẩm này không còn nữa", Toast.LENGTH_SHORT).show();
                } else {
                    tensp.setText(dataSnapshot.child("Ten").getValue().toString());
                    gia.setText(dataSnapshot.child("Giatien").getValue().toString());
                    danhmuc.setText(dataSnapshot.child("danhMuc").getValue().toString());
                    mota.setText(dataSnapshot.child("MoTa").getValue().toString());
                    Glide.with(ProductActivity.this).load(dataSnapshot.child("HinhAnh").getValue().toString()).placeholder(R.drawable.noimage).into(img);
                    imageLink = dataSnapshot.child("HinhAnh").getValue().toString();

                    final String tempUrl = dataSnapshot.child("HinhAnh").getValue().toString();
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(ProductActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            ImageView photoView = new ImageView(ProductActivity.this);
                            Glide.with(ProductActivity.this).load(tempUrl).into(photoView);
                            photoView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.addContentView(photoView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this, "load thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComment() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        ref = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Product").child(name).child("Comment");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstComment = new ArrayList<Comment>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Comment p = dataSnapshot1.getValue(Comment.class);
                    lstComment.add(p);
                }
                final RecyclerViewAdapterComment myAdapter = new RecyclerViewAdapterComment(ProductActivity.this, lstComment);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this, "Không thể load comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addLike() {
        final LoadingDialog loadingDialog = new LoadingDialog(ProductActivity.this);
        loadingDialog.startLoadingDialog();
        mData = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://coolmate-578b6.appspot.com");
        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

        // Get the data from an ImageView as bytes
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                }
                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String url = task.getResult().toString();
                    Product product = new Product(tensp.getText().toString(), String.valueOf(url), gia.getText().toString(),
                            danhmuc.getText().toString(),  mota.getText().toString());
                    mData.child("Favourite").child(id).child(tensp.getText().toString()).setValue(product);
                    loadingDialog.dismissDialog();
                    Toast.makeText(ProductActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.dismissDialog();
                    Toast.makeText(ProductActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToCart() {
        final LoadingDialog loadingDialog = new LoadingDialog(ProductActivity.this);
        loadingDialog.startLoadingDialog();

        mData = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://coolmate-578b6.appspot.com");
        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

        // Get the data from an ImageView as bytes
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                }
                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String url = task.getResult().toString();
                    Product product = new Product(tensp.getText().toString(), String.valueOf(url), gia.getText().toString(),
                            danhmuc.getText().toString(),  mota.getText().toString());
                    mData.child("Cart").child(name).child(tensp.getText().toString()).setValue(product);
                    loadingDialog.dismissDialog();
                    Toast.makeText(ProductActivity.this, "Đã thêm vào Giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.dismissDialog();
                    Toast.makeText(ProductActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getCurrentUser() {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        id = currentUser.getUid();

        ref = FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("User").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserName.setText(dataSnapshot.child("ten").getValue().toString());
                Glide.with(ProductActivity.this).load(dataSnapshot.child("anhDaiDien").getValue().toString()).placeholder(R.drawable.noimage).into(currentUserImage);
                tenkhachhang = dataSnapshot.child("ten").getValue().toString();
                imageUser = dataSnapshot.child("anhDaiDien").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product_back:
                finish();
                break;
            case R.id.text_xemthembaohanh:
                bottomDialod1.show();
                break;
            case R.id.btn_product_like:
                if (nguoiBan.equals(id))
                    Toast.makeText(ProductActivity.this, "Đây là sản phẩm bạn đăng bán", Toast.LENGTH_SHORT).show();
                else addLike();
                break;
            case R.id.btn_choose:
                Intent intent = new Intent(ProductActivity.this, ChonsizeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_chonmua:
                addToCart();
                break;
            case R.id.btn_post:
                createComment();
                break;
            case R.id.xemthem_mota:
                Intent intent1 = new Intent(ProductActivity.this, ProductdetailActivity.class);
                intent1.putExtra("mota", mota.getText());
                startActivity(intent1);
                break;
        }
    }
}