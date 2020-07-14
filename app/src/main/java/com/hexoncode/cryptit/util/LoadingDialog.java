package com.hexoncode.cryptit.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.hexoncode.cryptit.R;

public class LoadingDialog extends AlertDialog.Builder {

    private AlertDialog dialog;
    private boolean showing = false;

    public LoadingDialog(Context context) {
        super(context);
    }

    public void showLoading() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        setView(view);
        setCancelable(false);
        dialog = create();
        dialog.show();
        int size = (int) Utils.pxFromDp(getContext(), 120);
        dialog.getWindow().setLayout(size, size);
        this.showing = true;
    }

    public void hideLoading() {

        if (dialog != null) {
            dialog.dismiss();
            this.showing = false;
        }

    }

    public boolean isShowing() {
        return showing;
    }
}
