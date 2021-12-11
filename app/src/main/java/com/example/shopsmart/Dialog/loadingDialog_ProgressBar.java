package com.example.shopsmart.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.example.shopsmart.R;

public class loadingDialog_ProgressBar {
    private Context context;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;


    public loadingDialog_ProgressBar(Context context) {
        this.context = context;
    }

    public void startLoading_DialogProgressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        this.inflater = LayoutInflater.from(context);
        builder.setView(this.inflater.inflate(R.layout.dialog_progressbar, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissLoading_DialogProgressBar() {
        alertDialog.dismiss();
    }
}
