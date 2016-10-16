package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.utils.Constants;

/**
 * Created by Filippe on 16/10/16.
 */

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onEnterClicked(View view){

    }

    public void onRegisterClicked(View view) {
        Intent registerIntent = new Intent(view.getContext(), RegisterActivity.class);
        startActivityForResult(registerIntent, Constants.REQ_REGISTER);
    }
}
