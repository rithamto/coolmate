package com.dohuukhang.coolmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dohuukhang.coolmate.R;

public class ProductdetailActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        textView = findViewById(R.id.mota);

        // Recieve data
        Intent intent = getIntent();
        textView.setText(intent.getExtras().getString("MoTa"));
    }
}
