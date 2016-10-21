package com.oddsix.nutripro.utils.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.oddsix.nutripro.R;

/**
 * Created by Filippe on 16/10/16.
 */

public class BaseDialogHelper {
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public BaseDialogHelper(Context context) {
        mContext = context;
    }

    //PROGRESS DIALOG METHODS

    /**
     * Shows progress dialog with standard message.
     */
    public void showProgressDialog() {
        mProgressDialog = buildProgressDialog();
        mProgressDialog.setMessage(mContext.getString(R.string.progress_dialog_standard_msg));
        mProgressDialog.show();
    }

    public void showProgressDialog(String msg) {
        mProgressDialog = buildProgressDialog();
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private ProgressDialog buildProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public void showListDialog(String title, String negativeText, CharSequence[] options, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(title).setItems(options, listener);

        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    public void showListDialog(String title, CharSequence[] options, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setItems(options, listener);
        builder.show();
    }

}
