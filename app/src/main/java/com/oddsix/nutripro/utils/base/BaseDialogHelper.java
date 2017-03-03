package com.oddsix.nutripro.utils.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.MessagePattern;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

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
        if (mProgressDialog == null) {
            mProgressDialog = buildProgressDialog();
            mProgressDialog.setMessage(mContext.getString(R.string.progress_dialog_standard_msg));
        }
        mProgressDialog.show();
    }

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = buildProgressDialog();
            mProgressDialog.setMessage(msg);
        }
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

    public void showEditTextDialog(String title, String positiveText, String negativeText, final OnEditTextDialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(title).setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!input.getText().toString().isEmpty()) {
                    try {
                        listener.onInputConfirmed(Integer.valueOf(input.getText().toString()));
                    } catch (NumberFormatException e) {

                    }
                }
            }
        });
        builder.show();
    }

    public void showEditTextTextDialog(String title, String positiveText, final OnEditTextTextDialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(title).setCancelable(false);

        final EditText input = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onInputConfirmed(input.getText().toString());
            }
        });
        builder.show();
    }

    public interface OnEditTextDialogClickListener {
        void onInputConfirmed(int value);
    }

    public interface OnEditTextTextDialogClickListener {
        void onInputConfirmed(String text);
    }

    public void showAlertDialog(String msg, String positiveText, String negativeText, DialogInterface.OnClickListener postiveButtonListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext).setMessage(msg).setPositiveButton(positiveText, postiveButtonListener)
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showListDialog(String title, CharSequence[] options, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setItems(options, listener);
        builder.show();
    }

    public void showListDialog(String title, CharSequence[] options, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setOnCancelListener(cancelListener);
        builder.setTitle(title).setItems(options, listener);
        builder.show();
    }

}
