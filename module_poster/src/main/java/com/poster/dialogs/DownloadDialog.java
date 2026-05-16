package com.poster.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DownloadDialog {
    private ProgressDialog progressDialog;
    private DialogListener iml;
    private Context context;

    public DownloadDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        progressDialog.setTitle("正在生成，请稍等...");
        // 设置ProgressDialog 提示信息
        progressDialog.setMessage("当前进度:");
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iml.dialogEvent();
            }
        });
        progressDialog.setCanceledOnTouchOutside(false);

    }

    public void show() {
        progressDialog.show();
        progressDialog.setMax(100);
    }

    public void dismiss() {
        progressDialog.dismiss();
    }

    public void updateProgress(int size) {
        progressDialog.setProgress(size);
    }

    public void setDialogListener(DialogListener listener) {
        this.iml = listener;
    }

    public interface DialogListener {
        void dialogEvent();
    }
}
