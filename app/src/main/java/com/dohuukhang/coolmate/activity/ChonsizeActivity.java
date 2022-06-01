package com.dohuukhang.coolmate.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dohuukhang.coolmate.R;

public class ChonsizeActivity extends AppCompatActivity implements View.OnClickListener{
    SeekBar chieucao,cannang;
    Spinner sanpham;
    ImageView loaicothe1, loaicothe2, loaicothe3,back, sugest_img;
    TextView num1,num2, thin_num, blance_num, plumb_num;
    int Chieucao = 0, Cannang = 0, Loaicothe = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_size);
        thin_num = findViewById(R.id.thin_btn_num);
        blance_num = findViewById(R.id.blance_btn_num);
        plumb_num = findViewById(R.id.plump_btn_num);
        sugest_img = findViewById(R.id.sugest_img);
        chieucao = findViewById(R.id.Sb_chieucao);
        cannang = findViewById(R.id.Sb_cannang);
        sanpham = findViewById(R.id.chon_size);
        loaicothe1 = findViewById(R.id.thin_btn);
        loaicothe2 = findViewById(R.id.blance_btn);
        loaicothe3 = findViewById(R.id.plump_btn);
        back = findViewById(R.id.chonsize_back);
        num1 = findViewById(R.id.num_chieucao);
        num2 = findViewById(R.id.num_cannang);
        chieucao.setMax(183);
        chieucao.setMin(155);
        cannang.setMin(48);
        cannang.setMax(84);
        back.setOnClickListener(this);
        loaicothe3.setOnClickListener(this);
        loaicothe2.setOnClickListener(this);
        loaicothe1.setOnClickListener(this);
        chieucao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Chieucao = chieucao.getProgress();
                String s = String.valueOf(Chieucao);
                num1.setText(s);
                loadData();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String s = String.valueOf(Chieucao);
                num1.setText(s);

            }
        });
        cannang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Cannang = cannang.getProgress();
                String s = String.valueOf(Cannang);
                num2.setText(s);
                loadData();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String s = String.valueOf(Cannang);
                num2.setText(s);

            }
        });
    }
    private void loadData(){
        if(Loaicothe == 1 && Chieucao <= 163){
            Glide.with(ChonsizeActivity.this).load(R.drawable.letters).placeholder(R.drawable.noimage).into(sugest_img);
        }
        else if(Loaicothe == 1 && Chieucao > 163 && Chieucao < 169){
            Glide.with(ChonsizeActivity.this).load(R.drawable.letterm).placeholder(R.drawable.noimage).into(sugest_img);
        }
        else if(Loaicothe == 1 && Chieucao >= 169 && Chieucao < 175){
            Glide.with(ChonsizeActivity.this).load(R.drawable.letterl).placeholder(R.drawable.noimage).into(sugest_img);
        }
        else if(Loaicothe == 1 && Chieucao >= 175 && Chieucao < 179){
            Glide.with(ChonsizeActivity.this).load(R.drawable.letterxl).placeholder(R.drawable.noimage).into(sugest_img);
        }
        else if(Loaicothe == 1 && Chieucao >= 179){
            Glide.with(ChonsizeActivity.this).load(R.drawable.letterxxl).placeholder(R.drawable.noimage).into(sugest_img);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.thin_btn:
                Loaicothe = 1;
                thin_num.setTextColor(Color.BLUE);
                blance_num.setTextColor(Color.BLACK);
                plumb_num.setTextColor(Color.BLACK);
                break;
            case R.id.blance_btn:
                Loaicothe = 2;
                thin_num.setTextColor(Color.BLACK);
                blance_num.setTextColor(Color.BLUE);
                plumb_num.setTextColor(Color.BLACK);
                break;
            case R.id.plump_btn:
                Loaicothe = 3;
                thin_num.setTextColor(Color.BLACK);
                blance_num.setTextColor(Color.BLACK);
                plumb_num.setTextColor(Color.BLUE);
                break;
            case R.id.chonsize_back:
                finish();
                break;
        }
    }
}
