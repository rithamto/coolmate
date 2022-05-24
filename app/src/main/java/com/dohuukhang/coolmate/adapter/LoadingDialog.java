package com.dohuukhang.coolmate.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.dohuukhang.coolmate.R;

public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
