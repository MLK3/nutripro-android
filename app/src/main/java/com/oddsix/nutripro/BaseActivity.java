package com.oddsix.nutripro;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.utils.ViewUtil;
import com.oddsix.nutripro.utils.helpers.DialogHelper;

/**
 * Created by Filippe on 16/10/16.
 */

public class BaseActivity extends AppCompatActivity {
    private DialogHelper mDialogHelper;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FIX ORIENTATION TO PORTRAIT
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //TOOLBAR METHODS

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setToolbar(boolean displayHomeAsUpEnabled) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if(displayHomeAsUpEnabled){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("");
    }

    //TOAST METHODS

    public void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void functionNotImplemented(){
        showToast("função não implementada");
    }

    //ACTION BAR METHODS

    public void setToolbar(boolean displayHomeAsUpEnabled, String title) {
        setToolbar(displayHomeAsUpEnabled);
        setToolBarTitle(title);
    }

    public void setToolBarTitle(String title){
        setTitle(title);
    }

    //PROGRESS DIALOG METHODS

    public void showProgressdialog() {
        mDialogHelper = (mDialogHelper == null) ? new DialogHelper(this) : mDialogHelper;
        mDialogHelper.showProgressDialog();
    }

    public void dismissProgressDialog() {
        if (mDialogHelper != null) {
            mDialogHelper.dismissProgressDialog();
        }
    }

    //KEYBOARD METHODS
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            ViewUtil.hideKeyboard(this);
        }
    }

    //SWIPEREFRESH METHODS

    //MENU METHODS
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the listener to user touch outside the keyboard and hide it.
     * @param container
     */
    public void setKeyboardHiding(View container){
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
    }
}
