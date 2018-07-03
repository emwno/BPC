package com.aashir.bpc.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aashir.bpc.R;

public class MaterialDialog {

    private final static int BUTTON_BOTTOM = 9;

    private boolean mCancel = true;
    private Context mContext;
    private AlertDialog mAlertDialog;
    private MaterialDialog.Builder mBuilder;
    private View mView;
    private boolean mHasShow = false;

    public MaterialDialog(Context context) {
        this.mContext = context;
    }

    public void show() {
        if (!mHasShow)
            mBuilder = new Builder();
        else
            mAlertDialog.show();
        mHasShow = true;
    }

    public View getView() {
        return mView;
    }

    private class Builder {

        private Window mAlertDialogWindow;

        private Builder() {
            mAlertDialog = new AlertDialog.Builder(mContext).create();
            mAlertDialog.show();

            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            mAlertDialogWindow = mAlertDialog.getWindow();
            mView = LayoutInflater.from(mContext).inflate(R.layout.dialog, null);

            mAlertDialogWindow.setBackgroundDrawableResource(R.drawable.dialog_window);
            mAlertDialogWindow.setContentView(mView);
            mAlertDialog.setCanceledOnTouchOutside(mCancel);
        }
    }

}